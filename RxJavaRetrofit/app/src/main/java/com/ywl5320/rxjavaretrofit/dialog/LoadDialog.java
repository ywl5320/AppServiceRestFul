package com.ywl5320.rxjavaretrofit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ywl5320.rxjavaretrofit.R;

/**
 * Created by ywl on 2016/3/9.
 */
public class LoadDialog extends Dialog{

    public Context context;
    private TextView mtvLoadmsg;

    public LoadDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        mtvLoadmsg = (TextView) findViewById(R.id.tv_load_msg);
    }

    public void setLoadMsg(String msg)
    {
        if(mtvLoadmsg != null)
        {
            mtvLoadmsg.setText(msg);
        }
    }
}
