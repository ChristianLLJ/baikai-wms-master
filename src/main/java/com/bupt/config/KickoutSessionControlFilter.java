package com.bupt.config;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bupt.pojo.Staff;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;


/**
 *
 * @Title: KickoutSessionControlFilter.java
 * @Description: 同一用户后登陆踢出前面的用户
 * @date 2016年12月12日 下午7:25:40
 * @version V1.0
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private  static SessionManager sessionManager ;

    private  static CacheManager cacheManager ;
    // TODO 分布式集群环境下，需要改为redis
    private  static Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletResponse resp = (HttpServletResponse) response;
//        resp.setHeader("Access-Control-Allow-Origin", "http://8.141.172.245:8022");
//        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");
//        resp.setHeader("Access-Control-Max-Age", "3600");
//        resp.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");
//        resp.setHeader("Access-Control-Allow-Credentials", "true");
//        resp.setContentType("application/json;charset=utf-8");
//        System.out.println("设置了headers");
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        // 同步控制, 同步在本机的缓存中是有效的，但是一旦放入集群中，就会失效
        Deque<Serializable> deque = cache.get(username);
        System.out.println("当前缓存队列为" + deque);
        if(deque == null) {
            System.out.println("新建登陆队列");
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            System.out.println("session" + sessionId+"放入队列");
            deque.push(sessionId);
        }
        System.out.println("当前用户队列有" + deque);
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
            if(kickoutSession != null) {
                //设置会话的kickout属性表示踢出了
                kickoutSession.setAttribute("kickout", true);
            }
        }
        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                System.out.println("session"+sessionId+"被踢出了");
                subject.logout();
            } catch (Exception e) { //ignore
            }
            saveRequest(request);
            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
            httpServletResponse.sendError(HttpResultCodeEnum.LOGIN_KICKOUT.getCode(),HttpResultCodeEnum.LOGIN_KICKOUT.getMsg());
            return false;
        }
        return true;
    }
}
