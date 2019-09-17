package cn.t.rpc.core.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: demo消息处理器
 * create: 2019-09-17 11:44
 * @author: yj
 **/
public class SimpleHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("收到消息: " + s);
    }
}
