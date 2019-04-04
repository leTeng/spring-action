package com.eTeng.storage.memory.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

/**
 * @FileName UserControl.java
 * @Author eTeng
 * @Date 2018/12/29 16:28
 * @Description 用户控制器
 */
@Controller
public class UserController{

    /**
     * 登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 用户信息页面
     * @return
     */
    @GetMapping("/user")
    public String userInfo(@AuthenticationPrincipal Principal principal,Model model){
        model.addAttribute("username",principal.getName());
        return "user/user";
    }
}
