package com.ywl5320.appservice.service;

import com.ywl5320.appservice.bean.RestFulBean;
import com.ywl5320.appservice.bean.UpdateBean;
import com.ywl5320.appservice.dao.UpdateDao;
import com.ywl5320.appservice.util.CommonUtils;
import com.ywl5320.appservice.util.RestFulUtil;
import com.ywl5320.bsdiff.BsDiffYwl5320Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * Created by ywl5320 on 2017/10/26.
 */
@Transactional
public class UpdateService {

    @Autowired
    private UpdateDao updateDao;

    static String oldApksPath = "E:/source/oldversion";
    static String newApkPath = "E:/source/newversion";
    static String patchPath = "E:/source/patch";

    static
    {
        if(CommonUtils.getOsName().contains("linux"))//linux操作系统
        {
            oldApksPath = "/usr/dev/sources/oldversion";
            newApkPath = "/usr/dev/sources/newversion";
            patchPath = "/usr/dev/sources/patch";
        }
        else if(CommonUtils.getOsName().contains("windows"))//window操作系统
        {
            oldApksPath = "E:/source/oldversion";
            newApkPath = "E:/source/newversion";
            patchPath = "E:/source/patch";
        }
    }

    public RestFulBean<UpdateBean> checkUpdate(String md5value, int versioncode, String channelid){

        UpdateBean updateBean = updateDao.getUpdateInfo(md5value, versioncode, channelid);
        if(updateBean != null)
        {
            File file = new File(patchPath + "/" + updateBean.getPatchdownloadpath());
            if(file.exists()) {
                return RestFulUtil.getInstance().getResuFulBean(updateBean, 0, "有新版本");
            }
            //todo 不存在可以再生产增量包或删除此条记录
            updateDao.deleteUpdateInfo(updateBean);
            return RestFulUtil.getInstance().getResuFulBean(null, 1, "没有新版本");
        }
        else
        {
            updateBean = createPatch(md5value, versioncode, channelid);
            if(updateBean != null)
            {
                updateDao.saveUpdateInfo(updateBean);
                UpdateBean updateBean1 = updateDao.getUpdateInfo(md5value, versioncode, channelid);
                if(updateBean1 != null) {
                    File file = new File(patchPath + "/" + updateBean1.getPatchdownloadpath());
                    if(file.exists()) {
                        return RestFulUtil.getInstance().getResuFulBean(updateBean1, 0, "有新版本");
                    }
                    updateDao.deleteUpdateInfo(updateBean1);
                    return RestFulUtil.getInstance().getResuFulBean(null, 1, "没有新版本");
                }
            }
            return RestFulUtil.getInstance().getResuFulBean(null, 1, "没有新版本");
        }
    }

    private UpdateBean createPatch(String md5value, int versioncode, String channelid)
    {

        File md5File = null;
        File newFile = null;
        String patchName = "";
        File patchFile = null;
        String versionname = "";

        UpdateBean updateBean = null;
        int newVersionCode = 1;
        String newVersionName = "";

        File file = new File(oldApksPath);
        if(!file.exists())
            return null;
        File[] files = file.listFiles();
        if(files == null || files.length == 0)
            return null;

        /**
         * 根据MD5值找到和客户端相同的版本
         */
        for(File f : files)
        {
            String fmd5 = CommonUtils.getFileMd5(f);
            if(fmd5.equals(md5value))
            {
                System.out.print(f.getName() + " md5: " + fmd5);
                String[] flag = f.getName().replace(".apk", "").split("_");
                if(flag != null && flag.length == 4 && flag[3].equals(channelid))
                {
                    versionname = flag[2];
                    md5File = f;
                    break;
                }
            }
        }
        if(md5File == null)
            return null;

        /**
         * 根据渠道获取当前最新版本
         */
        File nfile = new File(newApkPath);
        if(!nfile.exists())
            return null;
        File[] nfiles = nfile.listFiles();
        if(nfiles == null || nfiles.length == 0)
            return null;

        for(File nf : nfiles)
        {
            String[] flag = nf.getName().replace(".apk", "").split("_");
            if(flag != null && flag.length == 4 && flag[3].equals(channelid))
            {
                System.out.println("渠道：" + channelid + " 的当前最新版本" + nf.getName());
                newFile = nf;
                newVersionCode = Integer.parseInt(flag[1]);
                newVersionName = flag[2];
                patchName = patchPath + "/" + nf.getName().replace(".apk", "") + "_patch_" + versioncode + ".patch";
                break;
            }
        }
        if(newfile == null)
            return null;

        System.out.println("oldfile:" + md5File.getAbsolutePath());
        System.out.println("newfile:" + newFile.getAbsolutePath());
        System.out.println("patchfile:" + patchName);

        int result = BsDiffYwl5320Util.getInstance().bsDiffFile(md5File.getAbsolutePath(), newFile.getAbsolutePath(), patchName);
        if(result != 0)
            return null;

        patchFile = new File(patchName);
        if(!patchFile.exists())
            return null;

        updateBean = new UpdateBean();
        updateBean.setMd5value(md5value);
        updateBean.setVersioncode(versioncode);
        updateBean.setVersionName(versionname);
        updateBean.setNewversioncode(newVersionCode);
        updateBean.setNewversionName(newVersionName);
        updateBean.setFilesize(md5File.length());
        updateBean.setPatchsize(patchFile.length());
        updateBean.setDownloadpath(newFile.getName());
        updateBean.setPatchdownloadpath(patchFile.getName());
        updateBean.setChannelid(channelid);

        return updateBean;
    }

}
