package com.zei.it.itclass.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zei.it.itclass.domain.Article;
import com.zei.it.itclass.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;


    /**
     * 分页列表
     * @param pageNo 第几页 默认1
     * @param pageSize 显示几条数据 默认10
     * @return
     */
    @GetMapping("/page")
    public PageInfo<Article> findAll(@RequestParam(value = "pageNo",defaultValue = "1")int pageNo,
                          @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){


        PageHelper.startPage(pageNo,pageSize);
        List<Article> list = articleService.findAll();

        PageInfo<Article> pages = new PageInfo<>(list);

        return pages;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/article/{id}")
    public Article getById(@PathVariable("id")int id){

        return articleService.getOne(id);
    }


    /**
     * 新增保存文章
     * @param article
     * @return
     */
    @PostMapping("/article")
    public Integer save(Article article){

        return articleService.save(article);
    }

    /**
     * 更新文章信息
     * @param article
     * @return
     */
    @PutMapping("/article")
    public Integer update(Article article){

        return articleService.update(article);
    }


}
