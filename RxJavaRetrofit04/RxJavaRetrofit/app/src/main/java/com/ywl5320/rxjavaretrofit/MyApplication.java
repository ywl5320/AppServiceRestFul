package com.ywl5320.rxjavaretrofit;

import android.app.Application;
import android.text.TextUtils;

import com.ywl5320.rxjavaretrofit.httpservice.beans.UserBean;

/**
 * Created by ywl on 2016/6/24.
 */
public class MyApplication extends Application{

    private static MyApplication instance;

    private UserBean userBean;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getToken()
    {
        if(userBean != null)
        {
            if(TextUtils.isEmpty(userBean.getToken())) {
                return "";
            }
            return userBean.getToken();
        }
        return "";
    }

    public String getPhone()
    {
        if(userBean != null)
        {
            if(TextUtils.isEmpty(userBean.getPhone())) {
                return "";
            }
            return userBean.getPhone();
        }
        return "";
    }
}
