package cn.t.rpc.core.network.client;

import cn.t.rpc.core.network.msg.CallMethodMsg;
import cn.t.rpc.core.network.msg.MsgType;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @description: demo编码器
 * create: 2019-09-17 11:39
 * @author: yj
 **/
public class RpcCallMethodMsgEncoder extends NettyTcpEncoder<CallMethodMsg> {

    private static final Logger logger = LoggerFactory.getLogger(RpcCallMethodMsgEncoder.class);

    @Override
    protected void doEncode(CallMethodMsg msg, ByteBuf byteBuf) {
        byteBuf.markWriterIndex();
        //type
        byteBuf.writeByte(MsgType.METHOD_CALL.value);
        //length
        int lengthIndex = byteBuf.writerIndex();
        byteBuf.writeZero(4);
        //id
        byteBuf.writeLong(msg.getId());
        //interface
        byte[] interfaceNameBytes = msg.getInterfaceName().getBytes();
        byteBuf.writeShort(interfaceNameBytes.length);
        byteBuf.writeBytes(interfaceNameBytes);
        //method
        byte[] methodNameBytes = msg.getMethodName().getBytes();
        byteBuf.writeShort(methodNameBytes.length);
        byteBuf.writeBytes(methodNameBytes);
        //arg
        if(msg.getArg() == null) {
            byteBuf.writeInt(0);
        } else {
            try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream)
            ) {
                oos.writeObject(msg.getArg());
                byte[] argBytes = byteArrayOutputStream.toByteArray();
                byteBuf.writeInt(argBytes.length);
                byteBuf.writeBytes(argBytes);
            } catch (IOException e) {
                logger.error("调用远程方法失败", e);
                byteBuf.resetWriterIndex();
            }
        }
        int length = byteBuf.writerIndex() - lengthIndex - 4;
        byteBuf.setInt(lengthIndex, length);
    }
}
