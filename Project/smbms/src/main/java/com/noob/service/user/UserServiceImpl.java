package com.noob.service.user;

import com.noob.dao.BaseDao;
import com.noob.dao.user.UserDao;
import com.noob.dao.user.UserDaoImpl;
import com.noob.pojo.User;
import org.junit.Test;
import sun.reflect.misc.ConstructorUtil;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{
    /*业务层需要调用Dao层,即需要引入Dao层*/
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            /*调用业务层调用对应的数据库操作*/
            user = userDao.getLoginUser(connection, userCode,password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        if(user!=null&&!password.equals(user.getUserPassword())){
            user=null;
        }
        return user;
    }

    public boolean updatePwd(int id, String pwd) {
        Connection connection = null;
        boolean flag=false;
        /*修改密码*/
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,pwd)>0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count=0;
        try {
            connection = BaseDao.getConnection();
            count=userDao.getUserCount(connection,username,userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);

        }
        return count;
    }

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
            Connection connection = null;
            List<User> userList = null;
            System.out.println("queryUserName ---- > " + queryUserName);
            System.out.println("queryUserRole ---- > " + queryUserRole);
            System.out.println("currentPageNo ---- > " + currentPageNo);
            System.out.println("pageSize ---- > " + pageSize);
            try {
                connection = BaseDao.getConnection();
                userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                BaseDao.closeResource(connection, null, null);
            }
            return userList;
    }


    @Test
    public void testGetUserCount(){
        UserServiceImpl userService = new UserServiceImpl();
        int adminCount = userService.getUserCount(null,1);
        System.out.println(adminCount);
    }


    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User admin = userService.login("liming","12344");
        System.out.println(admin.getUserPassword());
    }
}
