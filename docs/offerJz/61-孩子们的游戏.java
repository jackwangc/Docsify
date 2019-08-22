/**
 * 约瑟夫环问题
 * 对于一般问题
 * 出列编号 m%n-1
 * 起始编号 m % n
 * 找出递推公式
 * f[i]=(f[i-1]+m)%i;
 */
public class Solution {
    // 递归解法
    public int solution(int n, int m) {
        if (n == 0) {
            return -1;
        }
        if (n == 1) {
            return 0;
        }
        return (solution(n - 1, m) + m ) % n;

    }
    // 非递归解法
    public int solution2(int n, int m) {
        if (n = 0){
            return -1;
        }
        int last = 1;
        for (int i = 2; i < n; i++) {
            last = (last + m) % i;
        }
        return last;
    }
}