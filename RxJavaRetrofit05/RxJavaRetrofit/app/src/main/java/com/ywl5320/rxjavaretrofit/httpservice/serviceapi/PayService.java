package com.ywl5320.rxjavaretrofit.httpservice.serviceapi;


import com.ywl5320.rxjavaretrofit.beans.AliPayBean;
import com.ywl5320.rxjavaretrofit.httpservice.httpentity.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by ywl on 2016/5/19.
 */
public interface PayService {


    /**
     * 创建支付订单
     * @param userId
     * @param payGoods
     * @param payMoney
     * @param payWay
     * @param subject
     * @return
     */
    @PUT("pay/createOrder.do")
    Observable<HttpResult<AliPayBean>> createOrder(@Query("userId") int userId, @Query("payGoods") String payGoods, @Query("payMoney") String payMoney, @Query("payWay") int payWay, @Query("subject") String subject);
}
