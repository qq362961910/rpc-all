package cn.t.rpc.core.spring;

import cn.t.rpc.core.network.msg.CallMethodMsg;
import cn.t.rpc.core.service.RemoteServiceConsumerBean;
import cn.t.util.common.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ServiceConsumerAnnotationInjectedElement extends InjectionMetadata.InjectedElement {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConsumerAnnotationInjectedElement.class);

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

    private Object buildProxy(RemoteServiceConsumerBean remoteServiceConsumerBean) {
        return Proxy.newProxyInstance(remoteServiceConsumerBean.getClassLoader(), new Class[]{remoteServiceConsumerBean.getInterfaceClass()}, new ReferenceBeanInvocationHandler(remoteServiceConsumerBean));
    }

    private static class ReferenceBeanInvocationHandler implements InvocationHandler {

        private RemoteServiceConsumerBean remoteServiceConsumerBean;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            CallMethodMsg callMethodMsg = new CallMethodMsg();
            callMethodMsg.setInterfaceName(remoteServiceConsumerBean.getInterfaceClass().getName());
            callMethodMsg.setMethodName(method.getName());
            if(ArrayUtil.isEmpty(args)) {
                callMethodMsg.setArg(null);
            } else {
                callMethodMsg.setArg(args[0]);
            }
            return remoteServiceConsumerBean.getRpcClient().callMethod(callMethodMsg);
        }

        public ReferenceBeanInvocationHandler(RemoteServiceConsumerBean remoteServiceConsumerBean) {
            this.remoteServiceConsumerBean = remoteServiceConsumerBean;
        }
    }
}
