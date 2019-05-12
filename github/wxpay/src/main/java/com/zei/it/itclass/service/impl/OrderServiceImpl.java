package com.zei.it.itclass.service.impl;

import com.zei.it.itclass.config.WeChatConfig;
import com.zei.it.itclass.domain.Article;
import com.zei.it.itclass.domain.Order;
import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.mapper.ArticleMapper;
import com.zei.it.itclass.mapper.OrderMapper;
import com.zei.it.itclass.mapper.UserMapper;
import com.zei.it.itclass.service.OrderService;
import com.zei.it.itclass.util.CommonUtils;
import com.zei.it.itclass.util.HttpUtils;
import com.zei.it.itclass.util.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    WeChatConfig weChatConfig;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String save(Order order) {
        //验证文章是否存在
        Article article = articleMapper.getOne(order.getArticleId());
        if(article == null){
            return null;
        }
        //验证用户是否存在
        User user = userMapper.getOne(order.getUserId());
        if(user == null){
            return null;
        }

        //生成订单
        order.setNickName(user.getName());
        order.setHeadImg(user.getHeadImg());
        order.setOrderNo(CommonUtils.createUUID());
        order.setIsPay(0);
        order.setPay(article.getPrice());
        order.setArticleTitle(article.getTitle());
        order.setArticleImg(article.getCoverImg());
        order.setCreateTime(new Date());
        order.setState(1);
        orderMapper.save(order);

        //下单并获取codeURL
        String codeUrl = unifiedOrder(order);

        return codeUrl;
    }

    /**
     * 生成签名、统一下单
     * @param order
     * @return
     */
    private String unifiedOrder(Order order){
        //生成签名
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid",weChatConfig.getAppId());
        params.put("mch_id",weChatConfig.getMchId());
        params.put("nonce_str",CommonUtils.createUUID());
        params.put("body",order.getArticleTitle());
        params.put("out_trade_no",order.getOrderNo());
        params.put("total_fee",order.getPay().toString());
        params.put("spbill_create_ip",order.getIp());
        params.put("notify_url",weChatConfig.getPayCallbackUrl());
        params.put("trade_type","NATIVE");
        //签名sign
        String sign = WxPayUtils.createSign(params,weChatConfig.getKey());
        params.put("sign",sign);

        //转参为xml
        try {
            String payXml = WxPayUtils.mapToXml(params);
            String returnXml = HttpUtils.doPost(WeChatConfig.getUnifiledOrderUrl(),payXml,5000);
            if(!StringUtils.isEmpty(returnXml)){
                Map<String, String> unifiedOrderMap = WxPayUtils.xmlToMap(returnXml);
                String codeUrl = unifiedOrderMap.get("code_url");
                if(!StringUtils.isEmpty(codeUrl)){
                    return codeUrl;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    @Override
    public Order getOne(int id) {
        return orderMapper.getOne(id);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        return orderMapper.getByOrderNo(orderNo);
    }

    @Override
    public Integer updateOrderByOrderNo(Order order) {
        return orderMapper.updateOrderByOrderNo(order);
    }
}
