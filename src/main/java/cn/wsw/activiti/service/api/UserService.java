package cn.wsw.activiti.service.api;

import org.activiti.engine.identity.User;

import java.util.List;

/**
 * 用户服务相关
 * @author wensw
 */
public interface UserService {
    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    boolean login(String userName, String password);

    /**
     * 获取所有用户
     * @return
     */
    Object getAllUser();

    /**
     * 获取所有的组
     * @return
     */
    Object getAllGroup();

    /**
     * 根据指定组id获取对应的用户
     * @param groupId
     * @return
     */
    Object getUserGroup(String groupId);

    /**
     * 将流程引擎实体类的用户信息转换为普通用户信息
     * @param userList
     * @return
     */
    List<cn.wsw.activiti.entity.User> toMyUser(List<User> userList);

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    Object addUser(cn.wsw.activiti.entity.User user);
}
