public class Solution {
    public int NumberOf1(int n) {
        int m = 0;
        while (n != o) {
            n = n & (n - 1);
            m = m + 1;
        }
        System.out.print(m);
        return m;
    }
}