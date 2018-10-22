package com.eTeng.profile.bean.impl;

import com.eTeng.profile.bean.ProfileBean;

/**
 * 普通的bean,不受环境影响
 */
public class CommosBean implements ProfileBean {
    public String getBeanName() {
        return "commosBean";
    }
}
