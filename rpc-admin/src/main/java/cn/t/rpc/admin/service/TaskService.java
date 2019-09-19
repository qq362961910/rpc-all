package cn.t.rpc.admin.service;

public interface TaskService {
    void addOrUpdateFixRateTask(String interfaceName, String method, int interval);
}
