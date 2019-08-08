/**
 * 给出两个整数a和b, 求他们的和, 但不能使用 + 等数学运算符。
 * 位运算
 */
class Solution {
    public int soultion(int a, int b) {
        
        while (b != 0) {
            int _a = a ^ b; // ^ 不进位加法
            int _b = (a & b) << 1; // 进位之后的结果，(当 ab 都为 1 的时候进位)
            // a + b = (a ^ b) + (a & b) << 1;
            a = _a;
            b = _b;
        }
        return a;
    }
    /**
     * 二进制中 1 的个数
     */
    public int NumberOf1(int n) {
        int cnt = 0;
        while (n != 0) {
            cnt++;
            // n&(n-1)
            // 该位运算去除 n 的位级表示中最低的那一位
            n &= (n - 1);
        }
        return cnt;
    }
}


