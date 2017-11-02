package com.ywl5320.appservice.dao;

import com.ywl5320.appservice.bean.UserBean;

/**
 * Created by ywl5320 on 2017/10/12.
 */
public interface UserDao {

    /**
     * 注册
     * @param userBean
     */
    UserBean registor(UserBean userBean);

    /**
     * 登陆
     * @return
     */
    UserBean login(String phone, String password);

    /**
     * 根据名字获取用户信息
     * @param phone
     * @return
     */
    UserBean getUser(String phone);

}
