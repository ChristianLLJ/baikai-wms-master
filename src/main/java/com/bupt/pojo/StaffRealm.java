package com.bupt.pojo;

import com.bupt.service.authority.UserService;
import com.bupt.service.authority.WorkGroupService;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class StaffRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    @Autowired
    WorkGroupService workGroupService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String)subject.getPrincipal();
        Staff staff = userService.selectUserByUsername(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(staff.getCurWorkGroup());
        List<Function> functions = workGroupService.selectWorkGroupFunction(staff.getCurWorkGroupId());
        for(int i = 0 ; i<functions.size();i++){
            info.addStringPermission(functions.get(i).getFunctionName());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        Staff staff = userService.selectUserByUsername(userToken.getUsername());
        if(staff == null){
            return null;
        }
        return new SimpleAuthenticationInfo(staff.getUsername(),staff.getPassword(),"");
    }
}
