package com.ywl5320.appservice.service;

import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.TokenBean;
import com.ywl5320.appservice.bean.UserBean;
import com.ywl5320.appservice.dao.TokenDao;
import com.ywl5320.appservice.dao.UserDao;
import com.ywl5320.appservice.util.MD5;
import com.ywl5320.appservice.util.RestFulUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ywl5320 on 2017/10/12.
 */
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenDao tokenDao;

    public RestFulBean<UserBean> registorServer(UserBean userBean)
    {
        UserBean user = userDao.getUser(userBean.getPhone());
        if(user != null)
        {
            return RestFulUtil.getInstance().getResuFulBean(null, 1, "已经注册过了");
        }
        else
        {

            user = userDao.registor(userBean);
            if(user != null)
            {
                saveOrUpdateToken(user);
                return RestFulUtil.getInstance().getResuFulBean(user, 0, "注册成功");
            }
            else{
                return RestFulUtil.getInstance().getResuFulBean(null, 1, "注册失败");
            }
        }

    }

    public RestFulBean<UserBean> login(String phone, String password)
    {
        UserBean user = userDao.login(phone, password);
        if(user != null)
        {
            saveOrUpdateToken(user);
            return RestFulUtil.getInstance().getResuFulBean(user, 0, "登录成功");
        }
        else
        {
            return RestFulUtil.getInstance().getResuFulBean(null, 1, "用户不存在");
        }
    }

    public RestFulBean<UserBean> userinfo(String phone)
    {
        UserBean userBean = userDao.getUser(phone);
        if(userBean != null)
        {
            return RestFulUtil.getInstance().getResuFulBean(userBean, 0, "获取成功");
        }
        return RestFulUtil.getInstance().getResuFulBean(null, 1, "用户不存在");
    }

    private void saveOrUpdateToken(UserBean userBean){
        String token = null;
        try {
            token = MD5.encryptMD5(String.valueOf(System.currentTimeMillis()) + "appservice.02154778ke783dad34");
        } catch (Exception e) {
            e.printStackTrace();
        }
        userBean.setToken(token);
        TokenBean tokenBean = tokenDao.isTokenAvailable(userBean.getPhone());
        if(tokenBean != null)
        {
            tokenBean.setToken(token);
        }
        else
        {
            tokenBean = new TokenBean();
            tokenBean.setPhone(userBean.getPhone());
            tokenBean.setToken(userBean.getToken());
        }
        tokenDao.saveOrUpdageToken(tokenBean);
    }

}
