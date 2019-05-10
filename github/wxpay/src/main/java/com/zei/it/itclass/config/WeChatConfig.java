package com.zei.it.itclass.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 微信配置类
 */
@Configuration
@PropertySource(value = "classpath:application.properties")
public class WeChatConfig {

    @Value("${wxpay.appid}")
    private String appId;

    @Value("${wxpay.password}")
    private String appPassword;

    @Value("${wxopen.appid}")
    private String openAppId;

    @Value("${wxopen.password}")
    private String openAppPassword;

    @Value("${wxopen.redirect_url}")
    private String openRedirectUrl;


    //微信开放二维码连接
    private final static String OPEN_QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect?" +
                                                    "appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";


    //通过code获取access_token url
    private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                                                    "appid=%s&secret=%s&code=%s&grant_type=authorization_code";


    //通过access_token得到微信用户信息
    private final static String USER_MSG_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%slang=zh_CN";

    //下单链接
//    private final static String UNIFILED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private final static String UNIFILED_ORDER_URL = "http://api.xdclass.net/pay/unifiedorder";

    //获取支付配置信息
    @Value("${wxpay.mch_id}")
    private String mchId;

    @Value("${wxpay.key}")
    private String key;

    @Value("${wxpay.callback}")
    private String payCallbackUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getOpenAppId() {
        return openAppId;
    }

    public void setOpenAppId(String openAppId) {
        this.openAppId = openAppId;
    }

    public String getOpenAppPassword() {
        return openAppPassword;
    }

    public void setOpenAppPassword(String openAppPassword) {
        this.openAppPassword = openAppPassword;
    }

    public String getOpenRedirectUrl() {
        return openRedirectUrl;
    }

    public void setOpenRedirectUrl(String openRedirectUrl) {
        this.openRedirectUrl = openRedirectUrl;
    }

    public static String getOpenQrcodeUrl() {
        return OPEN_QRCODE_URL;
    }

    public static String getAccessTokenUrl() {
        return ACCESS_TOKEN_URL;
    }

    public static String getUserMsgUrl() {
        return USER_MSG_URL;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayCallbackUrl() {
        return payCallbackUrl;
    }

    public void setPayCallbackUrl(String payCallbackUrl) {
        this.payCallbackUrl = payCallbackUrl;
    }

    public static String getUnifiledOrderUrl() {
        return UNIFILED_ORDER_URL;
    }
}
