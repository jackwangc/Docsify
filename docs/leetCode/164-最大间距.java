/**
 * 找到给定未排序数组的最大差值
 * 桶排序 -- 计数排序
 * 1. 先找到最大值
 * 2. 找到最小值
 * 3. 生成 max-min 个桶
 * 4. 往桶中添加数据
 * 5. 统计空桶的数量
 */
class Solution {
    public int solution(int[] arr) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (min >= arr[i]) {
                min = arr[i];
            }
            if (max <= arr[i]) {
                max = arr[i];
            }
        }
        int len = max - min;
        int[] numArr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            numArr[arr[i]-min]++;
        }
        int count = 0;
        int re = 0;
        for (int i = 0; i < len; i++) {
            if (numArr[i] == 0) {
                count++;
            }else{
                re = Math.max(re, count);
                count = 0;
            }
        }
        return max+1;

    }
}