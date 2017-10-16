package com.ywl5320.appservice.bean;

/**
 * Created by ywl5320 on 2017/10/13.
 */
public class TokenBean extends BaseBean{

    private Integer id;
    private String phone;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
