package com.ywl5320.appservice.dao;

import com.ywl5320.appservice.bean.UpdateBean;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by hlwky001 on 2017/10/27.
 */
public class UpdateDaoImpl extends HibernateDaoSupport implements UpdateDao {

    public UpdateBean getUpdateInfo(String md5value, int versioncode, String channelid) {
        List<UpdateBean> updates = (List<UpdateBean>) this.getHibernateTemplate().find("from UpdateBean where md5value=? and versioncode=? and channelid=?", md5value, versioncode, channelid);
        if(updates != null && updates.size() > 0)
        {
            return updates.get(0);
        }
        return null;
    }

    public void saveUpdateInfo(UpdateBean updateBean) {
        this.getHibernateTemplate().save(updateBean);
    }

    public void deleteUpdateInfo(UpdateBean updateBean) {
        this.getHibernateTemplate().delete(updateBean);
    }
}
