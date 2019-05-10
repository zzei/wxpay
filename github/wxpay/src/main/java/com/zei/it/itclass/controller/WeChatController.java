package com.zei.it.itclass.controller;

import com.zei.it.itclass.config.WeChatConfig;
import com.zei.it.itclass.domain.Order;
import com.zei.it.itclass.domain.ResponseBean;
import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.service.OrderService;
import com.zei.it.itclass.service.UserService;
import com.zei.it.itclass.util.JwtUtils;
import com.zei.it.itclass.util.ResponseUtil;
import com.zei.it.itclass.util.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

@Controller
@RequestMapping("/weChat")
public class WeChatController {

    private static final String SUCCESS = "SUCCESS";

    @Autowired
    WeChatConfig weChatConfig;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;


    /**
     * 调用微信登陆二维码
     * @param accessPage
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("login")
    @ResponseBody
    public ResponseBean login(@RequestParam(value = "access_page",required = true)String accessPage) throws UnsupportedEncodingException{

        //开放平台重定向地址
        String redirectUrl = weChatConfig.getOpenRedirectUrl();

        String callbackUrl = URLEncoder.encode(redirectUrl,"GBK");

        String qrcodeUrl = String.format(weChatConfig.getOpenQrcodeUrl(),weChatConfig.getOpenAppId(),callbackUrl,accessPage);

        return ResponseUtil.buildSuccess(qrcodeUrl);
    }

    /**
     * 微信用户登录/注册
     * @param code
     * @param state
     * @param response
     */
    @GetMapping("/user/callback")
    public void weChatUserCallback(String code, String state, HttpServletResponse response){

        User user = userService.save(code);

        if(user != null){
            String token = JwtUtils.createToken(user);
            try {
                //防止站内跳转
                if(state.indexOf("http://")<0){
                    state = "http://" + state;
                }
                response.sendRedirect(state + "?token=" + token + "&name=" + URLEncoder.encode(user.getName(),"UTF-8") + "&head_img=" + user.getHeadImg());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 微信支付回调方法
     * @param request
     * @param response
     */
    @RequestMapping("/order/callback")
    public void payCallback(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setContentType("text/xml");

        //获取返回流
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        //拼装返回数据
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        while((line = reader.readLine())!=null){
            stringBuffer.append(line);
        }
        //xml转SortedMap
        SortedMap<String, String> callbackMap = WxPayUtils.xmlToSortedMap(stringBuffer.toString());

        //验证签名
        if(WxPayUtils.isSignValid(callbackMap,weChatConfig.getKey())){
            if(SUCCESS.equals(callbackMap.get("result_code"))){
                //处理订单
                String orderNo = callbackMap.get("out_trade_no");
                Order orderDb = orderService.getByOrderNo(orderNo);
                //判断订单存在且未支付
                if(orderDb != null && orderDb.getIsPay() == 0){
                    Order completeOrder = new Order();
                    completeOrder.setOrderNo(orderNo);
                    completeOrder.setOpenid(callbackMap.get("openid"));
                    completeOrder.setIsPay(1);
                    completeOrder.setNotifyTime(new Date());
                    completeOrder.setWxOrderNo(callbackMap.get("transaction_id"));
                    int result = orderService.updateOrderByOrderNo(completeOrder);
                    if(result == 1){
                        response.getWriter().println("SUCCESS");
                        return;
                    }
                }
            }
        }
        response.getWriter().println("FAIL");

    }

}
