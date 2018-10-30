package com.fayayo.study.algorithms.sort;

/**
 * @author dalizu on 2018/10/26.
 * @version v1.0
 * @desc 泛型+comparable
 */
public class SelectionSortComparable {


    private SelectionSortComparable(){}


    public static void sort(Comparable []arr){

        int n=arr.length;
        for (int i=0;i<n;i++){
            // 寻找[i, n)区间里的最小值的索引
            int minIndex=i;
            for(int j=i+1;j<n;j++){
                // 使用compareTo方法比较两个Comparable对象的大小
                if(arr[j].compareTo(arr[minIndex])<0){
                    minIndex=j;
                }
            }

            swap(arr,i,minIndex);//替换最小值
        }

    }

    public static void swap(Comparable []arr,int i,int j){
        Comparable t=arr[i];
        arr[i]=arr[j];
        arr[j]=t;
    }

    public static void main(String[] args) {

        // 测试Integer
        Integer[] a = {10,9,8,7,6,5,4,3,2,1};
        SelectionSortComparable.sort(a);
        for( int i = 0 ; i < a.length ; i ++ ){
            System.out.print(a[i]);
            System.out.print(' ');
        }
        System.out.println();

        // 测试Double
        Double[] b = {4.4, 3.3, 2.2, 1.1};
        SelectionSortComparable.sort(b);
        for( int i = 0 ; i < b.length ; i ++ ){
            System.out.print(b[i]);
            System.out.print(' ');
        }
        System.out.println();

        // 测试String
        String[] c = {"D", "C", "B", "A"};
        SelectionSortComparable.sort(c);
        for( int i = 0 ; i < c.length ; i ++ ){
            System.out.print(c[i]);
            System.out.print(' ');
        }
        System.out.println();


        // 测试自定义的类 Student
        Student[] d = new Student[4];
        d[0] = new Student("D",90);
        d[1] = new Student("C",100);
        d[2] = new Student("B",95);
        d[3] = new Student("A",95);
        SelectionSortComparable.sort(d);
        for( int i = 0 ; i < d.length ; i ++ )
            System.out.println(d[i]);

    }

}
