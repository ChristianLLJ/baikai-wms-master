package com.bupt.config;

import com.bupt.pojo.Staff;
import com.bupt.pojo.StaffRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.ehcache.core.EhcacheManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiro过滤器工厂
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager")DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String, Filter> filters = bean.getFilters();
        KickoutSessionControlFilter kickout= new KickoutSessionControlFilter();
        kickout.setSessionManager(getDefaultWebSessionManager());
        kickout.setCacheManager(getEhCacheManager());
        filters.put("authc",new KickoutSessionControlFilter());
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**", "authc");
//        filterMap.put("/**", "authc");  //注意这里！！！大佬帮我注销的这两行，不注销一样成功
        //所有请求都要认证
//        filterMap.put("/**", "perms");  //注意这里！！！大佬帮我注销的这两行，不注销一样成功
        bean.setFilters(filters);//添加过滤器
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }
    //shiro安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("staffRealm")StaffRealm staffRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(staffRealm);
        securityManager.setSessionManager(getDefaultWebSessionManager());
        return securityManager;
    }
    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        manager.setCacheManager(getEhCacheManager());
        // 删除过期的session
        manager.setDeleteInvalidSessions(true);
        // 设置全局session超时时间
        manager.setGlobalSessionTimeout(3600 * 1000);
        // 定义要使用的无效的Session定时调度器
//        manager.setSessionValidationScheduler(sessionValidationScheduler());
        // 是否定时检查session
        manager.setSessionValidationSchedulerEnabled(true);
        return manager;
    }
    /**
     * 配置session管理器
     * @return
     */
    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        return em;
    }
    //shiro权限管理域
    @Bean
    public StaffRealm staffRealm(){
        return new StaffRealm();
    }
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
    /**
     * 交给spring容器管理
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
