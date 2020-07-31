package cn.wsw.activiti.entity;

import lombok.Data;

/**
 * 用户信息
 * @author wensw
 */
@Data
public class User {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户组id
     */
    private String groupId;

}
