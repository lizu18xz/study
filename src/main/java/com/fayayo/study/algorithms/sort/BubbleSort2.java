package com.fayayo.study.algorithms.sort;

/**
 * @author dalizu on 2018/10/29.
 * @version v1.0
 * @desc
 */
public class BubbleSort2 {


    private BubbleSort2(){}

    public static void sort(Comparable[] arr){

        int n=arr.length;

        int newn;

        do {
            newn=0;

            for (int i=1;i<n;i++){
                if(arr[i-1].compareTo(arr[i])>0){

                    swap(arr,i-1,i);
                    // 记录最后一次的交换位置,在此之后的元素在下一轮扫描中均不考虑
                    newn=i;
                }

            }
            n=newn;
        }while (newn>0);

    }

    private static void swap(Object arr[],int i,int j){

        Object t=arr[i];
        arr[i]=arr[j];
        arr[j]=t;
    }


    public static void main(String[] args) {

        Integer[] arr = {10,9,8,7,6,5,4,3,2,1,11};

        BubbleSort2.sort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();

    }





}
