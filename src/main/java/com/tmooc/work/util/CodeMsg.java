package com.tmooc.work.util;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum CodeMsg {
    SERVER_ERROR(500,"服务器错误"),
    SUCCESS(200,"ok"),
    MOBILE_EMPTY(400,"手机号码为空"),
    MOBILE_NOT_EXIST(402,"手机号码不存在"),
    PASSWORD_EMPTY(405,"密码为空"),
    PASSWORD_ERROR(406,"密码错误"),
    MOBILE_ERROR(407,"手机格式错误" ),
    BIND_ERROR(501,"参数校验异常：%s");
    @Setter
    private int status;
    @Setter
    private String msg;
    CodeMsg(int status, String msg){
        this.status=status;
        this.msg=msg;
    }
    public CodeMsg formateMsg(Object ... args){
        String msg= String.format(BIND_ERROR.getMsg(),args);
        BIND_ERROR.setMsg(msg);
        return BIND_ERROR;
    }
}
