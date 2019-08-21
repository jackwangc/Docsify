import java.util.HashMap;

/**
 * 无重复子丰富的最长子串
 */
class Solution {
    public void solution(String s) {
        int n = s.length(),ans = 0;
        Map<Charcter,Integer> map = new HashMap<>();
        // 通过 i，j 来维持一个滑动窗口
        for (int j = 0, i = 0; j < n; j++){
            // 遇到与 [start, end] 区间内字符相同的情况
            if (map.containsKey(s.charAt(j))) {
                // 此时将字符作为 key 值，获取其 value 值，并更新 start，此时 [start, end] 区间内不存在重复字符
                i = Math.max(map.get(s.charAt(j)), i);
            }
            ans = Math.max(ans, j-i+1);
            // 定义一个 map 数据结构存储 (k, v)，其中 key 值为字符，value 值为字符位置 +1，加 1 表示从字符位置后一个才开始不重复
            map.put(s.charAt(j), j+1);
        }
        System.out.print(ans);
        
    }
}