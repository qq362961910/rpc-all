package cn.t.rpc.core.network.analysis.impl;


import cn.t.rpc.core.network.analysis.AbstractMessageAnalyser;
import cn.t.rpc.core.network.msg.CallMethodResultMsg;
import cn.t.rpc.core.network.msg.InternalMsg;
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
public class MethodCallResultAnalyser extends AbstractMessageAnalyser<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(MethodCallResultAnalyser.class);

    private List<MsgType> msgTypeList = new ArrayList<>();

    @Override
    public List<MsgType> supportMessageTypeList() {
        return msgTypeList;
    }

    @Override
    public Object analyse(ByteBuf byteBuf) {
        long id = byteBuf.readLong();
        int length = byteBuf.readInt();
        Object result;
        if(length == 0) {
            result = InternalMsg.VOID;
        } else {
            byte[] resultBytes = new byte[length];
            byteBuf.readBytes(resultBytes);
            try (
                ByteArrayInputStream bis = new ByteArrayInputStream(resultBytes);
                ObjectInputStream ois = new ObjectInputStream(bis)
            ) {
                result = ois.readObject();
            } catch (Exception e) {
                logger.error("远程方法调用结果消息解析失败", e);
                result = null;
            }
        }
        CallMethodResultMsg callMethodResultMsg = new CallMethodResultMsg();
        callMethodResultMsg.setId(id);
        callMethodResultMsg.setResult(result);
        return callMethodResultMsg;
    }

    public MethodCallResultAnalyser() {
        msgTypeList.add(MsgType.METHOD_CALL_RESULT);
    }
}
