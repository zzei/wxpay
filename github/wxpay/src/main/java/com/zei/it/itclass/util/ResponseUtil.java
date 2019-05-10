package com.zei.it.itclass.util;

import com.zei.it.itclass.domain.ResponseBean;

public class ResponseUtil {

    /**
     * 带数据返回成功
     * @param data
     * @return
     */
    public static ResponseBean buildSuccess(Object data){
        return new ResponseBean().setCode(200).setData(data);
    }

    /**
     * 带数据带消息返回成功
     * @param data
     * @param msg
     * @return
     */
    public static ResponseBean buildSuccess(Object data,String msg){
        return new ResponseBean().setCode(200).setData(data).setMsg(msg);
    }

    /**
     * 带消息返回失败
     * @param msg
     * @return
     */
    public static ResponseBean buildError(String msg){
        return new ResponseBean().setCode(500).setMsg(msg);
    }


}
