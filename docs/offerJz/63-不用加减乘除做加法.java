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
}