package com.ywl5320.appservice.dao;

import com.ywl5320.appservice.bean.OrderBean;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by ywl5320 on 2017/10/19.
 */
public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao{

    public void createOrder(OrderBean orderBean) {
        // TODO Auto-generated method stub
        this.getHibernateTemplate().save(orderBean);
    }

    public OrderBean getOrderById(int userId, String orderNo) {
        // TODO Auto-generated method stub
        List<OrderBean> orders = (List<OrderBean>) this.getHibernateTemplate().find("from OrderBean where userId=? and orderNo=?", userId, orderNo);
        if(orders != null && orders.size() > 0)
        {
            return orders.get(0);
        }
        return null;
    }

    public void updateOrder(OrderBean orderBean) {
        // TODO Auto-generated method stub
        this.getHibernateTemplate().update(orderBean);

    }

    public OrderBean getOrderByOrNo(String orderNo) {
        // TODO Auto-generated method stub
        List<OrderBean> orders = (List<OrderBean>) this.getHibernateTemplate().find("from OrderEntity where orderNo=?", orderNo);
        if(orders != null && orders.size() > 0)
        {
            return orders.get(0);
        }
        return null;
    }

}
