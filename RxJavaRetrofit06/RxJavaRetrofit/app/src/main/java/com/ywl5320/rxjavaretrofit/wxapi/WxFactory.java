package com.ywl5320.rxjavaretrofit.wxapi;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by ywl5320 on 2017/9/6.
 */

public class WxFactory {

    private IWXAPI api;
    private String WXAPPID = "";

    private static WxFactory instance = new WxFactory();

    private WxFactory(){
    }

    public static WxFactory getInstance()
    {
        return instance;
    }

    public IWXAPI getWxApi(Context context)
    {
        if(api == null) {
            api = WXAPIFactory.createWXAPI(context, WXAPPID, false);
            api.registerApp(WXAPPID);
        }
        return api;
    }

}
