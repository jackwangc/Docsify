import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * 给定一个数组和滑动窗口的大小，找出所有滑动窗口里数值的最大值。
 * 例如，如果输入数组{2,3,4,2,6,2,5,1}及滑动窗口的大小3，
 * 那么一共存在6个滑动窗口，他们的最大值分别为{4,4,6,6,6,5}
 */
/**
 * 20-滑动窗口的最大值
 */
class Solution {
    /**
     * 双端队列
     * @param nums
     * @param k
     * @return
     */
    public int[] solution(int[] nums, int k) {
        if (nums == null || num.length.length < k || k ==0){
            return new int[0];
        }
        int[] res = new int[nums.length - k + 1];
        // 队列存储的是位置的下标
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            
            while (!deque.isEmpty() && nums[deque.getLast()] < nums[i]) {
                deque.removeLast();
            }
            deque.addLast(i);
            // 最大元素位置不在队列内，移除
            if (deque.getFirst() == i - k) {
                deque.removeFirst();
            }
            // 输出结果
            if (i > k - 1) {
                res[i - k + 1] = nums[deque.getFirst()];
            }
        }
    }
    /**
     * 优先队列
     */
    public void solution2(int[] nums,int k) {
        // 大顶堆实现双向队列的功能
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1,o2) -> o2-o1);
        for (int i = 0; i < k; i++) {
            pq.add(nums[i]);
        }
        System.out.print(pq.peek());
        for (int i = 0,j = size; i < num.length; i++,j++) {
            pq.remove(nums[i]);
            pq.add(nums[j]);
            System.out.print(pq.peek());
        }
    }
}