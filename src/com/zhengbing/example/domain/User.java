package com.zhengbing.example.domain;

/**
 * 用户实体类
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class User {

    private int userid;
    private String username;
    private String userhost;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserhost() {
        return userhost;
    }

    public void setUserhost(String userhost) {
        this.userhost = userhost;
    }
}
