package cn.t.rpc.admin.controller;

import cn.t.rpc.admin.controller.response.ResultVoWrapper;
import cn.t.rpc.core.service.RpcServiceConsumer;
import cn.t.rpc.core.zookeeper.ZookeeperTemplate;
import cn.t.rpc.remote.TestService;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 远程服务控制器
 * create: 2019-09-17 19:52
 * @author: yj
 **/
@RequestMapping("remote-service")
@RestController
public class RemoteServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceController.class);

    private ZookeeperTemplate zookeeperTemplate;
    private ResultVoWrapper resultVoWrapper;

    @RpcServiceConsumer(interfaceClass = TestService.class)
    private TestService testService;

    @GetMapping("list")
    public Object list() throws KeeperException, InterruptedException {
        testService.whoRu();
        List<String> nodes = zookeeperTemplate.getChildren("/");
        List<String> interfaceNameList;
        if(nodes.contains("rpc") && zookeeperTemplate.getChildren("/rpc").contains("services")) {
            interfaceNameList =  zookeeperTemplate.getChildren("/rpc/services");
        } else {
            interfaceNameList = Collections.emptyList();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("interfaceNameList", interfaceNameList);
        return resultVoWrapper.buildSuccess(data);
    }

    @GetMapping("callM/{interfaceName}/method")
    public Object callM(@PathVariable("interfaceName") String interfaceName, @PathVariable("method") String method) throws KeeperException, InterruptedException {
        List<String> interfaceNameList =  zookeeperTemplate.getChildren("/rpc/services");
        if(!interfaceNameList.contains(interfaceName)) {
            logger.error("没有此接口");
            return resultVoWrapper.buildFail();
        }
        String name = testService.whoRu();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        return resultVoWrapper.buildSuccess(data);
    }

    public RemoteServiceController(ZookeeperTemplate zookeeperTemplate, ResultVoWrapper resultVoWrapper) {
        this.zookeeperTemplate = zookeeperTemplate;
        this.resultVoWrapper = resultVoWrapper;
    }
}
