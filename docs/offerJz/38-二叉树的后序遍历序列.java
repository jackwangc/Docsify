/**
 * 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。
 * 如果是则输出Yes,否则输出No。假设输入的数组的任意两个数字都互不相同
 */
public class Solution {
    public boolean VerifySquenceOfBST(int [] sequence) {
        if (sequence.length == 0 ){
            return false;
        }
        return judge(sequence,0,sequence.length-1);
    }
    public boolean judge(int[] arr,int i, int j) {
        if (i >= j){
            return true;
        }
        int tmp = arr[j];
        int m = j -1;
        // 查找右子树
        while (arr[m] > tmp && m > i) {
            m --;
        }
        // 判断左子树是否都小于 tmp
        for (int s = m-1; s >= i; s--) {
            if (arr[s] > tmp){
                return false;
            }
        }
        // 左子树，右子树分别递归
        return judge(arr, m, j-1) && judge(arr, i, m-1);
    }
}