package com.zei.it.itclass.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 通用工具类
 */
public class CommonUtils {

    /**
     * 生成32位uuid
     * @return
     */
    public static String createUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        return uuid;
    }


    /**
     * md5加密
     * @param data
     * @return
     */
    public static String MD5(String data){

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(data.getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest){
                stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
            }
            return stringBuilder.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
