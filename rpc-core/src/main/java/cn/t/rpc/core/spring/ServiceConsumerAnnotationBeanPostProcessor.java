package cn.t.rpc.core.spring;

import cn.t.rpc.core.network.client.RpcClient;
import cn.t.rpc.core.service.RemoteServiceConsumerBean;
import cn.t.rpc.core.service.RpcServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @description: rpc消费服务注解
 * create: 2019-09-18 10:47
 * @author: yj
 **/
public class ServiceConsumerAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanClassLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConsumerAnnotationBeanPostProcessor.class);

    private ClassLoader classLoader;
    private RpcClient rpcClient;

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        final List<InjectionMetadata.InjectedElement> elements = new LinkedList<>();
        ReflectionUtils.doWithFields(bean.getClass(), (field) -> {
            RpcServiceConsumer annotation = AnnotationUtils.getAnnotation(field, RpcServiceConsumer.class);
            if (annotation != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("@RpcServiceConsumer is not supported on static fields: " + field);
                    }
                    return;
                }
                RemoteServiceConsumerBean remoteServiceConsumerBean = new RemoteServiceConsumerBean();
                remoteServiceConsumerBean.setInterfaceClass(annotation.interfaceClass());
                remoteServiceConsumerBean.setClassLoader(classLoader);
                remoteServiceConsumerBean.setRpcClient(rpcClient);
                elements.add(new ServiceConsumerAnnotationInjectedElement(remoteServiceConsumerBean, field, null));
            }
        });
        InjectionMetadata metadata = new InjectionMetadata(bean.getClass(), elements);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of @RpcServiceConsumer" + " dependencies is failed", ex);
        }
        return pvs;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ServiceConsumerAnnotationBeanPostProcessor(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }
}
