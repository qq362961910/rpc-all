package cn.t.rpc.client.service.impl;

import cn.t.rpc.core.service.RpcServiceProvider;
import cn.t.rpc.remote.TestService;

/**
 * @description: 测试服务实现类
 * create: 2019-09-17 16:37
 * @author: yj
 **/
@RpcServiceProvider(interfaceClass = TestService.class)
public class TestServiceImpl implements TestService {

    @Override
    public String whoRu() {
        return "xiao ming";
    }
}
