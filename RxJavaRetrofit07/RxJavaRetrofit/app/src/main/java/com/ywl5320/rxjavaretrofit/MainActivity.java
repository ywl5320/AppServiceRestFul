package com.ywl5320.rxjavaretrofit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.ywl5320.rxjavaretrofit.beans.UpdateBean;
import com.ywl5320.rxjavaretrofit.dialog.LoadDialog;
import com.ywl5320.rxjavaretrofit.beans.AliWxPayBean;
import com.ywl5320.rxjavaretrofit.beans.PayResult;
import com.ywl5320.rxjavaretrofit.beans.UserBean;
import com.ywl5320.rxjavaretrofit.dialog.NormalDialog;
import com.ywl5320.rxjavaretrofit.dialog.UpdateDialog;
import com.ywl5320.rxjavaretrofit.httpservice.service.HttpMethod;
import com.ywl5320.rxjavaretrofit.httpservice.serviceapi.PayApi;
import com.ywl5320.rxjavaretrofit.httpservice.serviceapi.UserApi;
import com.ywl5320.rxjavaretrofit.httpservice.subscribers.HttpSubscriber;
import com.ywl5320.rxjavaretrofit.httpservice.subscribers.SubscriberOnListener;
import com.ywl5320.rxjavaretrofit.utils.CommonUtils;
import com.ywl5320.rxjavaretrofit.wxapi.WxFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private Button btnUserInfo;
    private Button btnAliPay;
    private Button btnWxPay;
    private TextView tvVersionName;
    public LoadDialog loadDialog;

    private PackageManager packageManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = (Button) findViewById(R.id.btn_rgtister);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnUserInfo = (Button) findViewById(R.id.btn_userinfo);
        btnAliPay = (Button) findViewById(R.id.btn_alipay);
        btnWxPay = (Button) findViewById(R.id.btn_wxpay);
        tvVersionName = (TextView) findViewById(R.id.tv_versionname);

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
                PayApi.getInstance().createOrder(MyApplication.getInstance().getUserBean().getId(), MyApplication.getInstance().getPhone(), "支付宝支付测试", "0.01", 1, "新本版支付宝SDK支付", new HttpSubscriber<AliWxPayBean>(new SubscriberOnListener<AliWxPayBean>() {
                    @Override
                    public void onSucceed(final AliWxPayBean data) {
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

        btnWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.getInstance().getUserBean() == null)
                {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                    return;
                }
                showLoadDialog("创建订单");
                PayApi.getInstance().createOrder(MyApplication.getInstance().getUserBean().getId(), MyApplication.getInstance().getPhone(), "微信支付测试", "1", 2, "APP微信SDK支付", new HttpSubscriber<AliWxPayBean>(new SubscriberOnListener<AliWxPayBean>() {
                    @Override
                    public void onSucceed(final AliWxPayBean data) {
                        hideLoadDialog();
                        System.out.println("微信预支付订单信息：" + data.getPrepayid());
                        System.out.println("sign：" + data.getSign());
                        PayReq request = new PayReq();
                        request.appId = data.getAppid();
                        request.partnerId = data.getPartnerid();
                        request.prepayId = data.getPrepayid();
                        request.packageValue = data.getPackageValue();
                        request.nonceStr = data.getNoncestr();
                        request.timeStamp = data.getTimestamp();
                        request.sign = data.getSign();
                        WxFactory.getInstance().getWxApi(MainActivity.this).sendReq(request);

                    }

                    @Override
                    public void onError(int code, String msg) {
                        hideLoadDialog();
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }, MainActivity.this));
            }
        });

        tvVersionName.setText("当前版本：" + CommonUtils.getVersionName(this));

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

    public void patch(View view) {

        showLoadDialog("检查更新");
        String path = CommonUtils.getAppSourceDirPath(this);
        Log.d("ywl5320", path);
        File file = new File(path);
        String md5 = CommonUtils.getFileMd5(file);
        Log.d("ywl5320", md5);
        UserApi.getInstance().chedkUpdate(md5, CommonUtils.getVersionCode(this), "xiaomi", new HttpSubscriber<UpdateBean>(new SubscriberOnListener<UpdateBean>() {
            @Override
            public void onSucceed(final UpdateBean data) {
                hideLoadDialog();

                NormalDialog normalDialog = new NormalDialog(MainActivity.this);
                normalDialog.show();
                String updatestr = "有新版本：" + data.getNewversionName() + "\n完整大小：" + CommonUtils.getFormatSize(data.getFilesize()) + "\n增量大小：" + CommonUtils.getFormatSize(data.getPatchsize());
                SpannableStringBuilder spannable = new SpannableStringBuilder(updatestr);
                spannable.setSpan(new StrikethroughSpan(), data.getNewversionName().length() + 5 + 6, data.getNewversionName().length() + 5 + 6 + CommonUtils.getFormatSize(data.getFilesize()).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_c7560a)), 5, data.getNewversionName().length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_c7560a)), data.getNewversionName().length() + 5 + 6, data.getNewversionName().length() + 5 + 6 + CommonUtils.getFormatSize(data.getFilesize()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_c7560a)), spannable.length() - 5, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                normalDialog.setData("更新提醒", spannable, "暂不更新", "立即更新", new NormalDialog.OnNoOrYesListener() {
                    @Override
                    public void onNn() {

                    }

                    @Override
                    public void onYes() {
                        try {
                            final String baseDir = "/ywl5320/update/";

                            /**
                             * 创建新版本apk存储路径
                             */
                            String newapkpath = CommonUtils.makeDir(baseDir + "apk");
                            File newApkFile = new File(newapkpath + "/" + CommonUtils.getPackageName(MainActivity.this) + "_" + data.getNewversionName() + ".apk");
                            if(newApkFile.exists())
                            {
                                if(newApkFile.length() == data.getFilesize())
                                {
                                    //安装
                                    Toast.makeText(MainActivity.this, "安装中", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setDataAndType(Uri.parse("file://" + newApkFile.getAbsolutePath()),
                                            "application/vnd.android.package-archive");
                                    MainActivity.this.startActivity(i);
                                    return;
                                }
                            }
                            else
                            {
                                newApkFile.createNewFile();
                            }

                            /**
                             * 创建patch存储路径
                             */
                            String newpatchpath = CommonUtils.makeDir(baseDir + "patch");
                            File newPatchFile = new File(newpatchpath + "/" + CommonUtils.getPackageName(MainActivity.this) + "_patch_" + data.getNewversioncode() + "_" + data.getVersioncode() + ".patch");
                            if(!newPatchFile.exists())
                            {
                                newPatchFile.createNewFile();
                            }

                        String downloadUrl = HttpMethod.DOWNLOAD_URL + "apk/update/patch/" + data.getPatchdownloadpath();
                        UpdateDialog updateDialog = new UpdateDialog(MainActivity.this);
                        updateDialog.show();
                        updateDialog.setInfo(downloadUrl, newPatchFile.getAbsolutePath(), newApkFile.getAbsolutePath(), data.getPatchsize(), data.getFilesize());

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, this));

    }

}
