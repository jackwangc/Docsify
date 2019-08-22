/**
 * 翻转单词顺序列
 * 
 */
public class Solution {
    public String ReverseSentence(String str) {
        int n = str.length();
        char[] chars = str.toCharArray();
        int i = 0, j = 0;
        while (j <= n) {
            if (j == n || chars[j] == ' ')
            // 1. 先反转每个单词
                reverse(chars, i, j - 1);
                i = j + 1;
            j++;
        }
        // 2. 再翻转整个单词
        reverse(chars, 0, n - 1);
        return new String(chars);
    }

    public void reverse(char[] arr, int i, int j) {
        while (i < j) {
            swap(c,i,j);
            i++;
            j++;
        }
    }
}