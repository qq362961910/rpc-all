package cn.t.rpc.core.network;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: demo解码器
 * create: 2019-09-17 11:16
 * @author: yj
 **/
public class SimpleDecoder extends NettyTcpDecoder {

    private static Logger logger = LoggerFactory.getLogger(SimpleDecoder.class);

    @Override
    protected Object readMessage(ChannelHandlerContext ctx, ByteBuf in) {
        return in.toString();
    }
}
