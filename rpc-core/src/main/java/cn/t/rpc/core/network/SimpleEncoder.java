package cn.t.rpc.core.network;

import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;

/**
 * @description: demo编码器
 * create: 2019-09-17 11:39
 * @author: yj
 **/
public class SimpleEncoder extends NettyTcpEncoder<String> {

    @Override
    protected void doEncode(String msg, ByteBuf out) {
        out.writeBytes(msg.getBytes());
    }
}
