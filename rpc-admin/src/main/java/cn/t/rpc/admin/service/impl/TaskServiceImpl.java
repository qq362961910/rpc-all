package cn.t.rpc.admin.service.impl;

import cn.t.rpc.admin.schedule.TaskManager;
import cn.t.rpc.admin.entity.FixedRateTask;
import cn.t.rpc.admin.service.TaskService;
import cn.t.rpc.core.network.client.RpcClient;
import cn.t.rpc.core.network.msg.CallMethodMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description: task service
 * create: 2019-09-19 12:25
 * @author: yj
 **/
@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private TaskManager taskManager;
    private RpcClient rpcClient;

    @Override
    public void addOrUpdateFixRateTask(String interfaceName, String methodName, int interval) {
        FixedRateTask fixedRateTask = new FixedRateTask();
        fixedRateTask.setInterfaceName(interfaceName);
        fixedRateTask.setInterval(interval);
        fixedRateTask.setRunnable(() -> {
            try {
                CallMethodMsg callMethodMsg = new CallMethodMsg();
                callMethodMsg.setId(System.currentTimeMillis());
                callMethodMsg.setInterfaceName(interfaceName);
                callMethodMsg.setMethodName(methodName);
                callMethodMsg.setArg(null);
                rpcClient.callMethod(callMethodMsg);
            } catch (Exception e) {
                logger.error("任务执行失败", e);
            }
        });
        taskManager.addOrUpdateFixRateTask(fixedRateTask);
    }


    public TaskServiceImpl(TaskManager taskManager, RpcClient rpcClient) {
        this.taskManager = taskManager;
        this.rpcClient = rpcClient;
    }
}
