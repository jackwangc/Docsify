/**
 * s = "3[a]2[bc]", 返回 "aaabcbc".
 * s = "3[a2[c]]", 返回 "accaccacc".
 * s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".
 */

 /**
  * 括号匹配问题
  */
 public class Solution {
     /**
      * 递归解法
      */
    public String[] solution(String s,int i) {
        StringBuilder res = new StringBuilder();
        int multi = 0;
        while (i < s.length()) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                multi = multi * 10 + Integer.parseInt(String.valueOf(s.charAt(i)));
            } else if(s.charAt(i) == '[') {
                String[] tmp = dfs(s, i + 1);
                i = Integer.parseInt(tmp[0]);
                while(multi > 0) {
                    res.append(tmp[1]);
                    multi--;
                }
            } else if(s.charAt(i) == ']') {
                return new String[]{ String.valueOf(i) ,res.toString() };
            } else {
                res.append(s.charAt(i));
            }
            i++;
        }
        return new String[] { res.toString()};
    }
    /**
     * 双栈法
     */
    public String decodeString(String s) {
        StringBuilder res = new StringBuilder();
        int multi = 0;
        // 存储当前片段内的字符个数
        LinkedList<Integer> stack_multi = new LinkedList<>();
        // 存储当前片段内字符串
        LinkedList<String> stack_res = new LinkedList<>();
        for(Character c : s.toCharArray()) {
            // 开始入栈
            if(c == '[') {
                stack_multi.addLast(multi);
                stack_res.addLast(res.toString());
                multi = 0;
                res = new StringBuilder();
            }
            // 结果处理
            else if(c == ']') {
                StringBuilder tmp = new StringBuilder();
                int cur_multi = stack_multi.removeLast();
                for(int i = 0; i < cur_multi; i++) tmp.append(res);
                res = new StringBuilder(stack_res.removeLast() + tmp);
            }
            // 数字循环相加
            else if(c >= '0' && c <= '9') 
                multi = multi * 10 + Integer.parseInt(c + "");
            // 普通字符加入结果
            else 
                res.append(c);
        }
        return res.toString();
    }    
 }