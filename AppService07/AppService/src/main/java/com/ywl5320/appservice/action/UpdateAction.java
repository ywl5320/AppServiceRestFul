package com.ywl5320.appservice.action;

import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.UpdateBean;
import com.ywl5320.appservice.bean.UserBean;
import com.ywl5320.appservice.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ywl5320 on 2017/10/26.
 */
@Controller
@RequestMapping("/update")
public class UpdateAction {

    @Autowired
    private UpdateService updateService;

    @ResponseBody
    @RequestMapping(value="/checkupdate.do", method= RequestMethod.GET)
    public RestFulBean<UpdateBean> loginByPwd(String md5value, int versioncode, String channelid)
    {
        System.out.println("md5value:" + md5value);
        return updateService.checkUpdate(md5value, versioncode, channelid);
    }

}
