package cn.wsw.activiti.entity;

import java.util.Date;

import lombok.Data;

/**
 * 当前对应用户的任务信息
 * @author wensw
 */
@Data
public class VacTask {

    /**
     * 任务id
     */
    private String id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 请假表单对象
     */
    private Vacation vac;
    /**
     * 创建时间
     */
    private Date createTime;
}
