/**
 * 给定一个二叉树和其中的一个结点，请找出中序遍历顺序的下一个结点并且返回。
 * 注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。
 */
public class TreeLinkNode {
    int val;
    TreeLinkNode left = null;
    TreeLinkNode right = null;
    TreeLinkNode next = null;

    TreeLinkNode(int val) {
        this.val = val;
    }
}

public class Solution {
    public TreeLinkNode GetNext(TreeLinkNode pNode)
    {
        // 分为两种情况
        // 1. 右子树不为空，找到右子树的最左结点
        if (pNode.right != null) {
            TreeLinkNode node = pNode.right;
            while (node.left != null){
                node = node.left;
            }
            return node;
        }else{
            // 右子树为空，找到第一个左节点包含该节点的树
            while (pNode.next != null) {
                TreeLinkNode parent = pNode.next;
                if (parent.next == pNode) {
                    return parent;
                }
                pNode = pNode.next;
            }
        }
        return null;
    }
}