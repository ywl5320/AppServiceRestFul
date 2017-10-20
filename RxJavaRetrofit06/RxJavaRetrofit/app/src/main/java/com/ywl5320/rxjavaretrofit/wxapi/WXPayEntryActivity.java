package com.ywl5320.rxjavaretrofit.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		api = WXAPIFactory.createWXAPI(this, "", true);
//		api.registerApp("");
		api = WxFactory.getInstance().getWxApi(this);
		api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0)
			{
				Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
			}
			else if(resp.errCode == -1)
			{
				Toast.makeText(this, "支付出错：" + resp.errStr, Toast.LENGTH_LONG).show();
			}
			else if(resp.errCode == -2)
			{
				Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
			}
		}
		finish();
	}
}