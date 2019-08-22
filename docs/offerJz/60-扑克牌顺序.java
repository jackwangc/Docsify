import java.util.*;
public class Solution {
    public boolean isContinuous(int [] numbers) {
        int cnt = 0;
        // 先统计个数
        for(int nums : numbers) {
            if (nums ==0){
                cnt++;
            }
        }
        Arrays.sort(numbers);
        for(int i = cnt; i < numbers.length-1; i++) {
            if (numbers[i] == numbers[i+1]) {
                return false;
            }
            // 3-2-1 癞子个数为两数相减，再减1
            cnt -= numbers[i+1] - numbers[i] - 1;
        }
        // 最后判断癞子是否为正数
        return cnt >= 0;
    }
}