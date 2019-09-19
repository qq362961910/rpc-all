package cn.t.rpc.core.network.server;

import cn.t.rpc.core.network.msg.CallMethodMsg;
import cn.t.rpc.core.network.msg.CallMethodResultMsg;
import cn.t.rpc.core.service.RemoteServiceManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description: call method handler
 * create: 2019-09-18 17:32
 * @author: yj
 **/
public class CallMethodMsgHandler extends SimpleChannelInboundHandler<CallMethodMsg> {

    private static final Logger logger = LoggerFactory.getLogger(CallMethodMsgHandler.class);

    private RemoteServiceManager remoteServiceManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CallMethodMsg msg) {
        logger.info("收到远程调用请求， 接口: {}, 方法: {}", msg.getInterfaceName(), msg.getMethodName());
        Object ref = remoteServiceManager.getRefByInterfaceName(msg.getInterfaceName());
        if(ref == null) {
            logger.error("未找到注册的服务类: {}", msg.getInterfaceName());
            //todo 响应客户端: 未找到注册的服务类
        } else {
            try {
                Method method;
                Object result;
                if(msg.getArg() == null) {
                    method = ref.getClass().getDeclaredMethod(msg.getMethodName());
                    result = method.invoke(ref);
                } else {
                    method = ref.getClass().getDeclaredMethod(msg.getMethodName(), msg.getArg().getClass());
                    result = method.invoke(ref, msg.getArg());
                }
                CallMethodResultMsg callMethodResultMsg = new CallMethodResultMsg();
                callMethodResultMsg.setId(msg.getId());
                callMethodResultMsg.setResult(result);
                ctx.writeAndFlush(callMethodResultMsg);
            } catch (NoSuchMethodException e) {
                logger.error("未找到服务方法", e);
                //todo 响应客户端: 异常
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("服务方法调用失败", e);
                //todo 响应客户端: 异常
            }
        }
    }

    public CallMethodMsgHandler(RemoteServiceManager remoteServiceManager) {
        this.remoteServiceManager = remoteServiceManager;
    }
}
