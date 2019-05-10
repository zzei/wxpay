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
