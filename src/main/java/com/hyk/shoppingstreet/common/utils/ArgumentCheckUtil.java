package com.hyk.shoppingstreet.common.utils;

import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;

public class ArgumentCheckUtil {


    public static void checkCellPhone(String cellPhoneNum){
        String regex = "[1][0-9]{10}";
        ArgumentCheckUtil.checkArgument(cellPhoneNum.matches(regex), "非法手机号码");
    }

    public static void checkPassword(String password){
        ArgumentCheckUtil.checkArgument(StringUtils.isNotBlank(password), "密码不能为空");
    }

    public static void checkNickName(String nickName){
        ArgumentCheckUtil.checkArgument(StringUtils.isNotBlank(nickName), "昵称不能为空");
    }

    public static void checkStrnig(String str){
        ArgumentCheckUtil.checkArgument(StringUtils.isNotBlank(str));
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new BizException(Status.PARAM_ERROR.getCode(), Status.PARAM_ERROR.getDesc());
        }
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new BizException(Status.PARAM_ERROR.getCode(), errorMessage);
        }
    }

}
