package cn.t.rpc.client.service.impl;

import cn.t.rpc.client.service.TestService;
import cn.t.rpc.core.service.RpcService;

/**
 * @description: 测试服务实现类
 * create: 2019-09-17 16:37
 * @author: yj
 **/
@RpcService(interfaceClass = TestService.class)
public class TestServiceImpl implements TestService {
}
