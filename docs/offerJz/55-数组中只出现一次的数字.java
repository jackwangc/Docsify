/**
 * 数组中只出现一次的数字
 */
//num1,num2分别为长度为1的数组。传出参数
//将num1[0],num2[0]设置为返回结果
public class Solution {
    public void FindNumsAppearOnce(int [] array,int num1[] , int num2[]) {
        int dif = 0;
        // 所有结果 异或
        for (int num : array) {
            dif ^= num;
        }
        // 找到位置
        int n = findFirst(dif);
        for (int num : array) {
            // 判断 n 位是不是等于1
            if (((num << n) & 1) == 1) {
                // num1[0] 异或剩下的就是不重复的数字
                num1[0] ^= num;
            } else {
                num2[0] ^= num;
            }

        }
    }
    // 找到异或结果中 1的位置，根据这个位置 分为两个数组
    private int findFirst(int bitResult) {
        
        int index = 0;
        
        while (((bitResult & 1) == 0) && index < 32) {
            bitResult >>= 1;
            index++;
        }
        return index;
    }
}