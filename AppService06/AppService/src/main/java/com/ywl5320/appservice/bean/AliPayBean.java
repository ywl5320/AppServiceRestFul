package com.ywl5320.appservice.bean;

/**
 *  Created by ywl5320 on 2017/10/19.
 */
public class AliPayBean extends BaseBean{
	
	private String orderId;
	private String orderInfo;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}
}
