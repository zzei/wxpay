package com.zei.it.itclass.domain;

import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable{
 
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 微信id
	 */
	private String openid;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 头像
	 */
	private String headImg;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 用户签名
	 */
	private String sign;
	/**
	 * 性别 0：女 1：男
	 */
	private Double sex;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	/**
	 * 状态标识
	 */
	private Integer state;

}
