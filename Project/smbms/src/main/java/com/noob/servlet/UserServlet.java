package com.noob.servlet;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.noob.pojo.Role;
import com.noob.pojo.User;
import com.noob.service.role.RoleServiceImpl;
import com.noob.service.user.UserService;
import com.noob.service.user.UserServiceImpl;
import com.noob.util.Constants;
import com.noob.util.PageSupport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd")&&method!=null){
            this.updatePwd(req,resp);
        } else if(method.equals("pwdmodify")&&method!=null){
            this.pwdModify(req,resp);
        }else if(method.equals("query")&&method!=null){
            this.query(req,resp);
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) {
        /*查询用户列表*/
        /*获取前端数据*/
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0 ;
        /*前端数据进行处理*/
        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;

        //第一次走该请求一定是第一页且页面大小固定的；
        int pageSize = 5;
        int currentPageNo = 1;

        if(queryUserName==null){
            queryUserName="";
        }
        if(temp!=null&&!temp.equals("")){
            queryUserRole =Integer.parseInt(temp); /*给查询赋值 0 1 2 3*/
        }
        if(pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        /*获取用户的总数*/

        int totalCount = userService.getUserCount(queryUserName,queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        /*int totalPageCount = pageSupport.getTotalPageCount();*/
        int totalPageCount = ((int)(totalCount/pageSize))+1;

        /*
        控制首页和尾页
        如果页面小于1显示第一页的东西
         */
        if (currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){ /*当前页面大于了最后一页*/
            currentPageNo = totalPageCount;
        }

        /*
        前端获取用户列表
        userlist.jsp
        <c:forEach var="user" items="${userList }" varStatus="status">
        */
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        /*前端页面保持查询参数*/
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);


        /*返回前端*/
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePwd(HttpServletRequest req,HttpServletResponse resp){
        /*从Session里面获取ID*/
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if(o!=null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                req.setAttribute("message","修改密码成功,请使用新密码重新登录");
                /*密码修改成功，移除当前Session*/
                req.getSession().removeAttribute(Constants.USER_SESSION);

            }else{
                req.setAttribute("message","修改密码失败");
            }

        }else{
            req.setAttribute("message","新密码存在问题");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //验证旧密码 session中有用户密码
    public void pwdModify(HttpServletRequest req,HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");


        Map<String,String> resultMap = new HashMap<String,String>();
        if(o==null){ /*session失效*/
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)) { /*输入旧密码为空*/
            resultMap.put("result","error");
        }else{
            String userPassword = ((User)o).getUserPassword(); //Session中用户的密码
            if(oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else{
                resultMap.put("result","false");
            }

        }
        /*JSONArray 阿里巴巴的JSON工具类
            resultMap = ["result","sessionerror","result","error"]
            Json格式 = {key：value}
          */
        try{
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            /*
            Ajax
            JSONArray 阿里巴巴的JSON工具类, 转换格式 fastjson
            resultMap = ["result","sessionerror","result","error"]
            Json格式 = {key：value}
             */
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}