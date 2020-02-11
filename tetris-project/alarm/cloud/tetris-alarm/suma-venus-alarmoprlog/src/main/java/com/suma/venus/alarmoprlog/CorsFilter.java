package com.suma.venus.alarmoprlog;

/**
 * Created by Poemafar on 2019/12/23 16:07
 */

import org.springframework.web.cors.CorsUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 */
@WebFilter(filterName = "corsFilter",urlPatterns = "/*")
public class CorsFilter implements Filter {


    //初始化调用的方法
    //当服务器 被启动的时候，调用
    public void init(FilterConfig filterConfig) throws ServletException { }

    //拦截的方法
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if(CorsUtils.isCorsRequest(request)) {
            //解决跨域的问题
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));//直接使用请求域
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length,X-Requested-With,tetris-001");
            response.setHeader("Access-Control-Allow-Methods", "POST,GET");
            response.setHeader("Access-Control-Max-Age", "3600");
            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }

    }

    //销毁时候调用的方法
    public void destroy() { }
}
