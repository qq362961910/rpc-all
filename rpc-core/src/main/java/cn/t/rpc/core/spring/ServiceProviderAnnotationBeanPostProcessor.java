package cn.t.rpc.core.spring;

import cn.t.rpc.core.service.RemoteServiceProviderBean;
import cn.t.rpc.core.service.RpcServiceProvider;
import cn.t.rpc.core.util.RpcConfigTool;
import cn.t.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.context.annotation.AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;


/**
 * @description: 服务注解扫描
 * create: 2019-09-17 11:52
 * @author: yj
 **/
public class ServiceProviderAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ResourceLoaderAware, BeanClassLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderAnnotationBeanPostProcessor.class);

    private Environment environment;
    private ClassLoader classLoader;
    private ResourceLoader resourceLoader;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        RpcServiceClassPathBeanDefinitionScanner scanner = new RpcServiceClassPathBeanDefinitionScanner(registry, environment, resourceLoader);
        BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);
        scanner.setBeanNameGenerator(beanNameGenerator);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcServiceProvider.class));

        String basePackages = environment.getProperty(RpcConfigTool.getBasePackages());
        if(!StringUtil.isEmpty(basePackages)) {
            String[] packages = basePackages.split(",");
            for (String packageToScan : packages) {
                // Registers @RpcService Bean first
                scanner.scan(packageToScan);
                // Finds all BeanDefinitionHolders of @RpcService whether @ComponentScan scans or not.
                Set<BeanDefinitionHolder> beanDefinitionHolders = findServiceBeanDefinitionHolders(scanner, packageToScan, registry, beanNameGenerator);
                if (!CollectionUtils.isEmpty(beanDefinitionHolders)) {
                    for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                        Class<?> beanClass = ClassUtils.resolveClassName(beanDefinitionHolder.getBeanDefinition().getBeanClassName(), classLoader);
                        RpcServiceProvider rpcServiceProvider = findAnnotation(beanClass, RpcServiceProvider.class);
                        Class<?> interfaceClass = rpcServiceProvider.interfaceClass();
                        String annotatedServiceBeanName = beanDefinitionHolder.getBeanName();
                        BeanDefinitionBuilder builder = rootBeanDefinition(RemoteServiceProviderBean.class);
                        AbstractBeanDefinition serviceBeanDefinition = builder.getBeanDefinition();
                        builder.addPropertyReference("ref", annotatedServiceBeanName);
                        builder.addPropertyValue("interfaceName", interfaceClass.getName());
                        String remoteServiceBeanName = "remote:" +  interfaceClass.getName();
                        registry.registerBeanDefinition(remoteServiceBeanName, serviceBeanDefinition);
                    }
                } else {
                    logger.warn("No Spring Bean annotating @RpcService was found under package[{}]", packageToScan);
                }
            }
        } else {
            logger.warn("scan path not set");
        }
    }

    private BeanNameGenerator resolveBeanNameGenerator(BeanDefinitionRegistry registry) {
        BeanNameGenerator beanNameGenerator = null;
        if (registry instanceof SingletonBeanRegistry) {
            SingletonBeanRegistry singletonBeanRegistry = SingletonBeanRegistry.class.cast(registry);
            beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(CONFIGURATION_BEAN_NAME_GENERATOR);
        }
        if (beanNameGenerator == null) {
            logger.info("BeanNameGenerator bean can't be found in BeanFactory with name [{}]", CONFIGURATION_BEAN_NAME_GENERATOR);
            logger.info("BeanNameGenerator will be a instance of {} , it maybe a potential problem on bean name generation.", AnnotationBeanNameGenerator.class.getName());
            beanNameGenerator = new AnnotationBeanNameGenerator();
        }
        return beanNameGenerator;
    }

    private Set<BeanDefinitionHolder> findServiceBeanDefinitionHolders(
        ClassPathBeanDefinitionScanner scanner, String packageToScan, BeanDefinitionRegistry registry,
        BeanNameGenerator beanNameGenerator) {
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);
        Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>(beanDefinitions.size());
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
            beanDefinitionHolders.add(beanDefinitionHolder);
        }
        return beanDefinitionHolders;

    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
