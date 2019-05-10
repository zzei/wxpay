package com.zei.it.itclass.controller;

import com.zei.it.itclass.domain.ResponseBean;
import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.util.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public ResponseBean login(User user){
        return ResponseUtil.buildSuccess(user,"登录成功！");
    }

    @GetMapping("/detail")
    public ResponseBean detail(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return ResponseUtil.buildSuccess(user,"yes");
    }
}
