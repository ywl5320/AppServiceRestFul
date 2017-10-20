package com.ywl5320.rxjavaretrofit.httpservice.serviceapi;


import com.ywl5320.rxjavaretrofit.beans.UserBean;
import com.ywl5320.rxjavaretrofit.httpservice.service.BaseApi;
import com.ywl5320.rxjavaretrofit.httpservice.service.HttpMethod;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by ywl on 2016/5/19.
 */
public class UserApi extends BaseApi {

    public static UserApi userApi;
    public UserService userService;
    public UserApi()
    {
        userService = HttpMethod.getInstance().createApi(UserService.class);
    }

    public static UserApi getInstance()
    {
        if(userApi == null)
        {
            userApi = new UserApi();
        }
        return userApi;
    }
    /*-------------------------------------获取的方法-------------------------------------*/

    public void register(String phone, String password, String username, int sex, int age, Observer<UserBean> subscriber)
    {
        UserBean userBean = new UserBean();
        userBean.setPhone(phone);
        userBean.setPassword(password);
        userBean.setUsername(username);
        userBean.setSex(sex);
        userBean.setAge(age);
        Observable observable = userService.register(userBean)
                .map(new HttpResultFunc<UserBean>());

        toSubscribe(observable, subscriber);
    }

    public void login(String phone, String password, Observer<UserBean> subscriber)
    {
        Observable observable = userService.login(phone, password)
                .map(new HttpResultFunc<UserBean>());

        toSubscribe(observable, subscriber);
    }

    public void userinfo(String phone, Observer<UserBean> subscriber)
    {
        Observable observable = userService.userinfo(phone)
                .map(new HttpResultFunc<UserBean>());

        toSubscribe(observable, subscriber);
    }

}
