package com.ywl5320.rxjavaretrofit.httpservice.serviceapi;


import com.ywl5320.rxjavaretrofit.beans.AliWxPayBean;
import com.ywl5320.rxjavaretrofit.httpservice.service.BaseApi;
import com.ywl5320.rxjavaretrofit.httpservice.service.HttpMethod;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by ywl on 2016/5/19.
 */
public class PayApi extends BaseApi {

    public static PayApi payApi;
    public PayService payService;
    public PayApi()
    {
        payService = HttpMethod.getInstance().createApi(PayService.class);
    }

    public static PayApi getInstance()
    {
        if(payApi == null)
        {
            payApi = new PayApi();
        }
        return payApi;
    }
    /*-------------------------------------获取的方法-------------------------------------*/


    public void createOrder(int userId, String phone, String payGoods, String payMoney, int payWay, String subject, Observer<AliWxPayBean> subscriber)
    {
        Observable observable = payService.createOrder(userId, phone, payGoods, payMoney, payWay, subject)
                .map(new HttpResultFunc<AliWxPayBean>());

        toSubscribe(observable, subscriber);
    }

}
