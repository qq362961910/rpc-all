package cn.t.rpc.core.network.analysis;

import cn.t.rpc.core.network.msg.MsgType;

public interface MessageAnalyser<In> {

    boolean support(MsgType type);

    Object analyse(In t);

}
