package cn.t.rpc.core.service;

/**
 * @description: 远程服务消费配置
 * create: 2019-09-18 11:07
 * @author: yj
 **/
public class RemoteServiceConsumerBean {

    private ClassLoader classLoader;

    private transient volatile Class interfaceClass;

    private String registryAddress;

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
