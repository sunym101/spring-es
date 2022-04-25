package com.sunym.springes.entity;


import lombok.Data;

@Data
public class ResultEntity {

    public static final String RES_CODE_OK = "0000";
    public static final String RES_CODE_NG = "1111";

    public static final String RES_MSG_OK = "处理成功";

    public static final String RES_MSG_NG = "处理失败";


    private String code;

    private String message;

    private Object info;

    public ResultEntity() {
    }

    public ResultEntity(String code, String message, Object info) {
        this.code = code;
        this.message = message;
        this.info = info;
    }

    public static ResultEntity ok(Object info) {
        ResultEntity result = new ResultEntity(RES_CODE_OK, RES_MSG_OK, info);
        return result;
    }

    public static ResultEntity ng(Object info) {
        ResultEntity result = new ResultEntity(RES_CODE_NG, RES_MSG_NG, info);
        return result;
    }
}
