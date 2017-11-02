package com.ywl5320.rxjavaretrofit.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ywl5320 on 2017/9/25.
 */

public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();

    }

    /**
     * @param url 下载连接
     * @param saveapkpath 下载文件的目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveapkpath, final long fileSize, final OnDownloadListener listener) {
        DownLoadCallBack downLoadCallBack = new DownLoadCallBack();
        downLoadCallBack.setdownloadinfo(saveapkpath, fileSize, listener);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(downLoadCallBack);
    }

    private class DownLoadCallBack implements Callback
    {
        private OnDownloadListener listener;
        private String saveapkpath;
        private long fileSize;
        public DownLoadCallBack()
        {

        }

        public void setdownloadinfo(final String saveDir, final long fileSize, final OnDownloadListener listener) {
            this.listener = listener;
            this.saveapkpath = saveDir;
            this.fileSize = fileSize;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            // 下载失败
            listener.onDownloadFailed();
            Log.d("ywl5320", e.toString());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            // 储存下载文件的目录
            try {
                if(response.code() == 200) {
                    is = response.body().byteStream();
                    Log.d("ywl5320", "length:" + response.body().contentLength());
                    File file = new File(saveapkpath);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / fileSize * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                }
                else
                {
                    listener.onDownloadFailed();
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onDownloadFailed();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                }
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    public void canDownLoad()
    {
        if(okHttpClient != null)
        {
            okHttpClient.dispatcher().cancelAll();
        }
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * 下载进度
         * @param progress
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
