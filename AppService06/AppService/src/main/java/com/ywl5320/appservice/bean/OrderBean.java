package com.ywl5320.appservice.bean;

/**
 * Created by ywl5320 on 2017/10/19.
 */
public class OrderBean extends BaseBean{

    private Integer id;
    private Integer userId;
    private String phone;
    private String orderNo;//订单号
    private String payMoney; //支付金额
    private String payGoods; //支付的商品名称
    private Integer payStatus;//支付状态 0：未支付 1：支付成功
    private String payTime;//支付时间
    private Integer payWay;//支付方式 1：支付宝  2：微信
    private String subject;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayGoods() {
        return payGoods;
    }

    public void setPayGoods(String payGoods) {
        this.payGoods = payGoods;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
