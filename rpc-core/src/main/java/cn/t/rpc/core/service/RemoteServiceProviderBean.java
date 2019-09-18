package cn.t.rpc.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @description: 远程调用服务
 * create: 2019-09-17 09:51
 * @author: yj
 **/
public class RemoteServiceProviderBean implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceProviderBean.class);

    private String interfaceName;
    private Object ref;
    private RemoteServiceManager remoteServiceManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        registerService();
    }

    private void registerService() {
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setInterfaceName(interfaceName);
        serviceItem.setRef(ref);
        remoteServiceManager.registerService(serviceItem);
    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }

    public RemoteServiceProviderBean(RemoteServiceManager remoteServiceManager) {
        this.remoteServiceManager = remoteServiceManager;
    }
}
