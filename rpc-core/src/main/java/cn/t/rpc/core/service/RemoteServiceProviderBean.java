package cn.t.rpc.core.service;

import cn.t.rpc.core.network.RpcServiceConfig;
import cn.t.rpc.core.network.SimpleDecoder;
import cn.t.rpc.core.network.SimpleEncoder;
import cn.t.rpc.core.network.SimpleHandler;
import cn.t.rpc.core.zookeeper.ZookeeperTemplate;
import cn.t.util.common.JsonUtil;
import cn.t.util.common.StringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @description: 远程调用服务
 * create: 2019-09-17 09:51
 * @author: yj
 **/
public class RemoteServiceProviderBean implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceProviderBean.class);

    private String interfaceName;

    private Object ref;

    private RpcServiceConfig rpcServerConfig;

    private ZookeeperTemplate zookeeperTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        registerService();
    }

    private void registerService() {
        RpcServiceConfig.Server server = rpcServerConfig.getServer();
        if(server == null) {
            logger.warn("no rpc bind host and port");
        } else {
            ServerBootstrap bootstrap = new ServerBootstrap();
            EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
            EventLoopGroup workerGroup = new NioEventLoopGroup(Math.min(Runtime.getRuntime().availableProcessors() + 1, 32), new DefaultThreadFactory("NettyServerWorker", true));
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline()
                            .addLast("logging",new LoggingHandler(LogLevel.INFO))
                            .addLast("decoder", new SimpleDecoder())
                            .addLast("encoder", new SimpleEncoder())
                            .addLast("server-idle-handler", new IdleStateHandler(0, 0, 5000, MILLISECONDS))
                            .addLast("handler", new SimpleHandler());
                    }
                });
            // bind
            ChannelFuture channelFuture = bootstrap.bind(server.getHost(), server.getPort());
            channelFuture.syncUninterruptibly();
            channelFuture.addListener(f -> {
                List<String> nodes = zookeeperTemplate.getChildren("/");
                Set<RpcServiceConfig.Server> serviceHostSet = new HashSet<>();
                serviceHostSet.add(server);
                if(!nodes.contains("rpc")) {
                    zookeeperTemplate.createNode("/rpc", "");
                    zookeeperTemplate.createNode("/rpc/services", "");
                    zookeeperTemplate.createNode("/rpc/services/" + interfaceName, JsonUtil.serialize(serviceHostSet));
                } else {
                    List<String> serviceNodes = zookeeperTemplate.getChildren("/rpc");
                    if(!serviceNodes.contains("services")) {
                        zookeeperTemplate.createNode("/rpc/services", "");
                        zookeeperTemplate.createNode("/rpc/services/" + interfaceName, JsonUtil.serialize(serviceHostSet));
                    } else {
                        List<String> interfaceNodes = zookeeperTemplate.getChildren("/rpc/services");
                        if(!interfaceNodes.contains(interfaceName)) {
                            zookeeperTemplate.createNode("/rpc/services/" + interfaceName, JsonUtil.serialize(serviceHostSet));
                        } else {
                            String data = zookeeperTemplate.getData("/rpc/services/" + interfaceName);
                            if(StringUtil.isEmpty(data)) {
                                zookeeperTemplate.setData("/rpc/services/" + interfaceName, JsonUtil.serialize(serviceHostSet));
                            } else {
                                HashSet<RpcServiceConfig.Server> oldSet = JsonUtil.deserialize(data, HashSet.class);
                                oldSet.addAll(serviceHostSet);
                                zookeeperTemplate.setData("/rpc/services/" + interfaceName, JsonUtil.serialize(oldSet));
                            }
                        }
                    }
                }
            });
            channelFuture.channel().closeFuture().addListener(f -> {
                System.out.println("================================关闭==================================");
            });
            logger.info("rpc daemon service started, host: {}, port: {}", server.getHost(), server.getPort());
        }
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

    public ZookeeperTemplate getZookeeperTemplate() {
        return zookeeperTemplate;
    }

    public void setZookeeperTemplate(ZookeeperTemplate zookeeperTemplate) {
        this.zookeeperTemplate = zookeeperTemplate;
    }

    public RemoteServiceProviderBean(RpcServiceConfig rpcServerConfig) {
        this.rpcServerConfig = rpcServerConfig;
    }
}
