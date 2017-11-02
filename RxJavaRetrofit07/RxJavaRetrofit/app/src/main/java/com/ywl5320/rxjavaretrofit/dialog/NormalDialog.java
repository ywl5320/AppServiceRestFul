package com.ywl5320.rxjavaretrofit.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.ywl5320.rxjavaretrofit.R;


/**
 * Created by ywl5320 on 2017/9/21.
 */

public class NormalDialog extends BaseDialog{


    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvNo;
    private TextView tvYes;

    public NormalDialog(Context context) {
        super(context, R.style.StyleDialogGray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal_layout);
        tvTitle = (TextView) findViewById(R.id.title);
        tvMsg = (TextView) findViewById(R.id.message);
        tvNo = (TextView) findViewById(R.id.no);
        tvYes = (TextView) findViewById(R.id.yes);
    }

    public void setData(String title, String msg, String no, String yes, final OnNoOrYesListener onNoOrYesListener)
    {
        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvNo.setText(no);
        tvYes.setText(yes);
        if(onNoOrYesListener != null)
        {
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNoOrYesListener.onNn();
                    dismiss();
                }
            });

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNoOrYesListener.onYes();
                    dismiss();
                }
            });
        }
    }

    public void setData(String title, SpannableStringBuilder msg, String no, String yes, final OnNoOrYesListener onNoOrYesListener)
    {
        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvNo.setText(no);
        tvYes.setText(yes);
        if(onNoOrYesListener != null)
        {
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNoOrYesListener.onNn();
                    dismiss();
                }
            });

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNoOrYesListener.onYes();
                    dismiss();
                }
            });
        }
    }

    public interface OnNoOrYesListener
    {
        void onNn();

        void onYes();
    }
}
