package cn.wsw.activiti.entity;

import java.util.Date;

import lombok.Data;

/**
 * 当前用户页面需填写、显示的表单相关信息
 * @author wensw
 */
@Data
public class Vacation {

    /**
     * 申请人
     */
    private String applyUser;
    /**
     * 请假市场
     */
    private int days;
    /**
     * 请假原因
     */
    private String reason;
    /**
     * 申请发起时间
     */
    private Date applyTime;
    /**
     * 申请状态
     */
    private String applyStatus;

    /**
     * 审核人
     */
    private String auditor;
    /**
     * 申核结果
     */
    private String result;
    /**
     * 审核时间
     */
    private Date auditTime;

}
