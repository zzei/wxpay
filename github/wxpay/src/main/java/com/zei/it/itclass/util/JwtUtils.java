package com.zei.it.itclass.util;

import com.zei.it.itclass.domain.User;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {


    //项目标识名
    private static final String SUBJECT = "itclass";

    //过期时间 1天
    private static final Long EXPIRE = 1000 * 60 * 60 * 24L;

    //秘钥
    private static final String APPSECRET = "zzei";

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String createToken(User user){

        if(user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                        .claim("id",user.getId())
                        .claim("name",user.getName())
                        .claim("img",user.getHeadImg())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                        .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }


    /**
     * 验证token
     * @param token
     * @return
     */
    public static Claims checkToken(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
