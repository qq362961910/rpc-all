package cn.t.rpc.core.network.msg;

/**
 * @description: rpc内部消息
 * create: 2019-09-19 11:22
 * @author: yj
 **/
public enum  InternalMsg {

    VOID(-1, null);

    public final long msgId;
    public final Object result;

    InternalMsg(long msgId, Object result) {
        this.msgId = msgId;
        this.result = result;
    }
}
