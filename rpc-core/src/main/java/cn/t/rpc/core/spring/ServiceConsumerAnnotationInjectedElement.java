package cn.t.rpc.core.spring;

import cn.t.rpc.core.service.RemoteServiceConsumerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.InjectionMetadata;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: 消费服务注入
 * create: 2019-09-18 10:56
 * @author: yj
 **/
public class ServiceConsumerAnnotationInjectedElement extends InjectionMetadata.InjectedElement implements BeanClassLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConsumerAnnotationInjectedElement.class);

    private ClassLoader classLoader;
    private RemoteServiceConsumerBean remoteServiceConsumerBean;

    private final ConcurrentMap<String, Object> injectedObjectsCache = new ConcurrentHashMap<>(32);

    public ServiceConsumerAnnotationInjectedElement(RemoteServiceConsumerBean remoteServiceConsumerBean, Member member, PropertyDescriptor pd) {
        super(member, pd);
        this.remoteServiceConsumerBean = remoteServiceConsumerBean;
    }

    @Override
    protected Object getResourceToInject(Object target, String requestingBeanName) {
        if(member instanceof Field) {
            Field field = (Field)member;
            Class<?> injectedType = field.getType();
            String cacheKey = "consumer:" + injectedType.getName();
            Object injectedObject = injectedObjectsCache.get(cacheKey);
            if (injectedObject == null) {
                injectedObject = buildProxy(remoteServiceConsumerBean);
                injectedObjectsCache.put(cacheKey, injectedObject);
            }
            return injectedObject;
        } else {
            logger.error("inject rpc service consumer failed, member type: {}", member.getClass());
        }
        return null;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private Object buildProxy(RemoteServiceConsumerBean remoteServiceConsumerBean) {
        return Proxy.newProxyInstance(classLoader, new Class[]{remoteServiceConsumerBean.getInterfaceClass()}, new ReferenceBeanInvocationHandler(remoteServiceConsumerBean));
    }

    private static class ReferenceBeanInvocationHandler implements InvocationHandler {

        private RemoteServiceConsumerBean remoteServiceConsumerBean;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }

        public ReferenceBeanInvocationHandler(RemoteServiceConsumerBean remoteServiceConsumerBean) {
            this.remoteServiceConsumerBean = remoteServiceConsumerBean;
        }
    }
}
