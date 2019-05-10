package com.zei.it.itclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 统一返回前端的类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResponseBean {

    //请求信息标识 成功：200  无效地址：404  异常：500
    private int code;

    //返回数据对象
    private Object data;

    //返回信息
    private String msg;

}
