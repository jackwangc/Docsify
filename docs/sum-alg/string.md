# 字符串

## 1. 剑指 offer

### 1.1 替换空格 ⭐

* [替换空格](https://www.nowcoder.com/practice/4060ac7e3e404ad1a894ef3e17650423?tpId=13&tqId=11155&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.2 正则表达式匹配 ⭐⭐

> 考虑可能发生的多种情况

* [正则表达式匹配](../offerJz/5-正则表达式匹配.java)
   
### 1.3 表示数值的字符串 ⭐⭐

> 考虑可能发生的多种情况

* [表示数值的字符串](../offerJz/6-表示数值的字符串.java)

### 1.4 字符流不重复字符串 ⭐⭐

> 字符 ascill 码的利用

* [字符流中第一个不重复的字符](https://www.nowcoder.com/practice/00de97733b8e4f97a3fb5c680ee10720?tpId=13&tqId=11207&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.5 字符串的排列 ⭐⭐⭐⭐

> 字符串，数组的排列组合问题汇总

* [字符串的排列](https://www.nowcoder.com/practice/fe6b651b66ae47d7acce78ffdd9a96c7?tpId=13&tqId=11180&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)


## 2. leetcode

### 1.6 组合 ⭐⭐

* [组合](https://leetcode-cn.com/problems/combinations/)
* [组合Ⅱ](https://leetcode-cn.com/problems/combination-sum-ii/)

## 3. 总结

1. 排列组合模板 ⭐⭐

```java
/*
* 排列模板
*/
public class Solution {

    // 定义参数，去重操作
	public ArrayList<Integer> permute(int[] num){
		Arrays.sort(num);
		ArrayList<ArrayList<Integer>> results=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> path=new ArrayList<Integer>();
		permuteHelper(results,path,num/*?*/);
		return results;
	}

    // 
    private void permuteHelper(ArrayList<ArrayList<Integer>> results,ArrayList<Integer> path,int[] num){
        //是方法结束的条件
        if(path.size()==num.length){ 
			results.add(new ArrayList<Integer>(path));
			return;
		}
		for(int =/*?*/;i<numm.length;i++){
			//提议要求要跳过那些条件
			path.add(num[i]);
			permuteHelper(results, path, num);
		    path.remove(path.size()-1);
		}
    }
}
```
