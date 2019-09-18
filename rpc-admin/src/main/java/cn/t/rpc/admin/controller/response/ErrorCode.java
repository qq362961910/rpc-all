package cn.t.rpc.admin.controller.response;

public enum ErrorCode {

    BAD_PARAM_EXCEPTION("400", "参数不合法"),

    SOURCE_NOT_FOUND_EXCEPTION("404", "资源不存在"),

    REQUEST_METHOD_NOT_SUPPORT("405", "请求方法不支持"),

    SERVER_INTERNAL_EXCEPTION("500", "服务器内部异常");


    public final String code;

    public final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCode getErrorCodeDescription(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return SERVER_INTERNAL_EXCEPTION;
    }

}
