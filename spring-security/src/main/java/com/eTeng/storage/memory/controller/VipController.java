package com.eTeng.storage.memory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @FileName VipController.java
 * @Author eTeng
 * @Date 2018/12/29 17:27
 * @Description
 */
@Controller
@RequestMapping("/vip")
public class VipController{

    @GetMapping("/index")
    public String index(){
        return "user/vip";
    }
}
