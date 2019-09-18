package cn.t.rpc.client.service.impl;

import cn.t.rpc.core.service.RpcServiceProvider;
import cn.t.rpc.remote.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 测试服务实现类
 * create: 2019-09-17 16:37
 * @author: yj
 **/
@RpcServiceProvider(interfaceClass = TestService.class)
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Override
    public String whoRu() {
        logger.info("method run: whoRu");
        return "xiao ming";
    }
}
