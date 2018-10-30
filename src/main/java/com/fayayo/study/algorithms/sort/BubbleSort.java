package com.fayayo.study.algorithms.sort;

/**
 * @author dalizu on 2018/10/29.
 * @version v1.0
 * @desc 冒泡排序
 */
public class BubbleSort {


    private BubbleSort() {

    }


    public static void sort(Comparable[] arr) {

        int n = arr.length;//总长度

        boolean swapped = false;

        do {
            swapped = false;

            for (int i = 1; i < n; i++) {

                if (arr[i - 1].compareTo(arr[i]) > 0) {
                    swap(arr, i - 1, i);
                    swapped = true;
                }
            }

            n--;
        } while (swapped);


    }


    private static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {

        Integer[] arr = {10,9,8,7,6,5,4,3,2,1,11};

        BubbleSort.sort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();

    }


}
