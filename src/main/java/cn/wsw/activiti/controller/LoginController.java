package cn.wsw.activiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.wsw.activiti.entity.User;
import cn.wsw.activiti.service.impl.UserServiceImpl;

/**
 * 登录控制层
 * @author wensw
 */
@Controller
public class LoginController {

    @Resource
    private UserServiceImpl userService;

    @PostMapping("/login")
    @ResponseBody
    public boolean login(HttpSession session, @RequestBody User user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        boolean login = userService.login(userName, password);
        if (login) {
            session.setAttribute("userName", userName);
            return true;
        }
        return false;
    }


    @GetMapping("/exit")
    public String exit(HttpSession session) {
        session.removeAttribute("userName");
        return "login";
    }
}
