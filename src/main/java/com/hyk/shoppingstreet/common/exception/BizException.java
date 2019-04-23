package com.hyk.shoppingstreet.common.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyk.shoppingstreet.common.Status;

public class BizException extends RuntimeException {
    private static final String ATTR_CODE = "errorCode";
    private static final String ATTR_DESP = "errorDesp";

    public static BizException create(Status statusEnum) {
        return new BizException(statusEnum.getCode(), statusEnum.getDesc());
    }

    public BizException(Integer errorCode, String message) {
        super((new JSONObject()).fluentPut("errorCode", errorCode).fluentPut("errorDesp", message).toJSONString());
    }

    public static Integer extractCode(String msg) {
        return JSON.parseObject(msg).getInteger("errorCode");
    }

    public static String extractMessage(String msg) {
        return JSON.parseObject(msg).getString("errorDesp");
    }
}
