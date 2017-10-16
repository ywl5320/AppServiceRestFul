package com.ywl5320.appservice.util;

import com.ywl5320.appservice.bean.RestFulBean;

/**
 * Created by ywl5320 on 2017-10-15.
 */
public class RestFulUtil<T> {

    private RestFulUtil(){}

    public static RestFulUtil getInstance()
    {
        return new RestFulUtil();
    }

    public RestFulBean<T> getResuFulBean(T o, int status, String msg)
    {
        RestFulBean<T> objectRestFulBean = new RestFulBean<T>();
        objectRestFulBean.setStatus(status);
        objectRestFulBean.setMsg(msg);
        objectRestFulBean.setData(o);
        return objectRestFulBean;
    }

}
