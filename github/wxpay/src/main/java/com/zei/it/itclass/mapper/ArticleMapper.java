package com.zei.it.itclass.mapper;

import com.zei.it.itclass.domain.Article;
import com.zei.it.itclass.provider.DomainProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {

    @Select("select * from article")
    List<Article> findAll();

    @Select("select * from article where id = #{id}")
    Article getOne(int id);

    @Insert("insert into article(title,description,cover_img,price,create_time,is_online,mark,state) " +
            "values(#{title},#{description},#{coverImg},#{price},#{createTime},#{isOnline},#{mark},#{state})")
    Integer save(Article article);

    @UpdateProvider(type = DomainProvider.class,method = "updateArticle")
    Integer update(Article article);


}
