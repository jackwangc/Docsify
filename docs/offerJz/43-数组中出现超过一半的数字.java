/**
 * 数组中出现超过一半的数字
 * 如果有符合条件的数字，则它出现的次数比其他所有数字出现的次数和还要多
 */
public class Solution {
    public int MoreThanHalfNum_Solution(int [] array) {
        int major = nums[0];
        for (int i = 1, cnt = 1; i < nums.length; i++) {
            cnt = nums[i] == major ? cnt + 1 : cnt - 1;
            if (cnt == 0) {
                // 一是数组中一个数字，一是次数
                major = nums[i];
                cnt = 1;
            }
        }
        int cnt = 0;
        for (int var : nums) {
            if (var == major) {
                cnt++;
            }
        }
        return cnt > nums.length / 2 ? major : 0;
    }
}