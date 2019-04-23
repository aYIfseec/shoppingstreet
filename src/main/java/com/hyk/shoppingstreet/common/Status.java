package com.hyk.shoppingstreet.common;

public enum Status {
    SUCCESS(0,"成功"),
    PARAM_ERROR(1, "参数错误"),
    TOKEN_ERROR(2, "令牌错误"),
    REGISTER_ERROR(3, "该手机号已注册，请尝试登录！"),
    NO_FOUND_DATA_ERROR(4, "数据不存在"),
    NO_FOUND_USER_ERROR(4, "用户或密码错误！"),
    DUPULICAT_COLLET_ERROR(5, "您已经收藏过了！"),
    SAVE_RES_FAIL(6, "上传失败！"),
    FILE_TOO_LARGE(7, "文件过大！"),
    Null(-1, "未知")
    ;

    private Integer code;
    private String desc;

    Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Status valueOfCode(Integer code) {
        for (Status obj : Status.values()) {
            if (java.util.Objects.equals(obj.code, code)) {
                return obj;
            }
        }
        return Null;
    }

    public static Status valueOfDesc(String desc) {
        for (Status obj : Status.values()) {
            if (java.util.Objects.equals(obj.desc, desc)) {
                return obj;
            }
        }
        return Null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
