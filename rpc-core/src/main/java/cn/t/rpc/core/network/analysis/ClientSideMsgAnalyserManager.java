package cn.t.rpc.core.network.analysis;

import cn.t.rpc.core.network.analysis.impl.MethodCallResultAnalyser;
import io.netty.buffer.ByteBuf;

public class ClientSideMsgAnalyserManager extends AbstractMessageAnalyserManager<ByteBuf> {
    public ClientSideMsgAnalyserManager() {
        addMessageAnalyser(new MethodCallResultAnalyser());
    }
}
