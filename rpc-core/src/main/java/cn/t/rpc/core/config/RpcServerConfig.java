package cn.t.rpc.core.config;

import cn.t.rpc.core.network.RpcServiceConfig;
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
@EnableConfigurationProperties(RpcServiceConfig.class)
@Configuration
public class RpcServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerConfig.class);

    @Bean
    public ServiceProviderAnnotationBeanPostProcessor rpcServiceClassPathBeanDefinitionScanner() {
        return new ServiceProviderAnnotationBeanPostProcessor();
    }

    @Bean
    public ZookeeperTemplate zookeeperTemplate(RpcServiceConfig rpcServiceConfig) throws Exception {
        ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
        RpcServiceConfig.ZookeeperConfig zookeeper = rpcServiceConfig.getZookeeper();
        if(zookeeper == null) {
            logger.error("zookeeper config not set");
        } else {
            zookeeperTemplate.connect(rpcServiceConfig.getZookeeper().getRegisterAddress());
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
    public RemoteServiceManager remoteServiceManager(ZookeeperTemplate zookeeperTemplate, RpcServiceConfig rpcServiceConfig) {
        return new RemoteServiceManager(zookeeperTemplate, rpcServiceConfig);
    }

}
