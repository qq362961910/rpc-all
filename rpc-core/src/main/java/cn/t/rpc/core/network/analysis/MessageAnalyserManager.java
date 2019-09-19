package cn.t.rpc.core.network.analysis;


import cn.t.rpc.core.network.msg.MsgType;

public interface MessageAnalyserManager<In> {

    MessageAnalyser selectMessageAnalyser(MsgType type);

    void addMessageAnalyser(MessageAnalyser<In> messageAnalyser);
}
