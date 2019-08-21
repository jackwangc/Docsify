import java.util.ArrayList;

/**
 * 丑数
 * 把只包含质因子2、3和5的数称作丑数（Ugly Number）。
 * 例如6、8都是丑数，但14不是，因为它包含质因子7。 
 * 习惯上我们把1当做是第一个丑数。求按从小到大的顺序的第N个丑数。
 */
public class Solution {
    public int GetUglyNumber_Solution(int index) {
        int[] dp = new int[index];
        dp[0] = 1;
        int i2 = 0, i3 =  0, i5 = 0;
        for (int i = 0; i < index; i++) {
            int min = Math.min(dp[i2] * 2, Math.min(dp[i3] * 3, dp[i5] * 5));
            if (min == dp[i2] * 2) {
                i2++;
            }
            if (min == dp[i3] * 3) {
                i3++;
            }
            if (min == dp[i5] * 5) {
                i5++;
            }
            dp[i] = min;
        }
        return dp[n - 1];

    }
}