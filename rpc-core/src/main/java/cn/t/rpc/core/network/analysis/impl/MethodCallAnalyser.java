package cn.t.rpc.core.network.analysis.impl;


import cn.t.rpc.core.network.analysis.AbstractMessageAnalyser;
import cn.t.rpc.core.network.msg.CallMethodMsg;
import cn.t.rpc.core.network.msg.MsgType;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * netty common message server
 * */
public class MethodCallAnalyser extends AbstractMessageAnalyser<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(MethodCallAnalyser.class);

    private List<MsgType> msgTypeList = new ArrayList<>();

    @Override
    public List<MsgType> supportMessageTypeList() {
        return msgTypeList;
    }

    @Override
    public Object analyse(ByteBuf byteBuf) {
        //id
        long id = byteBuf.readLong();
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
                logger.error("远程方法调用消息解析失败", e);
                return null;
            }
        }
        CallMethodMsg callMethodMsg = new CallMethodMsg();
        callMethodMsg.setId(id);
        callMethodMsg.setInterfaceName(new String(interfaceNameBytes));
        callMethodMsg.setMethodName(new String(methodNameBytes));
        callMethodMsg.setArg(arg);
        return callMethodMsg;
    }

    public MethodCallAnalyser() {
        msgTypeList.add(MsgType.METHOD_CALL);
    }
}
