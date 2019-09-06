/**
 * 数组在排序数组中出现的次数
 * 看见有序，进行二分查找
 */
class Solution {
    public int GetNumberOfK(int[] nums, int K) {
        int first = binarySearch(nums, K);
        int last = binarySearch(nums, K + 1);
        return (first == nums.length || nums[first] != K) ? 0 : last - first;
    }
    
    private int binarySearch(int[] nums, int K) {
        int l = 0, h = nums.length;
        // 一直循环找，直到 l=h, 返回 k 的第一个位置
        while (l < h) {
            int m = l + (h - l) / 2;
            if (nums[m] >= K)
                h = m;
            else
                l = m + 1;
        }
        return l;
    }
}