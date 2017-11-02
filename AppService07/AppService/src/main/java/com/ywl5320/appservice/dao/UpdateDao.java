package com.ywl5320.appservice.dao;

import com.ywl5320.appservice.bean.UpdateBean;

/**
 * Created by ywl5320 on 2017/10/27.
 */
public interface UpdateDao {

    /**
     * 获取更新信息
     * @param md5value
     * @param versioncode
     * @return
     */
    UpdateBean getUpdateInfo(String md5value, int versioncode, String channelid);

    /**
     * 存储更新信息
     * @param updateBean
     */
    void saveUpdateInfo(UpdateBean updateBean);

    /**
     * 删除更新信息
     * @param updateBean
     */
    void deleteUpdateInfo(UpdateBean updateBean);

}
