/**
 * 整数中 1 出现的次数
 * 1.
 * 个位数上，1会每隔10出现一次
 * 十位数上出现1的情况应该是10-19，依然沿用分析个位数时候的阶梯理论，我们知道10-19这组数，每隔100出现一次
 * (n/diviver) * i
 * 2. 
 * 露出来的部分
 * 如果在10-19之间的，我们计算结果应该是k - 10 + 1
 * 大于 20 ，只有 10 个
 * 
 * Math.min(Math.max(n % diviver - i + 1, 0), i)
 */
 class Solution {
    public int NumberOf1Between1AndN_Solution(int n) {
        if( n <= 0)
            return 0;
        int count = 0;
        for (int i = 1; i < n; i*= 10) {
            long diviver = i * 10;
            count += (n/diviver) * i + Math.min(Math.max(n % diviver - i + 1, 0), i); 
        }
        return count;
    }
}