package com.ywl5320.rxjavaretrofit.httpservice.subscribers;

/**
 * Created by ywl on 2016/6/21.
 */
public class ResponStatusCode {

    /**
     * 判断code 是否成功
     * @param code
     * @return
     */
    public static String getCode(int code, String msg){
        if(code == 1){
            return "1";
        }
        else if(code == -1){
            return "参数不合法";
        }else{
            return msg;
        }
    }

}
