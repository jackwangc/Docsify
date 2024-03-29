# jvm

## java 内存模型

1. 栈区
   1. 每个线程包含一个栈区，栈中只保存基本数据类型的对象和自定义对象的引用(不是对象)，对象都存放在堆区中。
   2. 每个栈中的数据(原始类型和对象引用)都是私有的，其他栈不能访问。
   3. 栈分为 3 个部分：基本类型变量区、执行环境上下文、操作指令区(存放操作指令)。
   4. 从方法调用直至执行完成的过程，对应着一个栈帧在 Java 虚拟机栈中入栈和出栈的过程.
2. 堆区:
   1. 存储的全部是对象，每个对象都包含一个与之对应的class的信息（class 的目的是得到操作指令）。
   2. jvm 只有一个堆区（heap）被所有线程共享，堆中不存放基本类型和对象引用，只存放对象本身。
   3. 所有对象都在这里分配内存，是垃圾收集的主要区域
   4. 现代的垃圾收集器基本都是采用分代收集算法，其主要的思想是针对不同类型的对象采取不同的垃圾回收算法
3. 方法区:
   1. 在java的虚拟机中有一块专门用来存放已经加载的类信息、常量、静态变量以及方法代码的内存区域
   2. 常量池是方法区的一部分，主要用来存放常量和类中的符号引用等信息
   3. 又叫静态区，跟堆一样，被所有的线程共享。方法区包含所有的class和static变量。
   4. 方法区中包含的都是在整个程序中永远唯一的元素，如class，static变量（使用static修饰的成员变量）
   

## java 类

> 类的生命周期，加载、[验证、准备、解析]连接、初始化、使用和卸载

### 类加载机制

#### 类加载时机

> JVM 的类加载机制是指 JVM 把描述类的数据从 .class 文件加载到内存，并对数据进行校验、转换解析和初始化，最终形成可以被虚拟机直接使用的 Java 类型

1. 有五种情况会触发类的初始化。
2. 主动引用：上面这五种行为称为对一个类的的主动引用，会触发类的初始化
   1. new 新建一个 Java 对象，访问或者设置一个类的静态字段，访问一个类的静态方法的时候
   2. 进行反射调用的时候，如果类没有进行初始化，则需要先触发其初始化
   3. 初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化
   4. 虚拟机启动的时候，虚拟机会先初始化这个类
   5. java.lang.invoke.MethodHandle 实例最后的解析结果 REF_getStatic、REF_putStatic、REF_invokeStatic 的方法句柄，并且这个方法句柄对应的类没有进行过初始化，则需要先触发其初始化？
3. 被动引用：除上面五种主动引用之外，其他引用类的方式都不会触发类的初始化，称为类的被动引用

#### 类加载过程

1. 加载
   1. 通过一个类的全限定名获取定义此类的二进制字节流
   2. 将二进制字节流所代表的静态存储结构转换为方法区中的运行时数据结构
   3. 在内存中生成一个代表此类的 java.lang.Class 的对象，作为方法区中这个类的访问入口

2. 验证
   1. 确保 .class 文件中的字节流所包含的信息是符合当前虚拟机的要求，并且不会危害到虚拟机自身的安全的。 

3. 准备
   1. 在方法区中为类 Class 对象的类变量分配内存并初始化类变量
   2. 在方法区中分配内存的只有类变量（被 static 修饰的变量），而不包括实例变量，实例变量将会跟随着对象在 Java 堆中为其分配内存
   3. 初始化类变量的时候，是将类变量初始化为其类型对应的 0 值
   4. 对于常量，其对应的值会在编译阶段就存储在字段表的 ConstantValue 属性当中

4. 解析
   1. 解析是将符号引用解析为直接引用的过程
   2. 有了直接引用，那引用的目标必定已经在内存中

5. 初始化
   1. 类的初始化阶段才是真正开始执行类中定义的 Java 程序代码。
   2. 初始化说白了就是调用类构造器 的过程`<clinit>()`
   3. 在类的构造器中会为类变量初始化定义的值，会执行静态代码块中的内容。
   4. `<clinit>()` 类构造器。`<init>()`实例构造器:init是对象构造器方法

#### 类加载器

> 类加载器是完成"通过一个类的全限定名获取这个类的二进制字节流"的工作，类加载器是独立于虚拟机之外存在的

