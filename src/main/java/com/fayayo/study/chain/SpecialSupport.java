package com.fayayo.study.chain;

/**
 * @author dalizu on 2019/3/15.
 * @version v1.0
 * @desc
 */
public class SpecialSupport extends Support{

    private int specialNumber;

    public SpecialSupport(String name, int specialNumber) {
        super(name);
        this.specialNumber=specialNumber;
    }

    @Override
    protected boolean resolve(Trouble trouble) {
        return trouble.getNumber()==specialNumber ? true : false;
    }

}
