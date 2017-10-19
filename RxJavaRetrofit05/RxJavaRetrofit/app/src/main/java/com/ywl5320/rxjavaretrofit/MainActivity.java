package com.ywl5320.rxjavaretrofit;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ywl5320.rxjavaretrofit.dialog.LoadDialog;
import com.ywl5320.rxjavaretrofit.beans.AliPayBean;
import com.ywl5320.rxjavaretrofit.beans.PayResult;
import com.ywl5320.rxjavaretrofit.beans.UserBean;
import com.ywl5320.rxjavaretrofit.httpservice.serviceapi.PayApi;
import com.ywl5320.rxjavaretrofit.httpservice.serviceapi.UserApi;
import com.ywl5320.rxjavaretrofit.httpservice.subscribers.HttpSubscriber;
import com.ywl5320.rxjavaretrofit.httpservice.subscribers.SubscriberOnListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private Button btnUserInfo;
    private Button btnAliPay;
    public LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = (Button) findViewById(R.id.btn_rgtister);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnUserInfo = (Button) findViewById(R.id.btn_userinfo);
        btnAliPay = (Button) findViewById(R.id.btn_alipay);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoadDialog("注册中");

                UserApi.getInstance().register("187*****502", "123456", "ywl5320", 1, 18, new HttpSubscriber<UserBean>(new SubscriberOnListener<UserBean>() {
                    @Override
                    public void onSucceed(UserBean data) {
                        hideLoadDialog();//这个可以在回调里面添加到complete里面，关闭对话框只写到这个里面就可以了
                        Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        MyApplication.getInstance().setUserBean(data);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(MainActivity.this, "status:" + code + "," + msg, Toast.LENGTH_LONG).show();
                        hideLoadDialog();
                    }
                }, MainActivity.this));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadDialog("登录中");

                UserApi.getInstance().login("187*****502", "123456", new HttpSubscriber<UserBean>(new SubscriberOnListener<UserBean>() {
                    @Override
                    public void onSucceed(UserBean data) {
                        hideLoadDialog();//这个可以在回调里面添加到complete里面，关闭对话框只写到这个里面就可以了
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        MyApplication.getInstance().setUserBean(data);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(MainActivity.this, "status:" + code + "," + msg, Toast.LENGTH_LONG).show();
                        hideLoadDialog();
                    }
                }, MainActivity.this));
            }
        });

        btnUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadDialog("获取中");
                UserApi.getInstance().userinfo("187*****502", new HttpSubscriber<UserBean>(new SubscriberOnListener() {
                    @Override
                    public void onSucceed(Object data) {
                        hideLoadDialog();
                        Toast.makeText(MainActivity.this, "获取成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        hideLoadDialog();
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }, MainActivity.this));
            }
        });

        btnAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.getInstance().getUserBean() == null)
                {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                    return;
                }
                showLoadDialog("创建订单");
                PayApi.getInstance().createOrder(MyApplication.getInstance().getUserBean().getId(), "支付宝支付测试", "0.01", 1, "新本版支付宝SDK支付", new HttpSubscriber<AliPayBean>(new SubscriberOnListener<AliPayBean>() {
                    @Override
                    public void onSucceed(final AliPayBean data) {
                        hideLoadDialog();
                        System.out.println("支付宝支付信息：" + data.getOrderInfo());
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(MainActivity.this);
                                Map<String, String> result = alipay.payV2(data.getOrderInfo(),true);

                                Message msg = new Message();
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        hideLoadDialog();
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }, MainActivity.this));
            }
        });

    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void showLoadDialog(String msg)
    {
        if(loadDialog == null)
        {
            loadDialog = new LoadDialog(this);
        }
        loadDialog.show();
        loadDialog.setLoadMsg(msg);
    }

    public void hideLoadDialog()
    {
        if(loadDialog != null) {
            loadDialog.dismiss();
        }
    }


}
