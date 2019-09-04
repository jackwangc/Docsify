/**
 * 比较版本号
 */
class Solution {
    public int solution(String str1, String str2) {
        String[] v1 = str1.split("\\.");
        String[] v2 = str2.split("\\.");
        int n = Math.max(v1.length, v2.length);
        for (int i = 0; i < n; i++) {
            int v1_int = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int v2_int = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (v1_int == v2_int) {
               continue; 
            } else if (v1_int > v2_int) {
                return 1;
            } else {
                return -1;
            }
        } 
        return 0;
    }
}