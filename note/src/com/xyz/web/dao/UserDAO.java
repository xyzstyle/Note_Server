package com.xyz.web.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.xyz.web.model.User;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author zzz
 * Dec 4, 2011 2:52:09 PM
 */
public class UserDAO {
    private SqlManager manager;
    private String sql = "";
    private ResultSet rs;

    public UserDAO() throws IOException, ClassNotFoundException, SQLException {
        manager = SqlManager.createInstance();
    }

    /**
     * 验证用户名和密码是否正确
     *
     * @param uname    用户名
     * @param password 密码
     */
    public boolean validate(String uname, String password) throws SQLException {
        boolean result = false;
        sql = "select count(id) from user where name=? and password=?";
        Object[] params = new Object[]{uname, password};
        manager.connectDB();
        rs = manager.executeQuery(sql, params);
        if (rs.next() && rs.getLong(1) == 1) {
            result = true;
        }
        manager.closeDB();
        return result;
    }

    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        sql = "select * from user";

        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        while (rs.next()) {
            User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
            list.add(user);
        }
        manager.closeDB();
        return list;
    }

    public User getUserById(String id) throws SQLException {
        sql = "select * from user where id=" + id;
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        if (rs.next()) {
            return new User(rs.getInt("id"),
                    rs.getString("name"), rs.getString("password"));

        }
        manager.closeDB();
        return null;
    }

    /**
     * 更新用户信息， 注意：只能根据用户名更新密码
     */
    public boolean insert(User user) throws SQLException {
        String name = user.getName();
        String password = user.getPassword();
        boolean result;
        sql = "insert into user(name,password) values(? ,?)";
        Object[] params = new Object[]{name, password};
        manager.connectDB();
        result = manager.executeUpdate(sql, params);
        manager.closeDB();
        return result;
    }

    public boolean checkUser(User user) throws SQLException {
        sql = "select * from user where id=" + user.getId() + " and password=" + user.getPassword();
        manager.connectDB();
        rs = manager.executeQuery(sql, null);
        if (rs != null && rs.next()) {
            return true;
        }
        manager.closeDB();
        return false;
    }

    public void update(String name, String password) {
        throw new NotImplementedException();
    }

}
