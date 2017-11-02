package com.ywl5320.rxjavaretrofit.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.ywl5320.bspatchywl5320.BsPatchYwl5320Util;
import com.ywl5320.rxjavaretrofit.R;
import com.ywl5320.rxjavaretrofit.utils.CommonUtils;
import com.ywl5320.rxjavaretrofit.utils.DownloadUtil;

import java.io.File;
import java.io.IOException;


/**
 * Created by ywl5320 on 2017/10/18.
 */

public class UpdateDialog extends BaseDialog{

    ProgressBar progressBar;
    TextView tvPercent;
    TextView tvDownSize;
    Button btnStatus;
    TextView tvTitle;
    ImageView ivClose;


    long patchsize = 0;
    long filesize = 0;
    private String url = "";
    private String patchPath = "";
    private String newapkPath = "";
    private int isFinish = 0;

    public UpdateDialog(Context context) {
        super(context, R.style.StyleDialogGray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        tvPercent = (TextView) findViewById(R.id.tv_p_progress);
        tvDownSize = (TextView) findViewById(R.id.tv_downsize);
        btnStatus = (Button) findViewById(R.id.btn_status);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivClose = (ImageView) findViewById(R.id.iv_close);

        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadUtil.getInstance().canDownLoad();
                dismiss();
            }
        });

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 失败
                 */
                if(isFinish == -1)
                {
                    setInfo(url, patchPath, newapkPath, patchsize, filesize);
                }
                /**
                 * 下载中
                 */
                else if(isFinish == 0)
                {
                    DownloadUtil.getInstance().canDownLoad();
                    dismiss();
                }
                /**
                 * 完成
                 */
                else if(isFinish == 1)
                {
                    File file = new File(newapkPath);
                    if(file.exists())
                    {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + newapkPath),
                                "application/vnd.android.package-archive");
                        context.startActivity(i);
                        dismiss();
                    }
                    else
                    {
                        Log.d("ywl5320", "apk不存在-->" + newapkPath);
                        isFinish = -1;
                        tvTitle.setText("安装包不存在");
                        btnStatus.setText("下载");
                        Toast.makeText(context, "安装包不存在，请重新下载", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    public void setInfo(String url, final String patchPath, final String newapkPath, long patchsize, long filesize)
    {
        this.url = url;
        this.patchPath = patchPath;
        this.newapkPath = newapkPath;
        this.patchsize = patchsize;
        this.filesize = filesize;
        Log.d("ywl5320", "url:" + url);
        Log.d("ywl5320", "patchPath:" + patchPath);
        Log.d("ywl5320", "newapkPath:" + newapkPath);
        Log.d("ywl5320", "patchsize:" + patchsize);
        Log.d("ywl5320", "filesize:" + filesize);

        progressBar.setMax(100);
        progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.color_progressbar));
        progressBar.setProgress(0);
        tvDownSize.setText("0M/" + CommonUtils.getFormatSize(patchsize));
        btnStatus.setText("取消");
        tvTitle.setText("下载新版本");
        tvPercent.setText("0%");
        DownloadUtil.getInstance().download(url, patchPath, patchsize, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                if(context != null) {

                    File file = new File(newapkPath);
                    if(!file.exists())
                    {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    int restus = BsPatchYwl5320Util.getInstance().bsPatch(CommonUtils.getAppSourceDirPath(context), newapkPath, patchPath);
                    if(restus == 0) {
                        isFinish = 1;
                        Message message = Message.obtain();
                        message.obj = 100;
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    else
                    {
                        isFinish = -1;
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }


                }
            }

            @Override
            public void onDownloading(int progress) {
                Log.d("ywl5320", "progress:" + progress);
                if(context != null) {
                    if (progress < 100) {
                        isFinish = 0;
                    } else {
                        isFinish = 1;
                    }
                    Message message = Message.obtain();
                    message.obj = progress;
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onDownloadFailed() {
                if(context != null) {
                    isFinish = -1;
                    Message message = Message.obtain();
                    message.what = -1;
                    handler.sendMessage(message);
                }
            }
        });
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == -1)
            {
                btnStatus.setText("重试");
                tvTitle.setText("下载失败");
            }
            else if(msg.what == 0) {
                int progress = (int) msg.obj;
                tvPercent.setText(progress + "%");
                progressBar.setProgress(progress);
                tvDownSize.setText(CommonUtils.getFormatSize(patchsize * progress / 100) + "/" + CommonUtils.getFormatSize(patchsize));
            }
            else if(msg.what == 1)
            {
                btnStatus.setText("安装");
            }
        }
    };
}
