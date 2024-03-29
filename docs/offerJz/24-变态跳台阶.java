/**
 * 跳台阶
 * 变态跳台阶
 * 先找出递推公式
 * f(n) = f(n-1) * 2
 */
class Solution {
    public int JumpFloorII(int target) {
        int[] dp = new int[target];
        Arrays.fill(dp, 1);
        for (int i = 1; i < target; i++)
            for (int j = 0; j < i; j++)
                dp[i] += dp[j];
        return dp[target - 1];
    }
    public int jump(int target) {
        return (int) Math.pow(2, target - 1);
    }
}
