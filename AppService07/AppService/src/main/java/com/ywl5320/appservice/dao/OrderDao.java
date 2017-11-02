package com.ywl5320.appservice.dao;

import com.ywl5320.appservice.bean.OrderBean;

/**
 * Created by ywl5320 on 2017/10/19.
 */
public interface OrderDao {

    /**
     * 生成订单
     * @param orderBean
     */
    void createOrder(OrderBean orderBean);

    /**
     * 根据订单ID获取订单
     * @param orderNo
     * @return
     */
    OrderBean getOrderById(int userId, String orderNo);

    /**
     * 更新订单
     * @param orderBean
     */
    void updateOrder(OrderBean orderBean);


    /**
     * 获取订单信息
     * @param orderNo
     * @return
     */
    OrderBean getOrderByOrNo(String orderNo);

}
