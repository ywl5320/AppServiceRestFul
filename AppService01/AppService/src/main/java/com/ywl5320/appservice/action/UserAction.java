package com.ywl5320.appservice.action;

import com.ywl5320.appservice.bean.FestFulBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ywl on 2017-10-2.
 */
@Controller
@RequestMapping("/user")
public class UserAction {

    @ResponseBody
    @RequestMapping(value="/loginByPwd.do", method= RequestMethod.GET)
    public FestFulBean<String> loginByPwd(@RequestParam String username, @RequestParam String password)
    {
        FestFulBean<String> restful = new FestFulBean<String>();
        restful.setData("hello, " + username + " welcom to my website!");
        restful.setStatus(0);
        restful.setMsg("成功");
        return restful;
    }

}
