package cn.wsw.activiti.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.wsw.activiti.entity.VacTask;
import cn.wsw.activiti.entity.Vacation;
import cn.wsw.activiti.service.api.VocationService;
import cn.wsw.activiti.util.ActivitiUtil;

@Service
public class VacationServiceImpl implements VocationService {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    /**
     * 流程定义key -》对应bpmn文件的id
     */
    private static final String PROCESS_DEFINE_KEY = "vacationProcess";


    @Override
    public Object startVac(String userName, Vacation vac) {
        identityService.setAuthenticatedUserId(userName);
        // 开始流程
        ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINE_KEY);
        // 查询当前任务
        Task currentTask = taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
        // 申明任务
        taskService.claim(currentTask.getId(), userName);
        Map<String, Object> vars = new HashMap<>(4);
        vars.put("applyUser", userName);
        vars.put("days", vac.getDays());
        vars.put("reason", vac.getReason());
        taskService.complete(currentTask.getId(), vars);
        return true;
    }

    @Override
    public Object myVac(String userName) {
        // 查询该用户创建了多少个流程市里，也就是发起了多少次申请或者审核
        List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().startedBy(userName).list();
        List<Vacation> vacList = new ArrayList<>();
        for (ProcessInstance instance : instanceList) {
            Vacation vac = getVac(instance);
            vacList.add(vac);
        }
        return vacList;
    }

    @Override
    public Vacation getVac(ProcessInstance instance) {
        // 拿到表单参数
        Integer days = runtimeService.getVariable(instance.getId(), "days", Integer.class);
        String reason = runtimeService.getVariable(instance.getId(), "reason", String.class);
        Vacation vac = new Vacation();
        // 设置申请人，申请原因等信息
        vac.setApplyUser(instance.getStartUserId());
        vac.setDays(days);
        vac.setReason(reason);
        // activiti 6才有的方法，获取流程市里的开始时间
        Date startTime = instance.getStartTime();
        vac.setApplyTime(startTime);
        // 设置申请状态
        vac.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
        return vac;
    }


    @Override
    public Object myAudit(String userName) {
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(userName)
            .orderByTaskCreateTime().desc().list();
        List<VacTask> vacTaskList = new ArrayList<>();
        //这里其实是对任务的相关信息进行了一层转换
        for (Task task : taskList) {
            VacTask vacTask = new VacTask();
            // 信息转换 task -> vacTask
            vacTask.setId(task.getId());
            vacTask.setName(task.getName());
            vacTask.setCreateTime(task.getCreateTime());
            // 从当前任务获取对应流程实例
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            Vacation vac = getVac(instance);
            vacTask.setVac(vac);
            vacTaskList.add(vacTask);
        }
        return vacTaskList;
    }

    @Override
    public Object passAudit(String userName, VacTask vacTask) {
        //审核通过，把对应的表单参数获取后调用TaskService的complete(taskId,Map<String,Object>)完成任务流转
        String taskId = vacTask.getId();
        String result = vacTask.getVac().getResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("result", result);
        vars.put("auditor", userName);
        vars.put("auditTime", new Date());
        taskService.claim(taskId, userName);
        taskService.complete(taskId, vars);
        return true;
    }

    /**
     * 查询当前用户的相关信息
     * @param userName
     * @return
     */
    @Override
    public Object myVacRecord(String userName) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
            .processDefinitionKey(PROCESS_DEFINE_KEY).startedBy(userName).finished()
            .orderByProcessInstanceEndTime().desc().list();

        List<Vacation> vacList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            Vacation vacation = new Vacation();
            vacation.setApplyUser(hisInstance.getStartUserId());
            vacation.setApplyTime(hisInstance.getStartTime());
            vacation.setApplyStatus("申请结束");
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(hisInstance.getId()).list();
            ActivitiUtil.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }
        return vacList;
    }


    @Override
    public Object myAuditRecord(String userName) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
            .processDefinitionKey(PROCESS_DEFINE_KEY).involvedUser(userName).finished()
            .orderByProcessInstanceEndTime().desc().list();

        List<String> auditTaskNameList = new ArrayList<>();
        auditTaskNameList.add("经理审批");
        auditTaskNameList.add("总监审批");
        List<Vacation> vacList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(hisInstance.getId()).processFinished()
                .taskAssignee(userName)
                .taskNameIn(auditTaskNameList)
                .orderByHistoricTaskInstanceEndTime().desc().list();
            boolean isMyAudit = false;
            for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
                if (taskInstance.getAssignee().equals(userName)) {
                    isMyAudit = true;
                }
            }
            if (!isMyAudit) {
                continue;
            }
            Vacation vacation = new Vacation();
            vacation.setApplyUser(hisInstance.getStartUserId());
            vacation.setApplyStatus("申请结束");
            vacation.setApplyTime(hisInstance.getStartTime());
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(hisInstance.getId()).list();
            ActivitiUtil.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }
        return vacList;
    }
}
