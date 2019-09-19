package cn.t.rpc.core.network.analysis;

import cn.t.rpc.core.network.msg.MsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息解析器Manager抽象类
 */
public abstract class AbstractMessageAnalyserManager<In> implements MessageAnalyserManager<In> {

    private final List<MessageAnalyser<In>> messageAnalyserList = new ArrayList<>();

    @Override
    public final MessageAnalyser<In> selectMessageAnalyser(MsgType type) {
        for (MessageAnalyser<In> analyser : messageAnalyserList) {
            if (analyser.support(type)) {
                return analyser;
            }
        }
        return null;
    }

    @Override
    public void addMessageAnalyser(MessageAnalyser<In> messageAnalyser) {
        if(messageAnalyser != null) {
            messageAnalyserList.add(messageAnalyser);
        }
    }
}
