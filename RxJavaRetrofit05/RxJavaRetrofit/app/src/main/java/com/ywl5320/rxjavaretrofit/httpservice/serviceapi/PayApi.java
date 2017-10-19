package com.ywl5320.rxjavaretrofit.httpservice.serviceapi;


import com.ywl5320.rxjavaretrofit.beans.AliPayBean;
import com.ywl5320.rxjavaretrofit.beans.BaseBean;
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


    public void createOrder(int userId, String payGoods, String payMoney, int payWay, String subject, Observer<AliPayBean> subscriber)
    {
        Observable observable = payService.createOrder(userId, payGoods, payMoney, payWay, subject)
                .map(new HttpResultFunc<AliPayBean>());

        toSubscribe(observable, subscriber);
    }

}
