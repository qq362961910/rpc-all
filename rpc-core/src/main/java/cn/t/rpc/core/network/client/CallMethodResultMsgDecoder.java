package cn.t.rpc.core.network.client;

import cn.t.rpc.core.network.analysis.ClientSideMsgAnalyserManager;
import cn.t.rpc.core.network.analysis.MessageAnalyser;
import cn.t.rpc.core.network.msg.MsgType;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: call method result decoder
 * create: 2019-09-18 15:33
 * @author: yj
 **/
public class CallMethodResultMsgDecoder extends NettyTcpDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CallMethodResultMsgDecoder.class);

    private ClientSideMsgAnalyserManager clientSideMsgAnalyserManager = new ClientSideMsgAnalyserManager();

    @Override
    protected Object readMessage(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        if(byteBuf.readableBytes() > 5) {
            byte type = byteBuf.readByte();
            int length = byteBuf.readInt();
            if(byteBuf.readableBytes() >= length) {
                MsgType msgType = MsgType.getMsgType(type);
                MessageAnalyser<ByteBuf> messageAnalyser = clientSideMsgAnalyserManager.selectMessageAnalyser(msgType);
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
