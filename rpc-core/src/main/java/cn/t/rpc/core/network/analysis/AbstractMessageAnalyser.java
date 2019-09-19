package cn.t.rpc.core.network.analysis;

import cn.t.rpc.core.network.msg.MsgType;
import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class AbstractMessageAnalyser<In> implements MessageAnalyser<In> {

    @Override
    public final boolean support(MsgType type) {
        List<MsgType> msgTypeList = supportMessageTypeList();
        return !CollectionUtils.isEmpty(msgTypeList) && msgTypeList.contains(type);
    }

    public abstract List<MsgType> supportMessageTypeList();

}
