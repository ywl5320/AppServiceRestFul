package com.ywl5320.appservice.action;

import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.UserBean;
import com.ywl5320.appservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ywl on 2017-10-2.
 */
@Controller
@RequestMapping("/user")
public class UserAction {

    @Autowired
    private UserService userService;


    /**
     * 注册
     * @param userBean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/register.do", method= RequestMethod.PUT)
    public RestFulBean<UserBean> register(@RequestBody UserBean userBean)
    {
        System.out.println("phone:" + userBean.getPhone());
        return userService.registorServer(userBean);
    }

    /**
     * 登录
     * @param phone
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/loginbypwd.do", method= RequestMethod.POST)
    public RestFulBean<UserBean> loginByPwd(String phone, String password)
    {
        System.out.println("phone:" + phone);
        return userService.login(phone, password);
    }

}
