package com.fayayo.study.threadlocal;

import java.util.HashSet;

/**
 * @author dalizu on 2020/2/17.
 * @version v1.0
 * @desc
 */
public class StartController {

    static HashSet<Val<Integer>> set = new HashSet<>();

    synchronized static void addSet(Val<Integer> v) {
        set.add(v);
    }

    /**
     * 函数式方法
     */
    static ThreadLocal<Val<Integer>> c = ThreadLocal.withInitial(() -> {
        Val<Integer> v = new Val<>();
        v.set(0);
        addSet(v);
        return v;
    });


    void __add() throws InterruptedException {
        Thread.sleep(100);
        Val<Integer> v = c.get();
        v.set(v.get() + 1);
    }

    public Integer stat() {
        System.out.println(set.size());
        for (Val<Integer> integerVal : set) {
            System.out.println(integerVal.get());
        }
        return set.stream().map(x -> x.get()).reduce((a, x) -> a + x).get();
    }

    public Integer add() throws InterruptedException {
        __add();
        return 1;
    }

}
