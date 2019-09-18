package cn.t.rpc.core.network.server;

import cn.t.rpc.core.network.msg.CallMethodMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: call method handler
 * create: 2019-09-18 17:32
 * @author: yj
 **/
public class CallMethodMsgHandler extends SimpleChannelInboundHandler<CallMethodMsg> {

    private static final Logger logger = LoggerFactory.getLogger(CallMethodMsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CallMethodMsg msg) {
        logger.info("收到远程调用请求， 接口: {}, 方法: {}", msg.getInterfaceName(), msg.getMethodName());
    }
}
