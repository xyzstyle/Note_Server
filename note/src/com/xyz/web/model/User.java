package com.xyz.web.model;

/**
 * @author zzz
 * @date Feb 19, 2014 用户
 */
public class User {


    private long id;
    private String name;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.name = username;
        this.password = password;
    }

    public User(int id, String name, String password) {
        this.setId(id);
        this.setName(name);
        this.setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
