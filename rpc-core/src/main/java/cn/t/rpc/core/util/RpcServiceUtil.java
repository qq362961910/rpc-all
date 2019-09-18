package cn.t.rpc.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: rpc service util
 * create: 2019-09-18 16:14
 * @author: yj
 **/
public class RpcServiceUtil {
    private static Map<String, Map<Long, Object>> RESULT_MAP = new HashMap<>();

    public static void request(String interfaceName, Long id) {
        Map<Long, Object> map = RESULT_MAP.computeIfAbsent(interfaceName, k -> new HashMap<>());
        map.put(id, null);
    }

    public static Object getRequestResult(String interfaceName, Long id) {
        Map<Long, Object> map = RESULT_MAP.get(interfaceName);
        if(map == null) {
            return null;
        } else {
            return map.get(id);
        }
    }
}
