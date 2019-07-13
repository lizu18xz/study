package com.fayayo.study.cpuLoad;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dalizu on 2019/7/12.
 * @version v1.0
 * @desc
 */
@RestController
@RequestMapping("/cpu")
public class CpuController implements InitializingBean{


    @RequestMapping("/view")
    public String view(){

        return"view";
    }

    @RequestMapping("/load")
    public void load(){

        CpuLoad.cpuLoad("[{name:abc,age:20,sex:0},{name:abc,age:20,sex:0},{name:abc,age:20,sex:0}]");

        //异常数据
        CpuLoad.cpuLoad("[{name:,age:20,sex:0},{name:abc,age:20,sex:0},{name:abc,age:20,sex:0}]");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }

}
