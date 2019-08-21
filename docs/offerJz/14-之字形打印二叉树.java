import java.util.*;

class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;

    public TreeNode(int val) {
        this.val = val;

    }

}

/**
 * 二叉树的层次遍历
 */
public class Solution {
    public ArrayList<ArrayList<Integer> > Print(TreeNode pRoot) {
        // 结果集合
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        // 层次遍历队列
        Queue<TreeNode> queue = new LinkedList<>();
        // 奇偶标签
        boolean flag = true;
        queue.add(pRoot);
        while (!queue.isEmpty()) {
            // 当前队列的长度 = 此行的元素个数
            int n = queue.size();
            ArrayList<Integer> re = new ArrayList<>();
            while (n > 0) {
                TreeNode node = queue.poll();
                // 弹出一个队列减少 1
                re.add(node.val);
                // 更改优化
//                if (flag == false){
//                    re.add(0,node.val);
//                } else{ re.add(node.val) }
                if (node.left != null){
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
                n--;
            }
            if (flag == false){
                // 偶数行反转队列，这样浪费效率
                Collections.reverse(re);
            }
            res.add(re);
            flag = !flag;

        }
        return res;
    }

}