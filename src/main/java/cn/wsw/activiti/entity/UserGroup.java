package cn.wsw.activiti.entity;

import lombok.Data;

/**
 * 用户组信息
 * @author wensw
 */
@Data
public class UserGroup {
    /**
     * 用户组id
     */
    private String id;
    /**
     * 用户组名称
     */
    private String name;
}
