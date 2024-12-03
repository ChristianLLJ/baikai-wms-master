package com.bupt.service.authority.impl;

import com.bupt.pojo.Staff;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    UserService userService;
    @Override
    public Boolean auth() {
        Subject subject = SecurityUtils.getSubject();
        String username = (String)subject.getPrincipal();
        Staff staff = userService.selectUserByUsername(username);
        String permit = Thread.currentThread().getStackTrace()[2].getMethodName();
        return subject.isPermitted(permit);
    }
}
