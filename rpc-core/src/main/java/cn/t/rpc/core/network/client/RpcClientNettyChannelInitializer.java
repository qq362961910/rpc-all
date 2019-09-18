package cn.t.rpc.core.network.client;

import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @description: rpc client initializer
 * create: 2019-09-18 15:22
 * @author: yj
 **/
public class RpcClientNettyChannelInitializer extends NettyChannelInitializer {

    @Override
    protected void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(new RpcCallMethodMsgEncoder());
        channelPipeline.addLast(new CallMethodResultMsgDecoder());
        channelPipeline.addLast(new CallMethodResultMsgHandler());
    }

    public RpcClientNettyChannelInitializer() {
        super(180, 180, 180, null, null);
    }
}
