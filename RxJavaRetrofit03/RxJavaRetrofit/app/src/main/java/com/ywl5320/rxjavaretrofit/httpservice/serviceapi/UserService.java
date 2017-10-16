package com.ywl5320.rxjavaretrofit.httpservice.serviceapi;


import com.ywl5320.rxjavaretrofit.httpservice.beans.UserBean;
import com.ywl5320.rxjavaretrofit.httpservice.beans.WeatherBean;
import com.ywl5320.rxjavaretrofit.httpservice.httpentity.HttpResult;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by ywl on 2016/5/19.
 */
public interface UserService {

    /**
     * 注册模拟
     * @param userBean
     * @return
     */
    @PUT("user/register.do")
    Observable<HttpResult<UserBean>> register(@Body UserBean userBean);

    @POST("user//loginbypwd.do")
    Observable<HttpResult<UserBean>> login(@Query("phone") String phone, @Query("password") String password);
}
