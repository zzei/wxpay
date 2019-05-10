package com.zei.it.itclass.domain;

import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Order implements Serializable{
 
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 用户微信id
	 */
	private String openid;
	/**
	 * 微信昵称
	 */
	private String nickName;
	/**
	 * 头像
	 */
	private String headImg;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 是否支付 0：未支付 1：已支付
	 */
	private Integer isPay;
	/**
	 * 支付时间
	 */
	private java.util.Date notifyTime;
	/**
	 * 微信支付订单号
	 */
	private String wxOrderNo;
	/**
	 * 支付金额 单位 分
	 */
	private Integer pay;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 文章id
	 */
	private Integer articleId;
	/**
	 * 文章标题
	 */
	private String articleTitle;
	/**
	 * 文章封面
	 */
	private String articleImg;
	/**
	 * 用户ip地址
	 */
	private String ip;
	/**
	 * 订单创建时间
	 */
	private Date createTime;
	/**
	 * 状态标识
	 */
	private Integer state;

}
