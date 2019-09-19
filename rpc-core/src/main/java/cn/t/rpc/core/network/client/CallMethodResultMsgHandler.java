package cn.t.rpc.core.network.client;

import cn.t.rpc.core.network.msg.CallMethodResultMsg;
import cn.t.rpc.core.util.RpcServiceUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: result handler
 * create: 2019-09-18 16:08
 * @author: yj
 **/
public class CallMethodResultMsgHandler extends SimpleChannelInboundHandler<CallMethodResultMsg> {

    private static final Logger logger = LoggerFactory.getLogger(CallMethodResultMsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CallMethodResultMsg msg) {
        logger.info("{}", msg);
        RpcServiceUtil.setRequestResult(msg.getId(), msg.getResult());
        logger.info("存储响应结果: {}", RpcServiceUtil.getRequestResult(msg.getId()));
    }
}
