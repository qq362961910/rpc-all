package cn.t.rpc.core.config;

import cn.t.rpc.core.network.RpcServiceConfig;
import cn.t.rpc.core.spring.ServiceAnnotationBeanPostProcessor;
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
    public ServiceAnnotationBeanPostProcessor rpcServiceClassPathBeanDefinitionScanner() {
        return new ServiceAnnotationBeanPostProcessor();
    }

    @Bean
    public ZookeeperTemplate zookeeperTemplate(RpcServiceConfig rpcServiceConfig) throws Exception {
        ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
        RpcServiceConfig.Zookeeper zookeeper = rpcServiceConfig.getZookeeper();
        if(zookeeper == null) {
            logger.error("zookeeper config not set");
        } else {
            zookeeperTemplate.connect(rpcServiceConfig.getZookeeper().getRegisterAddress());
        }
        return zookeeperTemplate;
    }

}
