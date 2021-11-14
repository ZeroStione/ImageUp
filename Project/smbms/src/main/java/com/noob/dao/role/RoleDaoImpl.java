package com.noob.dao.role;

import com.noob.dao.BaseDao;
import com.noob.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
        public List<Role> getRoleList(Connection connection) throws Exception {

            PreparedStatement pstm = null;
            ResultSet rs = null;
            ArrayList<Role> rolelist = new ArrayList<Role>();
            if(connection!=null){
                String sql = "select * from smbms_role";
                Object[] params = {};
                rs = BaseDao.excute(connection,pstm,rs,sql,params);

                while(rs.next()){
                    Role _role = new Role();
                    _role.setId(rs.getInt("id"));
                    _role.setRoleCode(rs.getString("roleCode"));
                    _role.setRoleName(rs.getString("roleName"));
                    rolelist.add(_role);
                }
                BaseDao.closeResource(null,pstm,rs);
            }
            return rolelist;
        }
}
