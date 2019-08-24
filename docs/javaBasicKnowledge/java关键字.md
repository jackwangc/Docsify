# java 关键字

## this

1. 代表对象。就是所在函数所属对象的引用。哪个对象调用了this所在的函数，this就代表哪个对象
2. this是方法（除静态方法外）中存在的隐式参数，所以在方法中使用this
3. 注意：this调用构造函数，必须在构造函数的第一行。否则编译失败。
4. this不能用于static修饰的方法

## super

> super 关键字用以访问父类成员

1. 使用 super 关键字， super 代表父类对象
2. super 只能出现在子类的方法和构造方法中
3. 调用构造方法是，必须是第一句
4. super 不能访问父类的 private 成员

## static

> 关键字，是一个修饰符，用于修饰成员（成员变量和成员函数）

1. 在类中，用static声明的成员变量为静态变量，或者叫做：类属性，类变量，从属于类，
2. 给对象分配的内存里没有它为类的公用变量，属于类，被该类的所有实例共享，在类被载入时被显示初始化
3. 用static声明的方法为静态方法
4. 在调用该方法时，不会将对象的引用传递给它，所以在static方法中，不可访问非static的成员
5. 在静态方法中，不能用this和supper关键字

## final

> 声明数据为常量，可以是编译时常量，也可以是在运行时被初始化后不能被改变的常量

1. 对于基本类型，final 使数值不变；
2. 对于引用类型，final 使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的

## volatile

> Java语言提供了一种稍弱的同步机制，即volatile变量，用来确保将变量的更新操作通知到其他 线程，不保证原子性

1. 两种特性保证 volatile 类型的变量总会返回最新的值
   1. 变量可见性
      1. 如何保证可见性： 每次访问变量时进行一次刷新，每次访问的都是主内存中的最新版本
   2. 禁止重排序
2. 比 synchronized 更轻量级的同步锁，因为不会执行加锁操作，因此不会使线程阻塞
3. 使用场景，一个变量被多个线程共享，线程直接给这个变量赋值
4. 能够替代 synchronized 的情况
   1. 或者说是单纯的变量赋值
   2. 只有在状态真正独立于程序内其他内容时才能使用 volatile

5. 变量已经在 synchronized 中，没必要使用
6. 屏蔽了 JVM 优化，效率比较低，要减少使用。 

## synchronized

> synchronized可以保证方法或代码块在运行时，同一时刻只有一个线程可以进入到临界区（互斥性），同时它还保证了共享变量的内存可见性

1. synchronized 它可以把任意一个非 NULL 的对象当作锁。他属于独占式的悲观锁，同时属于可重入锁
2. synchronized 是一个重量级操作，需要调用操作系统相关接口，性能是低效的，有可能给线 程加锁消耗的时间比有用操作消耗的时间更多
3. Synchronized是非公平锁。 Synchronized在线程进入ContentionList时，等待的线程会先 尝试自旋获取锁，如果获取不到就进入 ContentionList，这明显对于已经进入队列的线程是 不公平的，还有一个不公平的事情就是自旋获取锁的线程还可能直接抢占 OnDeck 线程的锁 资
4. 核心组件

## finalize()

> finalize()是Object的protected方法，子类可以override实现资源清理工作，GC在回收对象之前调用该方法。
> 增加收尾功能，其实就是如果一个对象正在处理的是非Java 资源，如文件句柄或window 字符字体，这时你要确认在一个对象被撤消以前要保证这些资源被释放。（其他资源的释放）
> 不建议用finalize方法完成“非内存资源”的清理工作，但建议用于：① 清理本地对象(通过JNI创建的对象)；在finalize方法中显式调用其他资源释放方法

## 比较器

> 分为两种 (Comparable、Comparator)
1. Comparable
   * 类实现实现 `Comparable` 接口, 重写 `compareTo` 方法
   * 调用 `Arrays.sort(arr)` 进行排序
2. Comparator
   * 自定义比较器 ，自定义比较类实现 `Comparator` 接口
   * 调用 `Collections.sort(list,new MyComparator())` 进行排序

3. 原理：二叉树排序？
4. jdk8 排序  `Collections.sort(names, (s1, s2) -> s1.compareTo(s2));`

## 异常

1. Error 和 Exception 区别是什么？

Error 类型的错误通常为虚拟机相关错误，如系统崩溃，内存不足，堆栈溢出等，编译器不会对这类错误进行检测，JAVA 应用程序也不应对这类错误进行捕获，一旦这类错误发生，通常应用程序会被终止，仅靠应用程序本身无法恢复；

Exception 类的错误是可以在应用程序中进行捕获并处理的，通常遇到这种错误，应对其进行处理，使应用程序可以继续正常运行。

2. 运行时异常和一般异常区别是什么？

编译器不会对运行时异常进行检测，没有 try-catch，方法签名中也没有 throws 关键字声明，编译依然可以通过。如果出现了 RuntimeException, 那一定是程序员的错误。

一般一场如果没有 try-catch，且方法签名中也没有用 throws 关键字声明可能抛出的异常，则编译无法通过。这类异常通常为应用环境中的错误，即外部错误，非应用程序本身错误，如文件找不到等。

3. JVM 是如何处理异常的？

在一个方法中如果发生异常，这个方法会创建一个一场对象，并转交给 JVM，该异常对象包含异常名称，异常描述以及异常发生时应用程序的状态。创建异常对象并转交给 JVM 的过程称为抛出异常。可能有一系列的方法调用，最终才进入抛出异常的方法，这一系列方法调用的有序列表叫做调用栈。

JVM 会顺着调用栈去查找看是否有可以处理异常的代码，如果有，则调用异常处理代码。当 JVM 发现可以处理异常的代码时，会把发生的异常传递给它。如果 JVM 没有找到可以处理该异常的代码块，JVM 就会将该异常转交给默认的异常处理器（默认处理器为 JVM 的一部分），默认异常处理器打印出异常信息并终止应用程序

4. throw 和 throws 的区别是什么？

throw 关键字用来抛出方法或代码块中的异常，受查异常和非受查异常都可以被抛出。`try{ }catch(){ throw e}`。jvm 检测出异常并抛出，一定会异常

throws 关键字用在方法签名处，用来标识该方法可能抛出的异常列表。一个方法用 throws 标识了可能抛出的异常列表，调用该方法的方法中必须包含可处理异常的代码，否则也要在方法签名中用 throws 关键字声明相应的异常 `void func throws IOException,SQLException` .不一定会发出异常。

### 自定义异常类

```java
class FushuException extends Exception {
    private String msg;
    public FushuException(String msg) {
        this.msg = msg;
    }
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return msg;
    }
}
throw new FushuException("出现了除数是负数的异常");
```
