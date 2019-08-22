/**
 * 滑动窗口
 */
import java.util.ArrayList;
public class Solution {
    public ArrayList<ArrayList<Integer> > FindContinuousSequence(int sum) {
       ArrayList<ArrayList<Integer>> re = new ArrayList<>();
       int begin = 1, end = 2;
       while (end > begin) {
           // 求和公式
           int cur = (begin + end) * (end - begin + 1) / 2;
           if (cur == sum) {
               ArrayList<Integer> list = new ArrayList<>();
               for (int i = begin; i < end; i++) {
                   list.add(i);
               }
               re.add(list);
               // 当前和大 begin 右移
           } else if (cur > sum) {
               begin++;
               // 当前和小 end 右移
           } else {
               end++;
           }
       }
    }
}