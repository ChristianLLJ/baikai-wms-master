package com.bupt.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

//public class ShiroWebSessionManager extends DefaultWebSessionManager {
//    @Override
//    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        /* 此方法获取客户端cookie的值,如果你的项目将sesssionId放在requestparam中，或者拼接在url中，请查看该方法源码，自行修改*/
//        String id = super.getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
//        if(id != null){
//            /*此处并非http 的session，是shiro在redis中缓存的session（SimpleSession）*/
//            /* 此方法是查询redis中的session，笔者在sessionDao中注入了redisManager如果你重写了RedisSessionDAO，则需要更改获取session的方法 */
//            Session session = /*(Session)redis.get(id)*/;
//            //if(session == null){
//                return null;
//            }
//        }
//        /*   如果redis中session未过期，此处必须调用父类获取方法 */
//        return super.getSessionId(request, response);
//    }
//}
