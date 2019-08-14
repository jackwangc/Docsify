import java.util.PriorityQueue;

/**
 * 读出数据流的中位数
 */
public class Solution {
    // 优先队列默认小顶堆
    private PriorityQueue<Integer> right = new PriorityQueue<>();
    // 大顶堆
    private PriorityQueue<Integer> left = new PriorityQueue<>((n1,n2) -> n2-n1);
    private int n = 0;
    public void Insert(Integer num) {
        // 偶数往右边插入
        if (n % 2 == 0) {
            left.add(num);
            right.add(left.poll());
        } else {
            right.add(num);
            left.add(right.poll());
        }
        n++;
    }

    public Double GetMedian() {
        if (n % 2 == 0){
            return (left.peek() + right.peek()) / 2;
        }else{
            return (double) right.peek();
        }
    }

}