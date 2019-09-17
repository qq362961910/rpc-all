package cn.t.rpc.core.util;

/**
 * @description: 配置
 * create: 2019-09-17 16:26
 * @author: yj
 **/
public class RpcConfigTool {
    public static final String SEPARATOR = ".";
    public static final String PREFIX = "rpc";
    public static final String APP_PREFIX = "app";
    public static final String SERVER_PREFIX = "server";
    public static final String BASE_PACKAGES = "base-packages";


    public static String getBasePackages() {
        return PREFIX.concat(SEPARATOR).concat(APP_PREFIX.concat(SEPARATOR).concat(BASE_PACKAGES));
    }

}
