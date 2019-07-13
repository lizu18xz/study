package com.fayayo.study.cpuLoad;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2019/7/12.
 * @version v1.0
 * @desc Cpu负载 demo
 */
@Slf4j
public class CpuLoad {


    public static void cpuLoad(String data){

        //解析一个文件   [{name:abc,age:20,sex:0}#{name:abc,age:20,sex:0}#{name:abc,age:20,sex:0}]

        //手动解析
        int leftBracket = data.indexOf("[");
        int rightBracket= data.indexOf("]");

        String dataStr=data.substring(leftBracket+1,rightBracket);

        if(dataStr == null || dataStr.length() <= 0){
            return;
        }

        System.out.println(dataStr);


        while(dataStr!=null && dataStr.length() > 0){

            int idpos = dataStr.indexOf("name");
            if(idpos < 0){
                break;
            }
            int colonpos = dataStr.indexOf(":",idpos);
            int commapos = dataStr.indexOf(",",idpos);
            if(colonpos < 0 || commapos < 0){
                //partners = partners.substring(idpos+"partnerid".length());//1  注释掉之后会死循环!!! 测试cpu负载
                continue;
            }
            String name = dataStr.substring(colonpos+1,commapos);
            if(name == null || name.length() <= 0){
                //partners = partners.substring(idpos+"partnerid".length());//2
                continue;
            }

            dataStr = dataStr.substring(commapos);
        }

    }


    public static void main(String[] args) {

        CpuLoad.cpuLoad("[{name:abc,age:20,sex:0},{name:abc,age:20,sex:0},{name:abc,age:20,sex:0}]");

        //异常数据
        //CpuLoad.cpuLoad("[{name:,age:20,sex:0},{name:abc,age:20,sex:0},{name:abc,age:20,sex:0}]");

    }

}
