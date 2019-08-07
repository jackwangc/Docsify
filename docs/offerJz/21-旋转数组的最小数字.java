/**
 * 旋转数组的最小数字
 */class Solution{
     public int solution(int[] arr) {
         int len = arr.length;
         int start = 0;
         int end = len -1;
         while (pre <= end) {
            int mid = start + (start - mid) << 1; 
            if(arr[mid] <= arr[end]){
                end = mid;
            } else {
                pre = mid + 1;
            } 

         }
     }
 }