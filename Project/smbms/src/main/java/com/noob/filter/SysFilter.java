package com.noob.filter;


import com.noob.pojo.User;
import com.noob.util.Constants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        /*从Session中获取用户*/
        User user = (User)request.getSession().getAttribute(Constants.USER_SESSION);
        /*用户已被注销或未登录*/
        if(user==null){
             response.sendRedirect(request.getContextPath()+"/error.jsp");
        }else{
            chain.doFilter(req,resp);
        }

    }

    public void destroy() {

    }
}
