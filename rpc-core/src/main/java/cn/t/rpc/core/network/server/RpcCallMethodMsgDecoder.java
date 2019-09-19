package cn.t.rpc.core.network.server;

import cn.t.rpc.core.network.analysis.MessageAnalyser;
import cn.t.rpc.core.network.analysis.ServerSideMsgAnalyserManager;
import cn.t.rpc.core.network.msg.MsgType;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: demo解码器
 * create: 2019-09-17 11:16
 * @author: yj
 **/
public class RpcCallMethodMsgDecoder extends NettyTcpDecoder {

    private static Logger logger = LoggerFactory.getLogger(RpcCallMethodMsgDecoder.class);

    private ServerSideMsgAnalyserManager serverSideMsgAnalyserManager = new ServerSideMsgAnalyserManager();

    @Override
    protected Object readMessage(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        if(byteBuf.readableBytes() > 5) {
            byte type = byteBuf.readByte();
            int length = byteBuf.readInt();
            if(byteBuf.readableBytes() >= length) {
                MsgType msgType = MsgType.getMsgType(type);
                MessageAnalyser<ByteBuf> messageAnalyser = serverSideMsgAnalyserManager.selectMessageAnalyser(msgType);
                if(messageAnalyser != null) {
                    return messageAnalyser.analyse(byteBuf);
                } else {
                    logger.error("未实现解析的消息类型: " + type);
                    ctx.close();
                }
            }
        }
        return null;
    }
}
