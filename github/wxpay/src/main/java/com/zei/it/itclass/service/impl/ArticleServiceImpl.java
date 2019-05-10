package com.zei.it.itclass.service.impl;

import com.zei.it.itclass.domain.Article;
import com.zei.it.itclass.mapper.ArticleMapper;
import com.zei.it.itclass.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public List<Article> findAll() {
        return articleMapper.findAll();
    }

    @Override
    public Article getOne(int id) {
        return articleMapper.getOne(id);
    }

    @Override
    public Integer save(Article article) {
        return articleMapper.save(article);
    }

    @Override
    public Integer update(Article article) {
        return articleMapper.update(article);
    }
}
