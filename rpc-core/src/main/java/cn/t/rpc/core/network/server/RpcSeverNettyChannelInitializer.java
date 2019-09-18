package cn.t.rpc.core.network.server;

import cn.t.rpc.core.service.RemoteServiceManager;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @description: rpc client initializer
 * create: 2019-09-18 15:22
 * @author: yj
 **/
public class RpcSeverNettyChannelInitializer extends NettyChannelInitializer {

    private RemoteServiceManager remoteServiceManager;

    @Override
    protected void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(new CallMethodResultMsgEncoder());
        channelPipeline.addLast(new RpcCallMethodMsgDecoder());
        channelPipeline.addLast(new CallMethodMsgHandler(remoteServiceManager));
    }

    public RpcSeverNettyChannelInitializer() {
        super(180, 180, 180, null, null);
    }

    public RpcSeverNettyChannelInitializer(RemoteServiceManager remoteServiceManager) {
        super(180, 180, 180, null, null);
        this.remoteServiceManager = remoteServiceManager;
    }

}
