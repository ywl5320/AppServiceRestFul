package com.ywl5320.appservice.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ywl5320.appservice.bean.BaseBean;
import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.service.PayService;
import com.ywl5320.appservice.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by ywl5320 on 2017/10/19.
 */
@Controller
@RequestMapping("/pay")
public class PayAction {

    @Autowired
    private PayService payService;

    String alilock = "alilock";
    String wxlock = "wxlock";

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
    public RestFulBean<BaseBean> register(int userId, String phone, String payGoods, String payMoney, int payWay, String subject)
    {
        return payService.createOrder(userId, phone, payGoods, payMoney, payWay, subject);
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
                    if(params.get("trade_status").equals("TRADE_SUCCESS") && params.get("app_id").equals(PayService.ALI_APPID) && params.get("seller_id").equals("****************"))
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

    /**
     * 微信回调接口
     * @param request
     * @param resp
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/verifywxpayresult.do", method=RequestMethod.POST)
    public String verifyWxPayRight(HttpServletRequest request, HttpServletResponse resp)
    {
        System.out.println("-----------------------------------come here wxpay--------------------------------");
        synchronized (wxlock) {
            System.out.println("-----------------------------------come here wxpay 2--------------------------------");

            BufferedReader reader = null ;

            try {
                reader = request.getReader() ;
                String line = "" ;
                String xmlString = null ;
                StringBuffer inputString = new StringBuffer() ;

                while( (line = reader.readLine()) != null ){
                    inputString.append(line) ;
                }

                xmlString = inputString.toString() ;
                request.getReader().close();
                System.out.println("xmlString:" + xmlString);
                Map<String, String> map = CommonUtils.xmlToMap(xmlString);

                System.out.println("result sign:" + map.get("sign"));
                SortedMap<String, String> parameters = new TreeMap<String, String>();
                parameters.put("appid", map.get("appid"));
                parameters.put("bank_type", map.get("bank_type"));
                parameters.put("cash_fee", map.get("cash_fee"));
                parameters.put("fee_type", map.get("fee_type"));
                parameters.put("is_subscribe", map.get("is_subscribe"));
                parameters.put("mch_id", map.get("mch_id"));
                parameters.put("nonce_str", map.get("nonce_str"));
                parameters.put("openid", map.get("openid"));
                parameters.put("out_trade_no", map.get("out_trade_no"));
                parameters.put("result_code", map.get("result_code"));
                parameters.put("return_code", map.get("return_code"));
                parameters.put("time_end", map.get("time_end"));
                parameters.put("total_fee", map.get("total_fee"));
                parameters.put("trade_type", map.get("trade_type"));
                parameters.put("transaction_id", map.get("transaction_id"));

                String resultSign = CommonUtils.createSign("UTF-8", parameters, PayService.WX_KEY);
                System.out.println("create sign:" + resultSign);
                if(map.get("sign").equals(resultSign))
                {
                    if(payService.verifyWxPay(map).equals("success"))
                    {
                        SortedMap<String, String> result = new TreeMap<String, String>();
                        result.put("return_code", "SUCCESS");
                        return mapToXml(result);
                    }
                    SortedMap<String, String> result = new TreeMap<String, String>();
                    result.put("return_code", "FAIL");
                    return mapToXml(result);
                }
                else
                {
                    SortedMap<String, String> result = new TreeMap<String, String>();
                    result.put("return_code", "FAIL");
                    return mapToXml(result);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SortedMap<String, String> result = new TreeMap<String, String>();
        result.put("return_code", "FAIL");
        return mapToXml(result);
    }

    private String mapToXml(SortedMap<String, String> parameters)
    {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<xml>");
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        CommonUtils.createXml(it, xmlBuilder);
        xmlBuilder.append("</xml>");
        return xmlBuilder.toString();
    }

}
