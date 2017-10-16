package com.ywl5320.appservice.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.TokenBean;
import com.ywl5320.appservice.dao.TokenDao;
import com.ywl5320.appservice.util.RestFulUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ywl5320 on 2017/10/16.
 */
public class RestFulInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenDao tokenDao;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		
		String uri = request.getRequestURI();
		Map<String, String> headerMaps = new HashMap<String, String>();
	    Enumeration headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        headerMaps.put(key, value);
	    }
	    if(!uri.endsWith(".do"))
	    {
	    	return true;
	    }
	    if(uri.endsWith("user/loginbypwd.do"))//登录
		{
			return true;
			
		}
		else if(uri.endsWith("user/register.do"))//注册
		{
			return true;
		}
		else if(uri.endsWith("pay/verifyalipayresult.do"))//支付验证(暂留)
		{
			return true;
		}
		else
		{
			TokenBean tokenBean = tokenDao.isTokenAvailable(headerMaps.get("phone"));
			if(tokenBean != null && tokenBean.getToken().equals(headerMaps.get("token")))
			{
				return true;
			}
			else
			{
				RestFulBean<TokenBean> resuFulBean = RestFulUtil.getInstance().getResuFulBean(null, 1, "用户身份验证失败");
				response.setCharacterEncoding("UTF-8");
				Writer writer = response.getWriter();
				writer.write(JSONObject.toJSONString(resuFulBean, SerializerFeature.WriteMapNullValue));
				return false;
			}
		}
	}
	
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}
	
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}
	
}
