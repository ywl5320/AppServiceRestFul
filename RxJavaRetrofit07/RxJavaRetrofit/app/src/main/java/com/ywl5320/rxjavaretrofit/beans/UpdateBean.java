package com.ywl5320.rxjavaretrofit.beans;

/**
 * Created by ywl5320 on 2017/10/26.
 */
public class UpdateBean extends BaseBean{

    private Integer id;
    /**
     * old apk md5 值
     */
    private String md5value;

    /**
     * 旧版本号（code）
     */
    private Integer versioncode;

    /**
     * 旧版本号（name）
     */
    private String versionName;

    /**
     * 新版本号（code）
     */
    private Integer newversioncode;

    /**
     * 新版本号（name）
     */
    private String newversionName;

    /**
     * 文件总大小
     */
    private Long filesize;

    /**
     * patch包大小
     */
    private Long patchsize;

    /**
     * 下载地址
     */
    private String downloadpath;

    /**
     * 差分包下载地址
     */
    private String patchdownloadpath;

    /**
     * 渠道标识
     */
    private String channelid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMd5value() {
        return md5value;
    }

    public void setMd5value(String md5value) {
        this.md5value = md5value;
    }

    public Integer getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(Integer versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public Long getPatchsize() {
        return patchsize;
    }

    public void setPatchsize(Long patchsize) {
        this.patchsize = patchsize;
    }

    public String getDownloadpath() {
        return downloadpath;
    }

    public void setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
    }

    public String getPatchdownloadpath() {
        return patchdownloadpath;
    }

    public void setPatchdownloadpath(String patchdownloadpath) {
        this.patchdownloadpath = patchdownloadpath;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public Integer getNewversioncode() {
        return newversioncode;
    }

    public void setNewversioncode(Integer newversioncode) {
        this.newversioncode = newversioncode;
    }

    public String getNewversionName() {
        return newversionName;
    }

    public void setNewversionName(String newversionName) {
        this.newversionName = newversionName;
    }
}
