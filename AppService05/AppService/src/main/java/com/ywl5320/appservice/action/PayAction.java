package com.ywl5320.appservice.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ywl5320.appservice.bean.BaseBean;
import com.ywl5320.appservice.bean.OrderBean;
import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.UserBean;
import com.ywl5320.appservice.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ywl5320 on 2017/10/19.
 */
@Controller
@RequestMapping("/pay")
public class PayAction {

    @Autowired
    private PayService payService;

    String alilock = "alilock";

    /**
     * 生成订单
     * @param userId
     * @param payGoods
     * @param payMoney
     * @param payWay
     * @param subject
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/createOrder.do", method= RequestMethod.PUT)
    public RestFulBean<BaseBean> register(int userId, String payGoods, String payMoney, int payWay, String subject)
    {
        return payService.createOrder(userId, payGoods, payMoney, payWay, subject);
    }

    /**
     * 支付宝回调接口
     * @param request
     * @param resp
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/verifyalipayresult.do", method=RequestMethod.POST)
    public String verifyAliPayRight(HttpServletRequest request, HttpServletResponse resp)
    {
        synchronized (alilock) {
            Map<String,String> params = new HashMap<String,String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            try {
                boolean flag = AlipaySignature.rsaCheckV1(params, PayService.ALI_PUBLIC_KEY, "utf-8", "RSA2");
                if(flag)
                {
                    if(params.get("trade_status").equals("TRADE_SUCCESS") && params.get("app_id").equals(PayService.ALI_APPID) && params.get("seller_id").equals("2088621136650617"))
                    {
                        return payService.verifyAliPay(params);
                    }
                }
            } catch (AlipayApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "fail";
            }
        }
        return "fail";
    }

}
