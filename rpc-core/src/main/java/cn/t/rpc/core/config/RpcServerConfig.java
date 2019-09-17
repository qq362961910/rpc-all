package cn.t.rpc.core.config;

import cn.t.rpc.core.network.RemoteServiceServerConfig;
import cn.t.rpc.core.spring.ServiceAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Rpc服务配置
 * create: 2019-09-17 11:47
 * @author: yj
 **/
@EnableConfigurationProperties(RemoteServiceServerConfig.class)
@Configuration
public class RpcServerConfig {

    @Bean
    public ServiceAnnotationBeanPostProcessor rpcServiceClassPathBeanDefinitionScanner() {
        return new ServiceAnnotationBeanPostProcessor();
    }


}
