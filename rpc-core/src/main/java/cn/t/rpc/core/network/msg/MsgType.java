package cn.t.rpc.core.network.msg;

public enum MsgType {

    METHOD_CALL((byte)1),

    METHOD_CALL_RESULT((byte)2)
    ;

    public final byte value;

    MsgType(byte type) {
        this.value = type;
    }

    public static MsgType getMsgType(byte value) {
        for(MsgType type: values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
