/**
 * 输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针指向任意一个节点）
 * 返回结果为复制后复杂链表的head。
 * 注意，输出结果中请不要返回参数中的节点引用，否则判题程序会直接返回空
 */

public class RandomListNode {
    int label;
    RandomListNode next = null;
    RandomListNode random = null;

    RandomListNode(int label) {
        this.label = label;
    }
}

public class Solution {
    public RandomListNode Clone(RandomListNode pHead)
    {
        if (pHead == null) {
            return null;
        }
        // 1. 复制每个结点，复制结点 a 到 a 的后面
        RandomListNode currentNode = pHead;
        while (currenNode != null) {
            // 复制结点
            RandomListNode cloneNode = new RandomListNode(currentNode.label);
            RandomListNode nextNode = currentNode.next;
            currentNode.next = cloneNode;
            cloneNode.next = nextNode;
            currentNode = nextNode;
        }
        // 2. 重新遍历链表，复制老结点随机指针的下一个结点给新结点的随机指针
        currentNode = pHead;
        while (currentNode != null) {
            // 复制老结点随机指针的下一个结点给新结点的随机指针
            currentNode.next.random = currentNode.random == null ? null : currentNode.random.next;
            currentNode = currentNode.next.next;
        }
        // 3. 拆分链表
        currentNode = pHead;
        RandomListNode pCloneNode = pHead.next;
        while (currentNode != null) {
            RandomListNode cloneNode = currentNode.next;
            // 恢复原有链表结构
            currentNode.next = cloneNode.next;
            // 创建新的链表结构
            cloneNode.next = cloneNode.next == null ? null : cloneNode.next.next;
            currentNode = currentNode.next;
        }

        // 4. 返回
        return pCloneNode;
    }
}