package com.zei.it.itclass.mapper;

import com.zei.it.itclass.domain.Order;
import com.zei.it.itclass.provider.DomainProvider;
import org.apache.ibatis.annotations.*;
;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderMapper {

	@Select("select * from `order`")
	List<Order> findAll();

	@Select("select * from `order` where id = #{id}")
	Order getOne(int id);

	@Insert("insert into `order`(openid,nick_name,head_img,order_no,is_pay,notify_time,wx_order_no,pay,user_id,article_id,article_title,article_img,ip,create_time,state) values(" +
			"#{openid},#{nickName},#{headImg},#{orderNo},#{isPay},#{notifyTime},#{wxOrderNo},#{pay},#{userId},#{articleId},#{articleTitle},#{articleImg},#{ip},#{createTime},#{state})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer save(Order order);

	@UpdateProvider(type = DomainProvider.class,method = "updateOrder")
	Integer update(Order order);

	/**
	 * 删除订单
	 * @param id
	 * @return
	 */
	@Update("update `order` set state = 0 where id = #{id}")
	Integer del(int id);

	/**
	 * 根据订单编号查找订单
	 * @param orderNo
	 * @return
	 */
	@Select("select * from `order` where order_no = #{orderNo}")
	Order getByOrderNo(String orderNo);

	/**
	 * 查看用户的订单列表
	 * @param userId
	 * @return
	 */
	@Select("select * from `order` where user_id = #{userId} and state = 1")
	List<Order> findByUser(int userId);


	@Update("update `order` set is_pay = #{isPay},notify_time = #{notifyTime},wx_order_no = #{wxOrderNo},openid = #{openid} where order_no = #{orderNo} and state = 1")
	Integer updateOrderByOrderNo(Order order);

}
