# wxpay
微信授权登陆、jwt、微信支付

# 微信登陆与支付

## 1、微信授权登陆（拥有公共号）

### 1.1、微信信息配置类

```
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
```

### 1.2、JwtUtils

```xml
<!--jwt-->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt</artifactId>
			<version>0.9.0</version>
		</dependency>
```

```java
package com.zei.it.itclass.util;

import com.zei.it.itclass.domain.User;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {


    //项目标识名
    private static final String SUBJECT = "itclass";

    //过期时间 1天
    private static final Long EXPIRE = 1000 * 60 * 60 * 24L;

    //秘钥
    private static final String APPSECRET = "zzei";

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String createToken(User user){

        if(user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                        .claim("id",user.getId())
                        .claim("name",user.getName())
                        .claim("img",user.getHeadImg())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                        .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }


    /**
     * 验证token
     * @param token
     * @return
     */
    public static Claims checkToken(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

### 1.3、拦截器

```java
package com.zei.it.itclass.interceptor;

import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor{


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //若head里为空，尝试从请求参数里获得
        if(StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        //若没有token，则返回登录
        if(!StringUtils.isEmpty(token)){
            Claims claims = JwtUtils.checkToken(token);
            if(claims != null){
                User user = new User().setId((Integer) claims.get("id"))
                                        .setName((String) claims.get("name"))
                                        .setHeadImg((String) claims.get("img"));
                request.setAttribute("user",user);
                return true;
            }
        }
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write("请登录！");
        printWriter.close();

        return false;
    }
}
```

### 1.4、微信获取登陆二维码

```java
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
```

### 1.5、HttpUtils

```java
package com.zei.it.itclass.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求工具
 */
public class HttpUtils {

    private static Gson gson = new Gson();

    /**
     * get请求
     * @param url
     * @return
     */
    public static Map<String,Object> doGet(String url){
        Map<String,Object> result = new HashMap<>();
        //创建连接
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                                                            .setConnectionRequestTimeout(5000)
                                                            .setSocketTimeout(5000)
                                                            .setRedirectsEnabled(true)
                                                            .build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type","text/html charset=UTF-8");
        httpGet.setConfig(requestConfig);

        try {
            //获取请求结果
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                //请求成功返回结果数据
                String jsonResult = EntityUtils.toString(httpResponse.getEntity());
                result = gson.fromJson(jsonResult,result.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    /**
     * post请求
     * @param url
     * @param data
     * @param timeout
     * @return
     */
    public static String doPost(String url, String data, int timeout){
        //创建连接
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .setRedirectsEnabled(true)
                .build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        httpPost.addHeader("Content-Type","text/html;charset=UTF-8");
        StringEntity entity = new StringEntity(data,"UTF-8");
        httpPost.setEntity(entity);

        try {
            //获取请求结果
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpResponse.getStatusLine().getStatusCode() == 200){

                String result = EntityUtils.toString(httpEntity,"UTF-8");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
```

### 1.6、登陆回调方法

```java
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
```

```java
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
```

## 2、微信支付

### 2.1、CommonUtils、WxPayUtils

```java
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
```

```java
package com.zei.it.itclass.util;

import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 * 微信支付相关的工具类
 */
public class WxPayUtils {
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }
		/**
     * XML格式字符串转换为SortedMap
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static SortedMap<String, String> xmlToSortedMap(String strXML) throws Exception {
        try {
            SortedMap<String, String> data = new TreeMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }
    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }

    /**
     * 生成微信签名
     * @param params
     * @param key
     * @return
     */
    public static String createSign(SortedMap<String, String> params, String key){
        StringBuffer stringBuffer = new StringBuffer();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if(!StringUtils.isEmpty(k) && !StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)){
                stringBuffer.append(k + "=" + v + "&");
            }
        }
        stringBuffer.append("key=").append(key);
        String sign = CommonUtils.MD5(stringBuffer.toString());
        return sign;
    }

    /**
     * 验证签名是否正确
     * @param params
     * @param key
     * @return
     */
    public static boolean isSignValid(SortedMap<String, String> params, String key){

        //生成的签名
        String sign = createSign(params,key);
        //微信返回的签名
        String returnSign = params.get("sign").toUpperCase();

        return sign.equals(returnSign);
    }
}
```

### 2.2、生成签名、调用微信统一下单

​		参考文档 <https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1>

```java
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
}
```

### 2.3、生成二维码

​		使用谷歌二维码生成工具，引入依赖

```xml
<!--google二维码-->
		<dependency>
			<groupId>com.google.zxing</groupId>
			<artifactId>core</artifactId>
			<version>3.3.0</version>
		</dependency>
		<dependency>
			<groupId>com.google.zxing</groupId>
			<artifactId>javase</artifactId>
			<version>3.3.0</version>
		</dependency>

```

​		将二维码流输出

```java
String codeUrl = orderService.save(order);
if(StringUtils.isEmpty(codeUrl)){
  throw new NullPointerException();
}

try {
  //生成二维码
  //设置纠错等级
  Map<EncodeHintType, Object> hits = new HashMap<>();
  hits.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
  hits.put(EncodeHintType.CHARACTER_SET, "UTF-8");

  //设置二维码
  BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 300, 300, hits);
  //输出
  OutputStream outputStream = response.getOutputStream();
  MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
}catch (Exception e){
  e.printStackTrace();
}
```

### 2.4、扫码支付回调

​		返回地址为notify_url的参数

```java
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
```

### 	2.5、开启事务

​		启动类上加上@EnableTransactionManagement

​		在需要的方法上加@Transactional(propagation = Propagation.REQUIRED)

## 3、开启全局异常

### 	3.1、自定义异常类

```java
package com.zei.it.itclass.exception;

public class MyException extends RuntimeException{

    private Integer code;

    private String msg;

    public MyException(Integer code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

### 	3.2、配置

```java
package com.zei.it.itclass.exception;

import com.zei.it.itclass.domain.ResponseBean;
import com.zei.it.itclass.util.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseBean Handler(Exception e){
        if(e instanceof MyException){
            return ResponseUtil.buildError(((MyException) e).getCode(),((MyException) e).getMsg());
        }
        return ResponseUtil.buildError("其他异常");
    }
}
```

