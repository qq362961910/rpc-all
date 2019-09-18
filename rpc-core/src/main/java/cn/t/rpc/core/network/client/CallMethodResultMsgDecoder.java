package cn.t.rpc.core.network.client;

import cn.t.rpc.core.network.msg.CallMethodResultMsg;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @description: call method result decoder
 * create: 2019-09-18 15:33
 * @author: yj
 **/
public class CallMethodResultMsgDecoder extends NettyTcpDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CallMethodResultMsgDecoder.class);

    @Override
    protected Object readMessage(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        if(byteBuf.readableBytes() >= length) {
            byte[] resultBytes = new byte[length];
            byteBuf.readBytes(resultBytes);
            Object result;
            try (
                ByteArrayInputStream bis = new ByteArrayInputStream(resultBytes);
                ObjectInputStream ois = new ObjectInputStream(bis)
            ) {
                result = ois.readObject();
                CallMethodResultMsg callMethodResultMsg = new CallMethodResultMsg();
                callMethodResultMsg.setResult(result);
                return callMethodResultMsg;
            } catch (Exception e) {
                logger.error("远程调用结果消息解析失败", e);
            }
        }
        return null;
    }
}
