/**
 * 递归用法
 * 判断递归条件
 */
public class Solution {
    public int Sum_Solution(int n) {
        int sum = n;
        // &&就是逻辑与，逻辑与有个短路特点，前面为假，后面不计算
        boolean b = (n > 0) && ((sum += Sum_Solution(n - 1)) > 0);
        return sum;
    }
}