package cn.t.rpc.core.network.server;

import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @description: rpc client initializer
 * create: 2019-09-18 15:22
 * @author: yj
 **/
public class RpcSeverNettyChannelInitializer extends NettyChannelInitializer {

    @Override
    protected void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(new CallMethodResultMsgEncoder());
        channelPipeline.addLast(new RpcCallMethodMsgDecoder());
        channelPipeline.addLast(new CallMethodMsgHandler());
    }

    public RpcSeverNettyChannelInitializer() {
        super(180, 180, 180, null, null);
    }
}
