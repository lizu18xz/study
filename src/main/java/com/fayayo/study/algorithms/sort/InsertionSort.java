package com.fayayo.study.algorithms.sort;

/**
 * @author dalizu on 2018/10/26.
 * @version v1.0
 * @desc 插入排序
 */
public class InsertionSort {


    private InsertionSort(){}


    public static void sort(Comparable[] arr){

        int n = arr.length;
        for (int i = 0; i < n; i++) {

            // 寻找元素arr[i]合适的插入位置

            // 写法1
//            for( int j = i ; j > 0 ; j -- )
//                if( arr[j].compareTo( arr[j-1] ) < 0 )
//                    swap( arr, j , j-1 );
//                else
//                    break;

            // 写法2
            for( int j = i; j > 0 && arr[j].compareTo(arr[j-1]) < 0 ; j--)
                swap(arr, j, j-1);

        }
    }

    private static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }




    public Object[] sort(Object[] array, int len) {
            int j;
            int temp = 0;
            for (int p = 1; p < len; p++) {
                temp = (int) array[p];
                for (j = p; j > 0 && temp < (int) array[j - 1]; j--) {
                    array[j] = array[j - 1];
                }
                array[j] = temp;
            }
            return array;

    }

    public Object[] sort1(Object[] array, int len) {


        for (int i=0;i<len;i++){

            for (int j=i;j>0;i--){
                if((int)array[j]<(int)array[j-1]){//后面的小于前面的，前移  132  123
                    //swap
                    int tmp=(int)array[j];
                    array[j]=array[j-1];
                    array[j-1]=tmp;

                }else {
                    break;//退出该循环
                }
            }
        }
        return array;
    }




    public static void main(String[] args) {

        Integer[] arr = {10,9,8,7,6,5,4,3,2,1,11};
        InsertionSort.sort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
    }

}