1. 如果两个类是由不同的类加载器加载的,即使它们两个来自于同一个 .class 文件，那这两个类也是不同的。
2. 加载器可以有不同的分类方式。我们的应用程序就是由上面三种类加载器相互配合被加载进来的，如果有必要可以自定义类加载器
   1. 启动类加载器
   2. 扩展类加载器
   3. 应用程序类加载器
3. 双亲委派
   1. 双亲委派模型的工作流程是这样的，如果一个类加载器收到了一个加载类的请求，会首先把这个请求委派给自己的父加载器去加载，这样的所有的类加载请求都会向上传递到 Bootstrap ClassLoader 中去，只有当父类加载器无法完成这个类加载请求时，才会让子类加载器去处理这个请求
   2. 好处：被加载到虚拟机中的类会随着加载他们的类加载器有一种优先级的层次关系

## 垃圾收集

> 垃圾收集主要是针对堆和方法区进行。程序计数器、虚拟机栈和本地方法栈这三个区域属于线程私有的，只存在于线程的生命周期内，线程结束之后就会消失，因此不需要对这三个区域进行垃圾回收

### 判断一个对象是否可以被回收

1. 堆中的对象
   1. 引用计数法-对象中增加空间存储引用次数，无法解决处理相互引用的数据。
   2. 可达性分析算法
      1. 虚拟机栈（栈帧中的本地变量表）中引用的对象
      2. 方法区中类静态属性引用的对象
      3. 方法区中常量引用的对象
      4. 本地方法栈中 JNI（即一般说的 Native 方法）引用的对象
   3. 以 GC Roots 为起始点进行搜索，可达的对象都是存活的，不可达的对象可被回收
   4. 虚拟机栈中局部变量表中引用的对象，本地方法栈中 JNI 中引用的对象，方法区中类静态属性引用的对象，方法区中的常量引用的对象

2. 方法区的回收
   1. 方法区主要存放永久代对象
   2. 对常量池的回收和对类的卸载。

### 引用类型

1. 强引用
   1. 被强引用关联的对象不会被回收
   2. 使用 new 一个新对象的方式来创建强引用

2. 软引用
   1. 被软引用关联的对象只有在内存不够的情况下才会被回收
   2. 使用 SoftReference 类来创建软引用

3. 弱引用
   1. 被弱引用关联的对象一定会被回收，也就是说它只能存活到下一次垃圾回收发生之前
   2. 使用 WeakReference 类来创建弱引用

4. 虚引用
   1. 为一个对象设置虚引用的唯一目的是能在这个对象被回收时收到一个系统通知
   2. 一个对象是否有虚引用的存在，不会对其生存时间造成影响，也无法通过虚引用得到一个对象

引用类型| 被垃圾回收时间| 用途| 生存时间
---|---|---|---
强引用 | 从来不会| 对象的一般状态 | JVM停止运行时终止
软引用 | 当内存不足时 | 对象缓存 | 内存不足时终止
弱引用 | 正常垃圾回收时 | 对象缓存 | 垃圾回收后终止
虚引用 | 正常垃圾回收时 | 跟踪对象的垃圾回收 | 垃圾回收后终止

### 垃圾回收算法

1. 标记整理
   1. 让所有存活的对象都向一端移动，然后直接清理掉端边界以外的内存
   2. 不会产生内存碎片
   3. 需要移动大量对象，处理效率比较低
2. 标记清除
   1. 标记和清除过程效率都不高
   2. 会产生大量不连续的内存碎片，导致无法给大对象分配内存
3. 复制
   1. 将内存划分为大小相等的两块，每次只使用其中一块，当这一块内存用完了就将还存活的对象复制到另一块上面，然后再把使用过的内存空间进行一次清理
   2. 现在的商业虚拟机都采用这种收集算法回收新生代
4. 分代收集
   1. 一般将堆分为新生代和老年代。
   2. 新生代使用：复制算法
   3. 老年代使用：标记 - 清除 或者 标记 - 整理 算法
      1. 老年代中因为对象存活率高、没有额外空间对它进行分配担保，就必须使用标记-清理或者标记 --- 整理
   4. 一块较大的 Eden 空间和两块较小的 Survivor 空间，每次使用 Eden 和其中一块 Survivor。 
   5. 年轻代为什么要1个eden两个survivor，这样做有什么好处？？：用了copying算法，解决内存碎片问题，能更及时的回收垃圾，增大内存的有效使用

### 垃圾收集器

