package cn.wsw.activiti.service.impl;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.wsw.activiti.entity.UserGroup;
import cn.wsw.activiti.service.api.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private IdentityService identityService;

    @Override
    public boolean login(String userName, String password) {
        return identityService.checkPassword(userName, password);
    }

    @Override
    public Object getAllUser() {
        List<User> userList = identityService.createUserQuery().list();
        return toMyUser(userList);
    }

    @Override
    public Object getAllGroup() {
        List<Group> groupList = identityService.createGroupQuery().list();
        List<UserGroup> userGroupList = new ArrayList<>();
        for (Group group : groupList) {
            UserGroup userGroup = new UserGroup();
            userGroup.setId(group.getId());
            userGroup.setName(group.getName());
            userGroupList.add(userGroup);
        }
        return userGroupList;
    }

    @Override
    public Object getUserGroup(String groupId) {
        List<User> userList = identityService.createUserQuery().memberOfGroup(groupId).list();
        return toMyUser(userList);
    }

    @Override
    public List<cn.wsw.activiti.entity.User> toMyUser(List<User> userList) {
        List<cn.wsw.activiti.entity.User> myUserList = new ArrayList<>();
        for (User user : userList) {
            cn.wsw.activiti.entity.User myUser = new cn.wsw.activiti.entity.User();
            myUser.setUserName(user.getId());
            myUser.setPassword(user.getPassword());
            myUserList.add(myUser);
        }
        return myUserList;
    }

    @Override
    public Object addUser(cn.wsw.activiti.entity.User user) {
        String userId = user.getUserName();
        String groupId = user.getGroupId();
        User actUser = identityService.newUser(userId);
        actUser.setPassword(user.getPassword());
        identityService.saveUser(actUser);
        identityService.createMembership(userId, groupId);
        return true;
    }
}
