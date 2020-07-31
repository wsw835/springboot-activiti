package cn.wsw.activiti.service.api;

import org.activiti.engine.runtime.ProcessInstance;

import cn.wsw.activiti.entity.VacTask;
import cn.wsw.activiti.entity.Vacation;

/**
 * 请假流程服务相关
 * @author wensw
 */
public interface VocationService {
    /**
     * 根据当前用户开启请假相关任务
     * @param userName
     * @param vac
     * @return
     */
    Object startVac(String userName, Vacation vac);

    /**
     * 根据当前用户获取对应的请假相关任务信息
     * @param userName
     * @return
     */
    Object myVac(String userName);

    /**
     * 根据流程实例获取当前的请假相关参数
     * @param instance
     * @return
     */
    Vacation getVac(ProcessInstance instance);

    /**
     * 根据用户名获取其对应的任务信息
     * @param userName
     * @return
     */
    Object myAudit(String userName);

    /**
     * 完成当前用户对应的请假流程所属任务
     * @param userName
     * @param vacTask
     * @return
     */
    Object passAudit(String userName, VacTask vacTask);

    /**
     * 获取当前用户的对应请假申请的结果
     * @param userName
     * @return
     */
    Object myVacRecord(String userName);

    /**
     * 返回当前用户的对应请假审批的结果
     * @param userName
     * @return
     */
    Object myAuditRecord(String userName);
}
