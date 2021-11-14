package com.noob.dao.user;

import com.noob.pojo.Role;
import com.noob.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    /*得到要登录用户*/
    public User getLoginUser(Connection connection, String usercode,String passWord) throws SQLException;

    /*修改当前用户密码*/
    public int updatePwd(Connection connection,int id,String password) throws SQLException;

    /*查询用户总数*/
    public int getUserCount(Connection connection,String username,int userRole) throws SQLException;

    /*条件查询userlist*/
    List<User> getUserList(Connection connection,String userName,int userRole ,int currentPageNo,int pageSize) throws Exception;
}
