package cn.t.rpc.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: rpc service util
 * create: 2019-09-18 16:14
 * @author: yj
 **/
public class RpcServiceUtil {
    private static  Map<Long, Object> RESULT_MAP = new HashMap<>();

    public static void request(Long id) {
        RESULT_MAP.put(id, null);
    }

    public static synchronized Object getRequestResult(Long id) {
        return RESULT_MAP.remove(id);
    }

    public static synchronized Object setRequestResult(Long id, Object result) {
        return RESULT_MAP.put(id, result);
    }

    public static Map<Long, Object> getAllResults() {
        return RESULT_MAP;
    }
}
