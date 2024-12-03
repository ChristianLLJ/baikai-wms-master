//package com.bupt.config;
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//@WebFilter("/*")
//public class CORSFilter implements Filter {
//    public CORSFilter() {
//    }
//    public void destroy() {
//    }
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        //设置跨域请求
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,HEAD,PUT,PATCH");
//        response.setHeader("Access-Control-Max-Age", "36000");
//        response.setHeader("Access-Control-Allow-Headers", "lineId,Origin, X-Requested-With, Content-Type, Accept,Authorization,authorization");
//        response.setHeader("Access-Control-Allow-Credentials","true");
//        chain.doFilter(req, response);
//    }
//    public void init(FilterConfig fConfig) throws ServletException {
//    }
//}
