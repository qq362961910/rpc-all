package cn.t.rpc.core.service;

import cn.t.rpc.core.network.RpcServiceConfig;
import cn.t.rpc.core.network.SimpleDecoder;
import cn.t.rpc.core.network.SimpleEncoder;
import cn.t.rpc.core.network.SimpleHandler;
import cn.t.rpc.core.zookeeper.ZookeeperTemplate;
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

import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @description: 远程调用服务
 * create: 2019-09-17 09:51
 * @author: yj
 **/
public class RemoteService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteService.class);

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
                if(!nodes.contains("/rpc")) {
                    zookeeperTemplate.createNode("/rpc", interfaceName);
                    zookeeperTemplate.createNode("/rpc/services", interfaceName);
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

    public RemoteService(RpcServiceConfig rpcServerConfig) {
        this.rpcServerConfig = rpcServerConfig;
    }
}
