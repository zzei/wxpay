package com.zei.it.itclass.service;

import com.zei.it.itclass.domain.Article;

import java.util.List;

public interface ArticleService {

    List<Article> findAll();

    Article getOne(int id);

    Integer save(Article article);

    Integer update(Article article);
}
