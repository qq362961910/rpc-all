package cn.t.rpc.core.service;

import cn.t.rpc.core.network.client.RpcClient;

/**
 * @description: 远程服务消费配置
 * create: 2019-09-18 11:07
 * @author: yj
 **/
public class RemoteServiceConsumerBean {

    private RpcClient rpcClient;

    private ClassLoader classLoader;

    private transient volatile Class interfaceClass;

    private String registryAddress;

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }
}
