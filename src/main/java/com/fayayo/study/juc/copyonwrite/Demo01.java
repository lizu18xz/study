package com.fayayo.study.juc.copyonwrite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author dalizu on 2020/2/26.
 * @version v1.0
 * @desc
 */
public class Demo01 {


    public static void main(String[] args) {


        //不能读取时候进行修改
        //ArrayList <String>list=new ArrayList<>();


        //可以修改，但是迭代看不到修改的东西
        CopyOnWriteArrayList<String>list=new CopyOnWriteArrayList<>();


        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Iterator<String>iterator=list.iterator();

        while (iterator.hasNext()){

            System.out.println("list is"+list);
            String next=iterator.next();
            System.out.println(next);

            if(next.equals("2")){
                list.remove("5");
            }

            if(next.equals("3")){
                list.add("3 found");
            }

        }



    }




}
