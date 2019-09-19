package cn.t.rpc.core.service;

import cn.t.rpc.core.config.RpcServiceProperties;
import cn.t.rpc.core.network.server.RpcSeverNettyChannelInitializer;
import cn.t.rpc.core.zookeeper.ZookeeperTemplate;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpSever;
import cn.t.util.common.JsonUtil;
import cn.t.util.common.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: remote service manager
 * create: 2019-09-18 20:58
 * @author: yj
 **/
public class RemoteServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceManager.class);

    private Map<String, Object> serviceRefMapping = new HashMap<>();
    private Map<Integer, DaemonServer> serverMap = new ConcurrentHashMap<>();
    private ZookeeperTemplate zookeeperTemplate;
    private RpcServiceProperties rpcServiceProperties;

    public void registerService(ServiceItem serviceItem) {
        if(rpcServiceProperties != null) {
            RpcServiceProperties.ServerConfig serverConfig = rpcServiceProperties.getServer();
            if(serverConfig != null) {
                DaemonServer daemonServer = serverMap.get(serverConfig.getPort());
                if(daemonServer == null) {
                    NettyTcpSever nettyTcpSever = new NettyTcpSever("rpc-server", serverConfig.getPort(), new RpcSeverNettyChannelInitializer(this));
                    DefaultLauncher defaultLauncher = new DefaultLauncher();
                    List<DaemonServer> daemonServerList = new ArrayList<>();
                    daemonServerList.add(nettyTcpSever);
                    defaultLauncher.setDaemonServerList(daemonServerList);
                    defaultLauncher.startup();
                }
                boolean success = registerToZookeeper(serviceItem, serverConfig);
                if(success) {
                    logger.info("远程服务: {},服务地址: {}:{} 发布成功", serviceItem.getInterfaceName(), serverConfig.getHost(), serverConfig.getPort());
                }
                serviceRefMapping.put(serviceItem.getInterfaceName(), serviceItem.getRef());
            } else {
                logger.warn("远程服务未设置合法的端口");
            }
        }
    }

    public Object getRefByInterfaceName(String interfaceName) {
        return serviceRefMapping.get(interfaceName);
    }

    private boolean registerToZookeeper(ServiceItem serviceItem, RpcServiceProperties.ServerConfig serverConfig ) {
        try {
            List<String> nodes = zookeeperTemplate.getChildren("/");
            Set<RpcServiceProperties.ServerConfig> serviceHostSet = new HashSet<>();
            serviceHostSet.add(serverConfig);
            if(!nodes.contains("rpc")) {
                zookeeperTemplate.createNode("/rpc", "");
                zookeeperTemplate.createNode("/rpc/services", "");
                zookeeperTemplate.createNode("/rpc/services/" + serviceItem.getInterfaceName(), JsonUtil.serialize(serviceHostSet));
            } else {
                List<String> serviceNodes = zookeeperTemplate.getChildren("/rpc");
                if(!serviceNodes.contains("services")) {
                    zookeeperTemplate.createNode("/rpc/services", "");
                    zookeeperTemplate.createNode("/rpc/services/" + serviceItem.getInterfaceName(), JsonUtil.serialize(serviceHostSet));
                } else {
                    List<String> interfaceNodes = zookeeperTemplate.getChildren("/rpc/services");
                    if(!interfaceNodes.contains(serviceItem.getInterfaceName())) {
                        zookeeperTemplate.createNode("/rpc/services/" + serviceItem.getInterfaceName(), JsonUtil.serialize(serviceHostSet));
                    } else {
                        String data = zookeeperTemplate.getData("/rpc/services/" + serviceItem.getInterfaceName());
                        if(StringUtil.isEmpty(data)) {
                            zookeeperTemplate.setData("/rpc/services/" + serviceItem.getInterfaceName(), JsonUtil.serialize(serviceHostSet));
                        } else {
                            HashSet<RpcServiceProperties.ServerConfig> oldSet = JsonUtil.deserialize(data, new TypeReference<HashSet<RpcServiceProperties.ServerConfig>>(){});
                            oldSet.addAll(serviceHostSet);
                            zookeeperTemplate.setData("/rpc/services/" + serviceItem.getInterfaceName(), JsonUtil.serialize(oldSet));
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("zookeeper注册服务失败", e);
        }
        return false;
    }

    public ZookeeperTemplate getZookeeperTemplate() {
        return zookeeperTemplate;
    }

    public void setZookeeperTemplate(ZookeeperTemplate zookeeperTemplate) {
        this.zookeeperTemplate = zookeeperTemplate;
    }

    public RpcServiceProperties getRpcServiceProperties() {
        return rpcServiceProperties;
    }

    public void setRpcServiceProperties(RpcServiceProperties rpcServiceProperties) {
        this.rpcServiceProperties = rpcServiceProperties;
    }

    public RemoteServiceManager(ZookeeperTemplate zookeeperTemplate, RpcServiceProperties rpcServiceProperties) {
        this.zookeeperTemplate = zookeeperTemplate;
        this.rpcServiceProperties = rpcServiceProperties;
    }
}
