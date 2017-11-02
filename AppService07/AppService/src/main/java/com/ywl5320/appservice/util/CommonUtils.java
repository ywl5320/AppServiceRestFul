package com.ywl5320.appservice.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ywl5320 on 2017/10/19.
 */
public class CommonUtils {

    public static String getNowTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(System.currentTimeMillis());
        return dateString;
    }

    /**
     * 获得随机字符串
     * @return
     */
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 微信支付签名算法sign
     * @param characterEncoding 签名编码（UTF-8)
     * @param parameters 要签名的参数的集合
     * @param key 商户自己设置的key
     */
    public static String createSign(String characterEncoding, SortedMap<String,String> parameters, String key){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        System.out.println(sb.toString());
        String sign = WxMd5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        System.out.println(sign);
        return sign;
    }

    public static Map<String, String> xmlToMap(String xmlstr) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            SAXReader reader = new SAXReader();
            InputStream ins = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();

            List<Element> list = root.elements();

            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取时间戳
     * @return
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static void createXml(Iterator it, StringBuilder xmlBuilder)
    {
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            xmlBuilder.append("<").append(k).append(">");
            xmlBuilder.append(v);
            xmlBuilder.append("</").append(k).append(">");
        }
    }

    public static String getFileMd5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        int length = 1024 * 2;
        byte[] buffer = new byte[length];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, length)) > 0) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());

        return bigInt.toString(16);
    }

    public static String getOsName()
    {
        return System.getProperty("os.name").toLowerCase();
    }

}
