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
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(pRoot);
        while (!queue.isEmpty()) {
            int size = queue.size();
            ArrayList<Integer> re = new ArrayList<>();
            while (size-- >0){
                TreeNode node = queue.poll();
                if (node != null) {
                    re.add(node.val);
                }
                queue.add(node.left);
                queue.add(node.right);
            }
            res.add(re);
        }
        return res;
    }

}