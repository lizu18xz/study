package com.fayayo.study.algorithms.search;

/**
 * @author dalizu on 2018/10/29.
 * @version v1.0
 * @desc 二分查找
 * 比较非递归和递归写法的二分查找的效率
   非递归算法在性能上有微弱优势
 */
public class BinarySearch {


    private BinarySearch(){}

    //1,3,5,7,8,9


    public static int find(Comparable [] arr,Comparable target){

        // 在arr[l...r]之中查找target
        int l=0,r=arr.length-1;

        while (l <= r){
            //int mid = (l + r)/2;
            // 防止极端情况下的整形溢出，使用下面的逻辑求出mid
            int mid=l+(r-l)/2;

            if(arr[mid].compareTo(target) == 0) {

                return mid;
            }


            if(arr[mid].compareTo(target) > 0){
                r=mid-1;
            }else {
                l=mid+1;
            }

        }

        return -1;
    }


    public static void main(String[] args) {
        int N = 1000000;
        Integer[] arr = new Integer[N];
        for(int i = 0 ; i < N ; i ++)
            arr[i] = new Integer(i);

        // 对于我们的待查找数组[0...N)
        // 对[0...N)区间的数值使用二分查找，最终结果应该就是数字本身
        // 对[N...2*N)区间的数值使用二分查找，因为这些数字不在arr中，结果为-1
        for(int i = 0 ; i < 2*N ; i ++) {
            int v = BinarySearch.find(arr, new Integer(i));
            if (i < N)
                assert v == i;
            else
                assert v == -1;
        }

        return;
    }


}
