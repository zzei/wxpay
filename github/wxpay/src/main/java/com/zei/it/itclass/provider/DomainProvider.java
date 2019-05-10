package com.zei.it.itclass.provider;

import com.zei.it.itclass.domain.*;
import org.apache.ibatis.jdbc.SQL;

/**
 * article 构建动态sql语句
 */
public class DomainProvider {


    public String updateArticle(final Article article){
        return new SQL(){
            {
                UPDATE("article");

                if(article.getTitle() != null){
                    SET("title = #{title}");
                }
                if(article.getDescription() != null){
                    SET("description = #{description}");
                }
                if(article.getCoverImg() != null){
                    SET("cover_img = #{coverImg}");
                }
                if(article.getPrice() != null){
                    SET("price = #{price}");
                }
                if(article.getCreateTime() != null){
                    SET("create_time = #{createTime}");
                }
                if(article.getIsOnline() != null){
                    SET("is_online = #{isOnline}");
                }
                if(article.getMark() != null){
                    SET("mark = #{mark}");
                }
                if(article.getState() != null){
                    SET("state = #{state}");
                }
            }
        }.toString();

    }

    public String updateUser(final User user){
        return new SQL(){
            {
                UPDATE("user");

                if(user.getOpenid() != null){
                    SET("openid = #{openid}");
                }
                if(user.getName() != null){
                    SET("name = #{name}");
                }
                if(user.getHeadImg() != null){
                    SET("head_img = #{headImg}");
                }
                if(user.getPhone() != null){
                    SET("phone = #{phone}");
                }
                if(user.getSign() != null){
                    SET("sign = #{sign}");
                }
                if(user.getSex() != null){
                    SET("sex = #{sex}");
                }
                if(user.getCity() != null){
                    SET("city = #{city}");
                }
                if(user.getCreateTime() != null){
                    SET("create_time = #{createTime}");
                }
                if(user.getState() != null){
                    SET("state = #{state}");
                }
            }
        }.toString();

    }





    public String updateOrder(final Order order){
        return new SQL(){
            {
                UPDATE("`order`");

                if(order.getOpenid() != null){
                    SET("openid = #{openid}");
                }
                if(order.getNickName() != null){
                    SET("nick_name = #{nickName}");
                }
                if(order.getHeadImg() != null){
                    SET("head_img = #{headImg}");
                }
                if(order.getOrderNo() != null){
                    SET("order_no = #{orderNo}");
                }
                if(order.getIsPay() != null){
                    SET("is_pay = #{isPay}");
                }
                if(order.getNotifyTime() != null){
                    SET("notify_time = #{notifyTime}");
                }
                if(order.getNotifyTime() != null){
                    SET("wx_order_no = #{wxOrderNo}");
                }
                if(order.getPay() != null){
                    SET("pay = #{pay}");
                }
                if(order.getUserId() != null){
                    SET("user_id = #{userId}");
                }
                if(order.getArticleId() != null){
                    SET("article_id = #{articleId}");
                }
                if(order.getArticleTitle() != null){
                    SET("article_title = #{articleTitle}");
                }
                if(order.getArticleImg() != null){
                    SET("article_img = #{articleImg}");
                }
                if(order.getIp() != null){
                    SET("ip = #{ip}");
                }
                if(order.getState() != null){
                    SET("state = #{state}");
                }
            }
        }.toString();

    }





}
