package com.zei.it.itclass.service.impl;

import com.zei.it.itclass.config.WeChatConfig;
import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.mapper.UserMapper;
import com.zei.it.itclass.service.UserService;
import com.zei.it.itclass.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    WeChatConfig weChatConfig;

    @Autowired
    UserMapper userMapper;


    @Override
    public User save(String code) {

        //获取weChat token
        String access_token_url = String.format(WeChatConfig.getAccessTokenUrl(),weChatConfig.getOpenAppId(),weChatConfig.getOpenAppPassword(),code);
        Map<String,Object> tokenMap = HttpUtils.doGet(access_token_url);
        if(tokenMap == null || tokenMap.isEmpty()){
            return null;
        }
        String token = (String) tokenMap.get("access_token");
        String openId = (String) tokenMap.get("openid");

        //查看本地是否已存在用户
        User existUser = userMapper.getUserByOpenId(openId);
        if(existUser != null){
            return existUser;
        }


        //获取微信用户信息
        String userMsgUrl = String.format(WeChatConfig.getUserMsgUrl(),token,openId);
        Map<String,Object> userMap = HttpUtils.doGet(userMsgUrl);
        if(userMap == null || userMap.isEmpty()){
            return null;
        }
        String nickname = (String) userMap.get("nickname");
        try {
            //转码
            nickname = new String(nickname.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Double sex = (Double) userMap.get("sex");
        String province = (String) userMap.get("province");
        String city = (String) userMap.get("city");
        String country = (String) userMap.get("country");
        String headimgurl = (String) userMap.get("headimgurl");

        User user = new User().setName(nickname)
                                .setOpenid(openId)
                                .setSex(sex)
                                .setCity(province + " " +city)
                                .setHeadImg(headimgurl)
                                .setCreateTime(new Date())
                                .setState(1);


        userMapper.save(user);

        return user;
    }
}
