import java.util.Stack;

/**
 * 输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）
 */
/**
 * 32-树的子结构
 */

public class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;

    public TreeNode(int val) {
        this.val = val;

    }

}

public class Solution {
    /**
     * 两个递归
     * 递归 1 找到 root1 中和 root2 相等的结点
     * @param root1
     * @param root2
     * @return
     */
    public boolean HasSubtree(TreeNode root1,TreeNode root2) {
        boolean re = false;
        if (root1.val == root2.val) {
            // 递归2 判断是否为子树
            re = isSame(root1, root2);
        } 
        if (re == false) {
            re = HasSubtree(root1.left, root2) || HasSubtree(root1.right, root2);
        }
        return re;
    }
    public boolean isSame(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        }
        if (node1 == null || node2 == null || node1.val != node2.val) {
            return false;
        }
        return isSame(node1.left, node2.left) && isSame(node1.right, node2.right);
    }
}