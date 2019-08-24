import java.util.HashMap;
import java.util.Stack;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
 */
class Solution {
    HashMap<Character,Character> map = new HashMap<>();
    public Solution() {
        // 栈中存储元素
        this.map.put(')', '(');
        this.map.put(']', '[');
        this.map.put('}', '{');
    }
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // 如果当前字符是闭合括号
            if (map.containsKey(c)) {
                // 获取堆栈的顶部元素。如果堆栈为空，则将虚拟值设置为'#'
                char top = stack.empty() ? '#' : stack.pop();
                // 如果此括号的映射与堆栈的top元素不匹配，则返回false。
                if (top != this.map.get(c)) {
                    return false;
                }
            } else {
                // 如果它是一个开括号，将其push到堆栈上。
                stack.push(c);
            }
        }
        return stack.empty();
    }
}