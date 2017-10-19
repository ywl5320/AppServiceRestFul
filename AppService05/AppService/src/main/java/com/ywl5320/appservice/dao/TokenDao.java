package com.ywl5320.appservice.dao;

import com.ywl5320.appservice.bean.TokenBean;

/**
 * Created by ywl5320 on 2017/10/13.
 */
public interface TokenDao {

    /**
     * 登录或注册时写入token
     * @param tokenBean
     */
    void saveOrUpdageToken(TokenBean tokenBean);

    /**
     * 根据电话号码获取token
     * @param phone
     * @return
     */
    TokenBean isTokenAvailable(String phone);

}
