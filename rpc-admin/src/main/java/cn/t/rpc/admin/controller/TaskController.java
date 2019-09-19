package cn.t.rpc.admin.controller;

import cn.t.rpc.admin.service.TaskService;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 人物调度控制器
 * create: 2019-09-19 12:20
 * @author: yj
 **/
@RequestMapping("task")
@RestController
public class TaskController {

    private TaskService taskService;

    @PostMapping
    public void addOrUpdate(@RequestParam("interfaceName") String interfaceName, @RequestParam("method") String method, @RequestParam("interval") Integer interval) {
        taskService.addOrUpdateFixRateTask(interfaceName, method, interval);
    }

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
}
