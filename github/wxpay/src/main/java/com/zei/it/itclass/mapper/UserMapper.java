package com.zei.it.itclass.mapper;

import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.provider.DomainProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper {

	@Select("select * from user")
	List<User> findAll();

	@Select("select * from user where id = #{id}")
	User getOne(int id);

	@Insert("insert into user(openid,name,head_img,phone,sign,sex,city,create_time,state) values(" + 
			"#{openid},#{name},#{headImg},#{phone},#{sign},#{sex},#{city},#{createTime},#{state})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer save(User user);

	@UpdateProvider(type = DomainProvider.class,method = "updateUser")
	Integer update(User user);

	@Select("select * from user where openid = #{openid}")
	User getUserByOpenId(String openid);

}
