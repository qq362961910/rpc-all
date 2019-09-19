package cn.t.rpc.core.config;

import cn.t.rpc.core.network.client.RpcClient;
import cn.t.rpc.core.service.RemoteServiceManager;
import cn.t.rpc.core.spring.ServiceConsumerAnnotationBeanPostProcessor;
import cn.t.rpc.core.spring.ServiceProviderAnnotationBeanPostProcessor;
import cn.t.rpc.core.zookeeper.ZookeeperTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Rpc服务配置
 * create: 2019-09-17 11:47
 * @author: yj
 **/
@EnableConfigurationProperties(RpcServiceProperties.class)
@Configuration
public class RpcServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerConfig.class);

    @Bean
    public ServiceProviderAnnotationBeanPostProcessor rpcServiceClassPathBeanDefinitionScanner() {
        return new ServiceProviderAnnotationBeanPostProcessor();
    }

    @Bean
    public ZookeeperTemplate zookeeperTemplate(RpcServiceProperties rpcServiceProperties) throws Exception {
        ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
        RpcServiceProperties.ZookeeperConfig zookeeper = rpcServiceProperties.getZookeeper();
        if(zookeeper == null) {
            logger.error("zookeeper config not set");
        } else {
            zookeeperTemplate.connect(rpcServiceProperties.getZookeeper().getRegisterAddress());
        }
        return zookeeperTemplate;
    }

    @Bean
    public ServiceConsumerAnnotationBeanPostProcessor serviceConsumerAnnotationBeanPostProcessor(RpcClient rpcClient) {
        return new ServiceConsumerAnnotationBeanPostProcessor(rpcClient);
    }

    @Bean
    public RpcClient rpcClient(ZookeeperTemplate zookeeperTemplate) {
        return new RpcClient(zookeeperTemplate);
    }

    @Bean
    public RemoteServiceManager remoteServiceManager(ZookeeperTemplate zookeeperTemplate, RpcServiceProperties rpcServiceProperties) {
        return new RemoteServiceManager(zookeeperTemplate, rpcServiceProperties);
    }

}
