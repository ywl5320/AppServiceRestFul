package com.ywl5320.appservice.service;

import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.UserBean;
import com.ywl5320.appservice.dao.UserDao;
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
            return RestFulUtil.getInstance().getResuFulBean(user, 0, "登录成功");
        }
        else
        {
            return RestFulUtil.getInstance().getResuFulBean(null, 1, "用户不存在");
        }
    }

}
