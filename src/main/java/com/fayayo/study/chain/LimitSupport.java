package com.fayayo.study.chain;

/**
 * @author dalizu on 2019/3/15.
 * @version v1.0
 * @desc
 */
public class LimitSupport extends Support{

    private int limit;

    public LimitSupport(String name,int limit) {
        super(name);
        this.limit=limit;
    }

    @Override
    protected boolean resolve(Trouble trouble) {
        System.out.println(name);
        return trouble.getNumber()<=limit? true : false;
    }

}
