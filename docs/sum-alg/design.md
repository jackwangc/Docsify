# 面试算法设计

## 1. 设计链表

```java
class linkedList {

    private class LinkNode{
        public int val;
        public LinkNode next;
        public LinkNode(int val,LinkNode next){
            this.val = val;
            this.next = next;
        }
    }
    private LinkNode dummyHeader;
    private int size;
    public linkedList() {
        this.size = 0;
        this.dummyHeader = new LinkNode(0, null); 
    }   
    public int get(int index) {
        if (index < 0 && index >=size){
            return -1;
        }else{
            LinkNode cur = dummyHeader.next;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
            return cur.val;
        }
    }   
    public void addAtHead(int val) {
        addAtIndex(0, val);
    }   
    public void addAtTail(int val) {
        addAtIndex(size, val);
    }   
    public void addAtIndex(int index, int val) {
        if(index < 0 || index > 0){

        }else{
            LinkNode pre = dummyHeader;
            for (int i = 0; i < index; i++) {
                pre = pre.next;
            }
            LinkNode insertNode = new LinkNode(val, null);
            insertNode.next = pre.next;
            pre.next = insertNode;
            size++;
        }
    }   
    public void deleteAtIndex(int index) {
        if(index < 0 || index > 0){

        }else{
            LinkNode pre = dummyHeader;
            for (int i = 0; i < index; i++) {
                pre = pre.next;
            }
            LinkNode dLinkNode = pre.next;
            pre.next = dLinkNode.next;
            dLinkNode = null;
            size--;
        }
    }
}
```

## 2. 设计循环队列

### 2.1 普通队列

```java
class queue{
    // 存储内容
    private List<Integer> data;
    // 维护数组的出队顺序
    private int p_start;
    public queue() {
        data = new ArrayList<Integer>();
        p_start = 0;
    }    
    public boolean enQueue(int x){
        data.add(x);
        return true;
    }
    public boolean deQueue() {
        if (isEmpty()){
            return false;
        }
        p_start++;
        return true;
    }
    public int Front() {
        return data.get(p_start);
    }
    public boolean isEmpty() {
        return p_start >= data.size();
    }
}
```

### 2.2 循环队列

```java
/**
 * circularQueue 循环队列
 */
class circularQueue {
    private int[] data; // 数组 存储队列元素
    private int head; // 头指针
    private int tail; // 尾指针
    private int size; // 队列大小
    // 构造函数
    public circularQueue(int k) {
        data = new int[k];
        head = -1;
        tail = -1;
        size = k;
    }
    public boolean enQueue(int value) {
        if (isFull()) {
            return false;
        }
        if (isEmpty()) {
            head = 0;
        }
        tail = (tail + 1) % size;
        data[tail] = value;
        return true;
    }
    public boolean deQueue() {
        if (isEmpty()) {
            return false;
        }
        head = (head + 1) % size;
        if (head == tail) {
            head = -1;
            tail = -1;
            return true;
        }
        return true;
    }
    public int Front() {
        if (isEmpty()) {
            return -1;
        }
        return data[head];
    }
    public int Rear() {
        if (isEmpty()) {
            return -1;
        }
        return data[tail];
    }
    public boolean isEmpty() {
        return head == -1;
    }
    public boolean isFull() {
        return ((tail+1)%size) == head;
    }   
}
```

## 3. 栈

### 4.1 普通栈

```java
/**
 * stack 栈 用动态数组实现
 */
 class stack {
    private List<Integer> data;
    public stack(){
        data = new ArrayList<>();
    }
    public void push(int x) {
        data.add(x);
    }
    public boolean isEmpty() {
        return data.isEmpty();
    }
    public int top() {
        return data.get(data.size()-1);
    }
    public boolean pop() {
        if(isEmpty()){
            return false;
        }
        data.remove(data.size()-1);
        return true;
    }
}
```

### 4.2 最小栈

```java
public class MinStack {

    // 数据栈
    private Stack<Integer> data;
    // 辅助栈
    private Stack<Integer> helper;
    /**
     * initialize your data structure here.
     */
    public MinStack() {
        data = new Stack<>();
        helper = new Stack<>();
    }
    // 思路 1：数据栈和辅助栈在任何时候都同步
    public void push(int x) {
        // 数据栈和辅助栈一定会增加元素
        data.add(x);
        if (helper.isEmpty() || helper.peek() >= x) {
            helper.add(x);
        } else {
            helper.add(helper.peek());
        }
    }
    public void pop() {
        // 两个栈都得 pop
        if (!data.isEmpty()) {
            helper.pop();
            data.pop();
        }
    }
    public int top() {
        if(!data.isEmpty()){
            return data.peek();
        }
        throw new RuntimeException("栈中元素为空，此操作非法");
    }
    public int getMin() {
        if(!helper.isEmpty()){
            return helper.peek();
        }
        throw new RuntimeException("栈中元素为空，此操作非法");
    }
}
```



## 4. LRU 缓存

> 运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 

```java
class LRUCache {
    // key -> Node(key, val)
    private HashMap<Integer, Node> map;
    // Node(k1, v1) <-> Node(k2, v2)...
    private DoubleList cache;
    // 最大容量
    private int cap;
    
    public LRUCache(int capacity) {
        this.cap = capacity;
        map = new HashMap<>();
        cache = new DoubleList();
    }
    
    public int get(int key) {
        if (!map.containsKey(key))
            return -1;
        int val = map.get(key).val;
        // 利用 put 方法把该数据提前
        put(key, val);
        return val;
    }
    
    public void put(int key, int val) {
        // 先把新节点 x 做出来
        Node x = new Node(key, val);      
        if (map.containsKey(key)) {
            // 删除旧的节点，新的插到头部
            cache.remove(map.get(key));
            cache.addFirst(x);
            // 更新 map 中对应的数据
            map.put(key, x);
        } else {
            if (cap == cache.size()) {
                // 删除链表最后一个数据
                Node last = cache.removeLast();
                map.remove(last.key);
            }
            // 直接添加到头部
            cache.addFirst(x);
            map.put(key, x);
        }
    }
}
```
