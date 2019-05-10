package com.zei.it.itclass.domain;

import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Article implements Serializable{
 
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 文章标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 封面图
	 */
	private String coverImg;
	/**
	 * 价格，单位：分
	 */
	private Integer price;
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	/**
	 * 是否上线 0：未上线 1：已上线
	 */
	private Integer isOnline;
	/**
	 * 评分，最高10分
	 */
	private Double mark;
	/**
	 * 状态标识
	 */
	private Integer state;

}
