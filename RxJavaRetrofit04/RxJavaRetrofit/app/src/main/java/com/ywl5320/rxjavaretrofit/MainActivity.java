package com.ywl5320.rxjavaretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ywl5320.rxjavaretrofit.dialog.LoadDialog;
import com.ywl5320.rxjavaretrofit.httpservice.beans.UserBean;
import com.ywl5320.rxjavaretrofit.httpservice.beans.WeatherBean;
import com.ywl5320.rxjavaretrofit.httpservice.serviceapi.UserApi;
import com.ywl5320.rxjavaretrofit.httpservice.subscribers.HttpSubscriber;
import com.ywl5320.rxjavaretrofit.httpservice.subscribers.SubscriberOnListener;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private Button btnUserInfo;
    public LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = (Button) findViewById(R.id.btn_rgtister);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnUserInfo = (Button) findViewById(R.id.btn_userinfo);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoadDialog("注册中");

                UserApi.getInstance().register("18780100502", "123456", "ywl5320", 1, 18, new HttpSubscriber<UserBean>(new SubscriberOnListener<UserBean>() {
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

                UserApi.getInstance().login("18780100502", "123456", new HttpSubscriber<UserBean>(new SubscriberOnListener<UserBean>() {
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
                UserApi.getInstance().userinfo("18780100502", new HttpSubscriber<UserBean>(new SubscriberOnListener() {
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

    }

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
        if(loadDialog != null)
            loadDialog.dismiss();
    }
}
