package cn.t.rpc.admin.dao;

import cn.t.rpc.admin.entity.FixedRateTask;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: task repo
 * create: 2019-09-19 12:26
 * @author: yj
 **/
@Component
public class TaskManager {

    private final HashedWheelTimer timer = new HashedWheelTimer();
    private Map<String, FixedRateTask> FIXED_RATE_TASK = new HashMap<>();

    public void addOrUpdateFixRateTask(FixedRateTask task) {
        FixedRateTask taskOld = FIXED_RATE_TASK.get(task.getInterfaceName());
        if(taskOld == null) {
            timer.newTimeout(new TimerTask() {
                @Override
                public void run(Timeout timeout) {
                    task.getRunnable().run();
                    if(FIXED_RATE_TASK.containsKey(task.getInterfaceName())) {
                        timer.newTimeout(this, task.getInterval(), TimeUnit.SECONDS);
                    }
                }
            }, task.getInterval(), TimeUnit.SECONDS);
            FIXED_RATE_TASK.put(task.getInterfaceName(), task);
        } else {
            taskOld.setInterval(task.getInterval());
        }
    }
}


