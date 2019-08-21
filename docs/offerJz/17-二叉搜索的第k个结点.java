/*
public class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;

    public TreeNode(int val) {
        this.val = val;

    }

}
*/
/**
 * 找出二叉搜索树的 k 个结点
 * 递归方法
 */
public class Solution {
    int cnt;
    TreeNode re;
    TreeNode KthNode(TreeNode pRoot, int k)
    {
        if (pRoot == null){
            return null;
        }
        solution(pRoot,k);
        return re;
    }
    void solution(TreeNode pRoot,int k){
        if (pRoot.left != null) {
            solution(pRoot.left,k);
        }
        
        cnt++;
        if (cnt == k) {
            re = pRoot;
            return;
        }
        if (pRoot.right != null){
            solution(pRoot.right,k);
        }
       
        
    }
}