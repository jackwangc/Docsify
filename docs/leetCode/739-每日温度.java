class Solution {
    public int[] dailyTemperatures(int[] T) {
        int[] res = new int[n];
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = T.length; i > 0; i--) {
            // 维护一个递减栈
            // 比当前元素小的都出栈
            while(!stack.isEmpty() && T[i] >= T[stack.peek()]) {
                stack.pop();
            }
            res[i] = stack.isEmpty() ? 0 : stack.peek() - i;
            stack.push(i);
        }
        return res;
    }
}