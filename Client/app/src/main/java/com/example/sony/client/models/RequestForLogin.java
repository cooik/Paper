package com.example.sony.client.models;

/**
 * Created by sony on 2017/4/16.
 */

public class RequestForLogin {
    public String work_id;
    public String password;

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
