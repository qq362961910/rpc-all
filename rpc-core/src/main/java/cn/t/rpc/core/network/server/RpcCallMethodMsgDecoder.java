package cn.t.rpc.core.network.server;

import cn.t.rpc.core.network.msg.CallMethodMsg;
import cn.t.rpc.core.network.msg.MsgType;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @description: demo解码器
 * create: 2019-09-17 11:16
 * @author: yj
 **/
public class RpcCallMethodMsgDecoder extends NettyTcpDecoder {

    private static Logger logger = LoggerFactory.getLogger(RpcCallMethodMsgDecoder.class);

    @Override
    protected Object readMessage(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        if(byteBuf.readableBytes() > 5) {
            byte type = byteBuf.readByte();
            MsgType msgType = MsgType.getMsgType(type);
            if(MsgType.METHOD_CALL == msgType) {
                //length
                int length = byteBuf.readInt();
                if(byteBuf.readableBytes() >= length) {
                    //interface
                    short interfaceNameBytesLength = byteBuf.readShort();
                    byte[] interfaceNameBytes = new byte[interfaceNameBytesLength];
                    byteBuf.readBytes(interfaceNameBytes);
                    //method
                    short methodNameBytesLength = byteBuf.readShort();
                    byte[] methodNameBytes = new byte[methodNameBytesLength];
                    byteBuf.readBytes(methodNameBytes);
                    //arg
                    int argBytesLength = byteBuf.readInt();
                    Object arg;
                    if(argBytesLength == 0) {
                        arg = null;
                    } else {
                        byte[] argBytes = new byte[argBytesLength];
                        byteBuf.readBytes(argBytes);
                        try (
                            ByteArrayInputStream bis = new ByteArrayInputStream(argBytes);
                            ObjectInputStream ois = new ObjectInputStream(bis)
                        ) {
                            arg = ois.readObject();
                        } catch (Exception e) {
                            logger.error("远程调用消息解析失败", e);
                            return null;
                        }
                    }
                    CallMethodMsg callMethodMsg = new CallMethodMsg();
                    callMethodMsg.setInterfaceName(new String(interfaceNameBytes));
                    callMethodMsg.setMethodName(new String(methodNameBytes));
                    callMethodMsg.setArg(arg);
                    return callMethodMsg;
                }
            } else {
                logger.error("未实现解析的消息类型: " + type);
                ctx.close();
            }
        }
        return null;
    }
}
