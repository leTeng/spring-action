package com.eTeng.storage.memory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @FileName HomeController.java
 * @Author eTeng
 * @Date 2018/12/29 16:37
 * @Description 主页控制器
 */
@Controller
public class HomeController{

    /**
     * 主页控制器
     * @return
     */
    @GetMapping({"/","/index","/home"})
    public String toIndex(){
        return "index";
    }
}
