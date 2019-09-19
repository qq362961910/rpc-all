package cn.t.rpc.core.network.server;

import cn.t.rpc.core.network.msg.CallMethodResultMsg;
import cn.t.rpc.core.network.msg.MsgType;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @description: call method result encoder
 * create: 2019-09-18 15:26
 * @author: yj
 **/
public class CallMethodResultMsgEncoder extends NettyTcpEncoder<CallMethodResultMsg> {

    private static final Logger logger = LoggerFactory.getLogger(CallMethodResultMsgEncoder.class);

    @Override
    protected void doEncode(CallMethodResultMsg msg, ByteBuf byteBuf) {
        //type
        byteBuf.writeByte(MsgType.METHOD_CALL_RESULT.value);
        //length
        int lengthIndex = byteBuf.writerIndex();
        byteBuf.writeZero(4);
        //id
        byteBuf.writeLong(msg.getId());
        //result
        if(msg.getResult() == null) {
            byteBuf.writeInt(0);
        } else {
            try (
                ByteArrayOutputStream bis = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bis)
            ) {
                oos.writeObject(msg.getResult());
                byte[] resultBytes = bis.toByteArray();
                byteBuf.writeInt(resultBytes.length);
                byteBuf.writeBytes(resultBytes);
            } catch (IOException e) {
                logger.error("调用远程方法失败", e);
            }
        }
        int length = byteBuf.writerIndex() - lengthIndex - 4;
        byteBuf.setInt(lengthIndex, length);
    }
}
