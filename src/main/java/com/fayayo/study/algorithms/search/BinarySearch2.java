package com.fayayo.study.algorithms.search;

/**
 * @author dalizu on 2018/10/29.
 * @version v1.0
 * @desc 二分查找 递归写法
 */
public class BinarySearch2 {

    private BinarySearch2(){}


    public static int find(Comparable arr[],Comparable target){


        return find(arr,0,arr.length-1,target);
    }

    private static int find(Comparable[] arr, int l, int r, Comparable target) {

        if(l>r){
            return -1;
        }

        //int mid = (l + r)/2;
        // 防止极端情况下的整形溢出，使用下面的逻辑求出mid
        int mid=l+(r-l)/2;

        if(arr[mid].compareTo(target) == 0){
            return mid;
        }


        if(arr[mid].compareTo(target) > 0){
            return find(arr,l,mid-1,target);
        }else {
            return find(arr,mid+1,r,target);
        }
    }


    public static void main(String[] args) {

        Integer a[]={1,2,4,6,7,9,22,44,55};


        System.out.println(BinarySearch2.find(a,44));


    }
}
