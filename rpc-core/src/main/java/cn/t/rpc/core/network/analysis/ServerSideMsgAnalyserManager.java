package cn.t.rpc.core.network.analysis;

import cn.t.rpc.core.network.analysis.impl.MethodCallAnalyser;
import io.netty.buffer.ByteBuf;

public class ServerSideMsgAnalyserManager extends AbstractMessageAnalyserManager<ByteBuf> {
    public ServerSideMsgAnalyserManager() {
        addMessageAnalyser(new MethodCallAnalyser());
    }
}
