package cn.wsw.activiti;


import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 用户数据初始化及相关查询crud
 * @author wensw
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ActivitiDemo1ApplicationTests {

    @Test
    public void contextLoads() {

    }

    @Test
    public void initUserInfo() {
        // 用户表和用户组表中加入用户信息
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService is = engine.getIdentityService();
        // 添加用户组
        Group empGroup = saveGroup(is, "empGroup", "员工组");
        Group manageGroup = saveGroup(is, "manageGroup", "经理组");
        Group dirGroup = saveGroup(is, "dirGroup", "总监组");
        // 添加用户
        User empA = saveUser(is, "empa"); // 员工a
        User empB = saveUser(is, "empb"); // 员工b
        User managea = saveUser(is, "managea"); // 经理a
        User manageb = saveUser(is, "manageb"); // 经理b
        User dira = saveUser(is, "dira"); // 总监a
        // 绑定关系
        saveRel(is, empA, empGroup);
        saveRel(is, empB, empGroup);
        saveRel(is, managea, manageGroup);
        saveRel(is, manageb, manageGroup);
        saveRel(is, dira, dirGroup);
    }

    // 为方便直接全部密码设为 123456
    User saveUser(IdentityService is, String id) {
        User u = is.newUser(id);
        u.setPassword("123456");
        is.saveUser(u);
        return u;
    }

    Group saveGroup(IdentityService is, String id, String name) {
        Group g = is.newGroup(id);
        g.setName(name);
        is.saveGroup(g);
        return g;
    }

    //调用identityService设置用户与组的关系
    void saveRel(IdentityService is, User u, Group g) {
        is.createMembership(u.getId(), g.getId());
    }

    @Test
    public void addCandidateStarterGroup() {
        //部署流程文件
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        Deployment dep = rs.createDeployment().addClasspathResource("processes/vacation.bpmn").deploy();
        //获取单个流程定义
        ProcessDefinition pd = rs.createProcessDefinitionQuery()
            .deploymentId(dep.getId()).singleResult();
        //设置起始候选用户组为empGroup
        rs.addCandidateStarterGroup(pd.getId(), "empGroup");
    }

/*
    // 查询流程部署表
    public void selectInDepolyment(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment= engine.getRepositoryService().createDeploymentQuery() //从流程部署表查找
            .deploymentId(deployId) //根据部署id查询
            .deploymentKey(deployKey) //根据部署key查询
            .deploymentCategory(deployCategory)
            .deploymentCategoryLike(deployCategory) //模糊
            .deploymentCategoryNotEquals(deployCategory) //不等于
            .listPage(int start,int pagesize) //分页
            .list() // 返回结果集
            .singleResule() //返回单条记录
            .count() // 统计数据
            .asc() ;//升序
    }
*/

 /*   //查询流程定义表
    public void selectInDefination(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        ProcessDefinition definition= engine.getRepositoryService().createProcessDefinitionQuery() //从流程定义表查找
            .processDefinitionKey(defKey) //根据流程定义key查询
            .processDefinitionId(defId) //根据流程定义id查询
            .processDefinitionVersion(version) //根据流程定义版本查询
            .orderByProcessDefinitionId() // 排序
            .listPage(int start,int pagesize) //分页
            .list() // 返回结果集
            .count() // 统计数据
            .asc() ;//升序
    }*/

/*    //删除流程定义
    public void deleteDefination(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
             engine.getRepositoryService()
            .deleteDeployment(deployId) // 只能删除未启动的流程，否则抛出异常
            .deleteDeployment(deployId,true) // 允许级联删除，可删除正常启动的流程
    }*/
/*
    //查询流程实例表
    public void selectProcessInstance(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance instance= engine.getRuntimeService().createProcessInstanceQuery()
            .processInstanceId(insId)
            .processDefinitionKey(defineKey)
            .orderByProcessInstanceId()
            .processInstanceNameLikeIgnoreCase(insName)
            .singleResult()
            .listPage(int start,int pageSize)
            .list();
    }*/
/*
    //任务查询
    public void selectTask(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<Task> taskList= engine.getTaskService().createTaskQuery()
            .taskCandidateOrAssigned(candidateUser/assignUser) // 个人或组用户查询
            .taskCandidateUser(candidateUser) // 组用户查询
            .taskAssignee(assignUser) // 个人用户查询
            .processInstanceId(instanceId) //流程实例id查询
            .processDefinitionKey(defineKey) //流程定义key查询
            .singleResult() //唯一结果集
            .list() //集合
            .listPage(int start,int pageSize); //分页
    }*/

  /*  // 设置流程变量
    public void setProcessVal(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getRuntimeService()
            .setVariables(execuId,Map<variaName,value>) // 根据执行对象id,设置对应流程变量key-value
            .setVariable(execuId , variaName, value) ;// 根据执行对对象ID,流程变量Key ->设置对应流程变量key的value
        engine.getTaskService()
            .setVariables(execuId,Map<variaName,value>) // 根据任务id,设置对应流程变量key-value
            .setVariable(taskId,variaName,value);// 根据任务ID,流程变量Key ->设置对应流程变量key的value

    }*/


/*    // 获取流程变量
    public void getProcessVal(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getRuntimeService()
            .getVariables(execuId) // 根据执行对象id,返回对应流程变量,Map<variaName,value>
            .getVariable(execuId , variaName) ;// 根据执行对对象ID,流程变量Key ->获取对应流程变量key的value
        engine.getTaskService()
            .getVariables(execuId) // 根据任务id,返回对应流程变量,Map<variaName,value>
            .getVariable(taskId,variaName);// 根据任务ID,流程变量Key ->获取对应流程变量key的value
    }*/

    /*// 流程历史数据查询
    public void getHistoryData(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getHistoryService()
            .createHistoricActivityInstanceQuery()// 历史活动节点查询-对应表act_hi_actinst
            .createHistoricTaskInstanceQuery() // 历史任务查询 -对应表act_hi_task
            .createHistoricVariableInstanceQuery() // 历史流程变量查询 -对应表act_hi_variable
            .createHistoricProcessInstanceQuery() // 历史流程实例查询 -对应表act_hi_procinst
    }*/
}
