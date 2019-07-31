# 数组题目

## 1. 剑指 offer

### 1.1 二维数组的查找 ⭐

* [数组查找](https://www.nowcoder.com/practice/abc3fe2ce8e146608e868a70efebf62e?tpId=13&tqId=11154&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.2 数组中的重复数字 ⭐

* [数组中的重复数组](https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8?tpId=13&tqId=11203&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.3 构建乘积数组 ⭐⭐

> o(n) 复杂度，类似动态规划，上下三角相乘

* [乘积数组](https://www.nowcoder.com/practice/94a4d381a68b47b7a8bed86f2975db46?tpId=13&tqId=11204&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.4 调整数组顺序 ⭐⭐⭐

> 分为排序和不排序，使用双指针，插入排序变形

* [调整数组顺序](https://www.nowcoder.com/practice/beb5aa231adc45b2a5dcc5b62c93f593?tpId=13&tqId=11166&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.5 顺时针打印矩阵 ⭐

* [打印矩阵](https://www.nowcoder.com/practice/9b4c81a02cd34f76be2659fa0d54342a?tpId=13&tqId=11172&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.6 连续子数组最大和 ⭐⭐⭐

* [子数组最大和](https://www.nowcoder.com/practice/459bd355da1549fa8a49e350bf3df484?tpId=13&tqId=11183&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

### 1.7 把数组排成最小的数 ⭐⭐⭐

> 装换为字符串，自定义比较器，求最大数和最小数

* [数组排成最小的数](https://www.nowcoder.com/practice/8fecd3f8ba334add803bf2a06af1b993?tpId=13&tqId=11185&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking)

## 2. leetcode

### 2.1 数组中第 k 大元素 ⭐⭐⭐

> 快排，堆排，java 堆的使用

* [数组中第k 个元素](https://leetcode-cn.com/problems/kth-largest-element-in-an-array/)

### 2.2 前 k 个高频元素 ⭐⭐⭐

> hash + 堆

* [前 k 个高频元素](https://leetcode-cn.com/problems/top-k-frequent-elements/)

### 2.3 第 k 大的数

> 构建三个变量，进行比较存储

* [第三大的数](https://leetcode-cn.com/problems/third-maximum-number/)

---

## 总结

1. 优先队列的使用
   
   ```java
   // 1. 实例化: 构造方法 （）、(队列大小)、(队列的大小，比较器)
   // 1.1 构造小顶堆
    Queue<Integer> ps = new PriorityQueue<Integer>();
   // 1.2 构造大顶堆
    Queue<Integer> pq = new PriorityQueue<Integer>(11,
                 new Comparator<Integer>() {
                         public int compare(Integer i1, Integer i2) {
                             return i2 - i1;  // 顺序变换 大顶堆
                         }
                     });
    pq.remove(); // 删除队首元素
    pq.add(object) // 添加元素
   ```
2. 双指针的用法
3. 排序算法记忆比较
4. Hash 的添加，遍历，删除










