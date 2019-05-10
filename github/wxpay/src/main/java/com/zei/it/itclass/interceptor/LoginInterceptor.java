package com.zei.it.itclass.interceptor;

import com.zei.it.itclass.domain.User;
import com.zei.it.itclass.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor{


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //若head里为空，尝试从请求参数里获得
        if(StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        //若没有token，则返回登录
        if(!StringUtils.isEmpty(token)){
            Claims claims = JwtUtils.checkToken(token);
            if(claims != null){
                User user = new User().setId((Integer) claims.get("id"))
                                        .setName((String) claims.get("name"))
                                        .setHeadImg((String) claims.get("img"));
                request.setAttribute("user",user);
                return true;
            }
        }
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write("请登录！");
        printWriter.close();

        return false;
    }
}
