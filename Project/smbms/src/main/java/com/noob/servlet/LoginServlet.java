package com.noob.servlet;

import com.noob.pojo.User;
import com.noob.service.user.UserService;
import com.noob.service.user.UserServiceImpl;
import com.noob.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    /*Servlet控制层，调用业务层代码*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet Start");
        /*从login.jsp获取用户名和密码*/
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        /*与数据库的密码进行对比，调用业务层*/
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode,userPassword);

        /*数据库存在此人*/
        if(user!=null){
            /*将用户信息存放到Session中*/
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            /*跳转到主页*/
            resp.sendRedirect("jsp/frame.jsp");

        }else{ /*查无此人,转发到登录页面，提示错误*/
            req.setAttribute("error","用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