收集器 | 串行、并行or并发 | 新生代/老年代 | 算法 | 目标 | 适用场景
---|---|---|---|---|---
Serial	|串行	|新生代	|复制算法	|响应速度优先	|单CPU环境下的Client模式
Serial Old	|串行	|老年代	|标记-整理|	响应速度优先|	单CPU环境下的Client模式、CMS的后备预案
ParNew|	并行	|新生代	|复制算法	|响应速度优先	|多CPU环境时在Server模式下与CMS配合
Parallel Scavenge	|并行|	新生代	|复制算法	|吞吐量优先|	在后台运算而不需要太多交互的任务
Parallel Old	|并行	|老年代|	标记-整理|	吞吐量优先	|在后台运算而不需要太多交互的任务
CMS	|并发	|老年代|	标记-清除	|响应速度优先	|集中在互联网站或B/S系统服务端上的Java应用
G1	|并发	|both	|标记-整理+复制算法	|响应速度优先	|面向服务端应用，将来替换CMS

### 内存分配和回收策略

1. Minor GC：回收新生代，因为新生代对象存活时间很短，因此 Minor GC 会频繁执行，执行的速度一般也会比较快。
   1. 大多数情况下，对象在新生代 Eden 上分配，当 Eden 空间不够时，发起 Minor GC
   2. 大对象直接进入老年代
   3. 长期存活的对象进入老年代
   4. 空间分配担保：发生 Minor GC 之前，虚拟机先检查老年代最大可用的连续空间是否大于新生代所有对象总空间

2. Full GC：回收老年代和新生代，老年代对象其存活时间长，因此 Full GC 很少执行，执行速度会比 Minor GC 慢很多。
   1. 老年代空间不足
   2. 空间分配担保失败

## 内存泄漏

> 内存溢出：在堆上没有内存可以进行实例分配，并且堆没有办法扩展，方法区无法满足内存分配需求，虚拟机栈扩展时无法申请到足够的内存
> 内存泄漏：程序动态的分配了内存，但是存续结束时没有释放

如何避免内存泄漏

* 今早释放无用对象的引用
* 使用临时变量的时候，让引用变量退出活动区域自动设置为 null,按时进行gc,防止内存泄漏
* 进行字符串处理时，尽量使用 StringBuffer, 避免使用 String, 每个 string 对象都会独立占用一块区域

怎么排查

* 虚拟机在出现内存溢出异常时生成dump文件，然后通过外部工具(作者使用的是VisualVM)来具体分析异常的原因
* gc 时间，gc 次数
* 常见异常：java 堆内存异常，Java 栈内存异常，java 方法区内存异常

## jvm 调优

> 1. 将进入老年代的对象数量降到最低
> 2. 减少 FULL GC 的执行时间 

### cpu 飙高

1. 通过 top  命令找到 CPU 消耗最高的进程，并记住进程 ID。
2. 再次通过 top -Hp [进程 ID] 找到 CPU 消耗最高的线程 ID，并记住线程 ID.
3. 通过 JDK 提供的 jstack 工具 dump 线程堆栈信息到指定文件中。具体命令：jstack -l [进程 ID] >jstack.log。
4. 由于刚刚的线程 ID 是十进制的，而堆栈信息中的线程 ID 是16进制的，因此我们需要将10进制的转换成16进制的，并用这个线程 ID 在堆栈中查找。使用 printf "%x\n" [十进制数字] ，可以将10进制转换成16进制。
5. 通过刚刚转换的16进制数字从堆栈信息里找到对应的线程堆栈。就可以从该堆栈中看出端倪。

### 内存问题

1. `jstat -gcutil` 查看 分析 gc 数据
2. `jmap -dump` 记录下堆的内存快照，分析整个 Heap 的对象关联情况
3. `-Xmx100m` jvm 最大堆内存; `Xms100m` jvm 初始堆内存
4. `-Xss128k` 每个线程的栈大小
5. `-Xmn2g` 设置年轻代大小
6. 通过 gc 次数和时间来进行调整
---

**参考**

1. [理解 JVM 中的类加载机制](https://www.jianshu.com/p/0cf9aa251921)
2. [JVM 总结](https://meandni.com/tags/jvm/)
3. [JVM 从入门到放弃](https://mp.weixin.qq.com/s/JuNm9GK2gbZdkU7tVKaOCA)
4. [java虚拟机](https://cyc2018.github.io/CS-Notes/#/notes/Java%20虚拟机)
5. [深入java虚拟机](https://blog.csdn.net/mmc_maodun/column/info/java-vm)
6. [gc精华-必看](https://mp.weixin.qq.com/s/k_LPR3kd3BFrd8hofYXDjg)


