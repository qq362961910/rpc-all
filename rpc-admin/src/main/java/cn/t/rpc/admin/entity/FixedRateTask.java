package cn.t.rpc.admin.entity;

/**
 * @description: task
 * create: 2019-09-19 12:26
 * @author: yj
 **/
public class FixedRateTask {
    private String interfaceName;
    private int interval;
    private Runnable runnable;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
