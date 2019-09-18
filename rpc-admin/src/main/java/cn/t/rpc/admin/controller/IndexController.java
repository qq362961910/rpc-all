package cn.t.rpc.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description: 首页
 * create: 2019-09-17 19:44
 * @author: yj
 **/
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
