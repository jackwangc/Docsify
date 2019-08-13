> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 https://mp.weixin.qq.com/s?__biz=MzIwNjEwNTQ4Mw==&mid=2651577015&idx=1&sn=a34f7681fdafc886fe77d2cfd52e5c77&chksm=8cd9c453bbae4d45738641644a6553c29fa2e370495f3d2fa7cfdf62b0d7d0c32a1c467df981#rd

> 本文来自 凯伦 在 GitChat 上精彩分享「零基础搭建你的个人博客」，「**阅读原文**」看看大家与作者做了哪些交流。

**前言**

基于个人的兴趣，开了这场 chat，主题是 Mybatis 一级和二级缓存的应用及源码分析。希望在本场 chat 结束后，能够帮助读者朋友明白以下三点。

1. Mybatis 是什么。

2. Mybatis 一级和二级缓存如何配置使用。

3. Mybatis 一级和二级缓存的工作流程及源码分析。

本次分析中涉及到的代码和数据库表均放在 Github 上，地址: mybatis-cache-demo。

### 1. Mybatis 的基础概念

本章节会对 Mybatis 进行大体的介绍，分为官方定义和核心组件介绍。首先是 Mybatis 官方定义，如下所示。

MyBatis 是支持定制化 SQL、存储过程以及高级映射的优秀的持久层框架。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以对配置和原生 Map 使用简单的 XML 或注解，将接口和 Java 的 POJOs （Plain Old Java Objects，普通的 Java 对象）映射成数据库中的记录。

其次是 Mybatis 的几个核心概念。

1.  SqlSession : 代表和数据库的一次会话，向用户提供了操作数据库的方法。
    
2.  MappedStatement: 代表要发往数据库执行的指令，可以理解为是 Sql 的抽象表示。
    
3.  Executor: 具体用来和数据库交互的执行器，接受 MappedStatement 作为参数。
    
4.  映射接口: 在接口中会要执行的 Sql 用一个方法来表示，具体的 Sql 写在映射文件中。
    
5.  映射文件: 可以理解为是 Mybatis 编写 Sql 的地方，通常来说每一张单表都会对应着一个映射文件，在该文件中会定义 Sql 语句入参和出参的形式。
    

下图就是一个针对 Student 表操作的接口文件 StudentMapper，在 StudentMapper 中，我们可以若干方法，这个方法背后就是代表着要执行的 Sql 的意义。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhFd9SpeIkLmz3UQI8ApbY40nR9icE8MgTIwzd0lRpG8RtqfQrZH9Ojpg/0?)通常也可以把涉及多表查询的方法定义在 StudentMapper 中，如果查询的主体仍然是 Student 表的信息。也可以将涉及多表查询的语句单独抽出一个独立的接口文件。在定义完接口文件后，我们会开发一个 Sql 映射文件，主要由 mapper 元素和 select | insert | update | delete 元素构成，如下图所示。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhbbrPCkVuMA1rOxhEHHGFuAzHDNVnp70wOX2H5uVXlvQouOiabaLpyUA/0?)mapper 元素代表这个文件是一个映射文件，使用 namespace 和具体的映射接口绑定起来，namespace 的值就是这个接口的全限定类名。

select | insert | update | delete 代表的是 Sql 语句，映射接口中定义的每一个方法也会和映射文件中的语句通过 id 的方式绑定起来，方法名就是语句的 id，同时会定义语句的入参和出参，用于完成和 Java 对象之间的转换。

在 Mybatis 初始化的时候，每一个语句都会使用对应的 MappedStatement 代表，使用 namespace+ 语句本身的 id 来代表这个语句。如下代码所示，使用 mapper.StudentMapper.getStudentById 代表其对应的 Sql。

```sql
SELECT id,name,age FROM student WHERE id = #{id}
```

在 Mybatis 执行时，会进入对应接口的方法，通过类名加上方法名的组合生成 id，找到需要的 MappedStatement，交给执行器使用。至此，Mybatis 的基础概念介绍完毕。

### 2. 一级缓存

#### 2.1 一级缓存介绍

在系统代码的运行中，我们可能会在一个数据库会话中，执行多次查询条件完全相同的 Sql，鉴于日常应用的大部分场景都是读多写少，这重复的查询会带来一定的网络开销，同时 select 查询的量比较大的话，对数据库的性能是有比较大的影响的。

如果是 Mysql 数据库的话，在服务端和 Jdbc 端都开启预编译支持的话，可以在本地 JVM 端缓存 Statement，可以在 Mysql 服务端直接执行 Sql，省去编译 Sql 的步骤，但也无法避免和数据库之间的重复交互。关于 Jdbc 和 Mysql 预编译缓存的事情，可以看我的这篇博客 JDBC 和 Mysql 那些事。

Mybatis 提供了一级缓存的方案来优化在数据库会话间重复查询的问题。实现的方式是每一个 SqlSession 中都持有了自己的缓存，一种是 SESSION 级别，即在一个 Mybatis 会话中执行的所有语句，都会共享这一个缓存。一种是 STATEMENT 级别，可以理解为缓存只对当前执行的这一个 statement 有效。如果用一张图来代表一级查询的查询过程的话，可以用下图表示。![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhN8CtnRCia6rKAIS5X95sHeQY4W155psg9VPjPoHRLjFNuYTwgmjZzVw/0?)每一个 SqlSession 中持有了自己的 Executor，每一个 Executor 中有一个 Local Cache。当用户发起查询时，Mybatis 会根据当前执行的 MappedStatement 生成一个 key，去 Local Cache 中查询，如果缓存命中的话，返回。如果缓存没有命中的话，则写入 Local Cache，最后返回结果给用户。

#### 2.2 一级缓存配置

上文介绍了一级缓存的实现方式，解决了什么问题。在这个章节，我们学习如何使用 Mybatis 的一级缓存。只需要在 Mybatis 的配置文件中，添加如下语句，就可以使用一级缓存。共有两个选项，SESSION 或者 STATEMENT，默认是 SESSION 级别。

```
<setting />
```

#### 2.3 一级缓存实验

配置完毕后，通过实验的方式了解 Mybatis 一级缓存的效果。每一个单元测试后都请恢复被修改的数据。首先是创建了一个示例表 student，为其创建了对应的 POJO 类和增改的方法，具体可以在 entity 包和 Mapper 包中查看。

```
CREATE TABLE `student` (  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL,  `age` tinyint(3) unsigned DEFAULT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```

在以下实验中，id 为 1 的学生名称是凯伦。

##### 实验 1

开启一级缓存，范围为会话级别，调用三次 getStudentById，代码如下所示:

```
public void getStudentById() throws Exception {        SqlSession sqlSession = factory.openSession(true); // 自动提交事务        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);        System.out.println(studentMapper.getStudentById(1));        System.out.println(studentMapper.getStudentById(1));        System.out.println(studentMapper.getStudentById(1));    }
```

执行结果:![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhaC0ATRBTVLexH7oH4wMHHTkia3pl7IAPB7ZaSmrglbsxLHfR2JK3M4A/0?) 我们可以看到，只有第一次真正查询了数据库, 后续的查询使用了一级缓存。

##### 实验 2

在这次的试验中，我们增加了对数据库的修改操作，验证在一次数据库会话中，对数据库发生了修改操作，一级缓存是否会失效。

```sql
@Testpublic void addStudent() throws Exception {        SqlSession sqlSession = factory.openSession(true); // 自动提交事务        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);        System.out.println(studentMapper.getStudentById(1));        System.out.println("增加了" + studentMapper.addStudent(buildStudent()) + "个学生");        System.out.println(studentMapper.getStudentById(1));        sqlSession.close();}
```

执行结果:

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhjTlFDQe3cTdzC0FKic0WXoXgSx29ibQPxlSTzdOLsSAqA79AGnj7jkdw/0?)我们可以看到，在修改操作后执行的相同查询，查询了数据库，**一级缓存失效**。

实验 3

开启两个 SqlSession，在 sqlSession1 中查询数据，使一级缓存生效，在 sqlSession2 中更新数据库，验证一级缓存只在数据库会话内部共享。

```sql
@Testpublic void testLocalCacheScope() throws Exception {        SqlSession sqlSession1 = factory.openSession(true);         SqlSession sqlSession2 = factory.openSession(true);        StudentMapper studentMapper = sqlSession1.getMapper(StudentMapper.class);       StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);        System.out.println("studentMapper读取数据: " + studentMapper.getStudentById(1));        System.out.println("studentMapper读取数据: " + studentMapper.getStudentById(1));        System.out.println("studentMapper2更新了" + studentMapper2.updateStudentName("小岑",1) + "个学生的数据");        System.out.println("studentMapper读取数据: " + studentMapper.getStudentById(1));        System.outs.println("studentMapper2读取数据: " + studentMapper2.getStudentById(1));}
```

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhYu43ueAW56BbM1NCNMBuhxKp6KAzD9lSrRnu8yKZGu2UMDeYiacGxng/0?)我们可以看到，sqlSession2 更新了 id 为 1 的学生的姓名，从凯伦改为了小岑，但 session1 之后的查询中，id 为 1 的学生的名字还是凯伦，出现了脏数据，也证明了我们之前就得到的结论，一级缓存只存在于只在数据库会话内部共享。

#### 3. 一级缓存工作流程 & 源码分析

这一章节主要从一级缓存的工作流程和源码层面对一级缓存进行学习。

##### 3.1 工作流程

根据一级缓存的工作流程，我们绘制出一级缓存执行的时序图，如下图所示。

![](http://mmbiz.qpic.cn/mmbiz_png/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhsXOWweHehIbRLldiakbicZ4diaU13ia3Ns6dsSsHibjM0YJ1Fib0vZBljoAQ/0?)

**主要步骤如下:**

1. 对于某个 Select Statement，根据该 Statement 生成 key。

2. 判断在 Local Cache 中，该 key 是否用对应的数据存在。

3. 如果命中，则跳过查询数据库，继续往下走。

4. 如果没命中：

> 去数据库中查询数据，得到查询结果；
> 
> 将 key 和查询到的结果作为 key 和 value，放入 Local Cache 中。
> 
> 将查询结果返回；

5. 判断缓存级别是否为 STATEMENT 级别，如果是的话，清空本地缓存。

##### 3.2 源码分析

了解具体的工作流程后，我们队 Mybatis 查询相关的核心类和一级缓存的源码进行走读。这对于之后学习二级缓存时也有帮助。

**SqlSession**: 对外提供了用户和数据库之间交互需要的所有方法，隐藏了底层的细节。它的一个默认实现类是 DefaultSqlSession。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhxv8FCRI7PExiaOib5TCn6btVyEBj0BFNAictwjY4v0X0nWgxfsuQFSSwQ/0?)**Executor**: SqlSession 向用户提供操作数据库的方法，但和数据库操作有关的职责都会委托给 Executor。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZh7Isk6B2tdFDiaYIBXcY762ibQCXSQ5g8LJruEkgVgsjMOUzDuMMicRTDw/0?)如下图所示，Executor 有若干个实现类，为 Executor 赋予了不同的能力，大家可以根据类名，自行私下学习每个类的基本作用。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhadrHCgu8FXtDY6zXV7PVVKpdibzssQBI37FJ9KCSjqcqJNFN2Od4WTQ/0?)在一级缓存章节，我们主要学习 BaseExecutor。

**BaseExecutor**: BaseExecutor 是一个实现了 Executor 接口的抽象类，定义若干抽象方法，在执行的时候，把具体的操作委托给子类进行执行。

```
protected abstract int doUpdate(MappedStatement ms, Object parameter) throws SQLException;protected abstract List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException;protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;protected abstract <E> Cursor<E> doQueryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds, BoundSql boundSql) throws SQLException;
```

在一级缓存的介绍中，我们提到对 Local Cache 的查询和写入是在 Executor 内部完成的。在阅读 BaseExecutor 的代码后，我们也发现 Local Cache 就是它内部的一个成员变量，如下代码所示。

```
public abstract class BaseExecutor implements Executor {protected ConcurrentLinkedQueue<DeferredLoad> deferredLoads;protected PerpetualCache localCache;
```

**C****ache**: Mybatis 中的 Cache 接口，提供了和缓存相关的最基本的操作，有若干个实现类，使用装饰器模式互相组装，提供丰富的操控缓存的能力。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZheMDfSReEnbgeIERTVk8l3TLntYZhjlQbMur59hX7yfiaibWlic5sDCK8g/0?)

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhWiaBoc6m9myqQ3ic0b9P1U6E59YT9z5ibHiazPg8xhsLvVLkGbAxlVzlBw/0?)BaseExecutor 成员变量之一的 PerpetualCache，就是对 Cache 接口最基本的实现，其实现非常的简内部持有了 hashmap，对一级缓存的操作其实就是对这个 hashmap 的操作。如下代码所示。

```sql
public class PerpetualCache implements Cache {  private String id;  private Map<Object, Object> cache = new HashMap<Object, Object>();
```

在阅读相关核心类代码后，从源代码层面对一级缓存工作中涉及到的相关代码，出于篇幅的考虑，对源码做适当删减，读者朋友可以结合本文，后续进行更详细的学习。

为了执行和数据库的交互，首先会通过 DefaultSqlSessionFactory 开启一个 SqlSession，在创建 SqlSession 的过程中，会通过 Configuration 类创建一个全新的 Executor，作为 DefaultSqlSession 构造函数的参数，代码如下所示。

```sql
private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {      ............    final Executor executor = configuration.newExecutor(tx, execType);         return new DefaultSqlSession(configuration, executor, autoCommit);}
```

如果用户不进行制定的话，Configuration 在创建 Executor 时，默认创建的类型就是 SimpleExecutor, 它是一个简单的执行类，只是单纯执行 Sql。以下是具体用来创建的代码。

```sql
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {    executorType = executorType == null ? defaultExecutorType : executorType;    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;    Executor executor;    if (ExecutorType.BATCH == executorType) {      executor = new BatchExecutor(this, transaction);    } else if (ExecutorType.REUSE == executorType) {      executor = new ReuseExecutor(this, transaction);    } else {      executor = new SimpleExecutor(this, transaction);    }    // 尤其可以注意这里，如果二级缓存开关开启的话，是使用CahingExecutor装饰BaseExecutor的子类    if (cacheEnabled) {      executor = new CachingExecutor(executor);                          }    executor = (Executor) interceptorChain.pluginAll(executor);    return executor;}
```

在 SqlSession 创建完毕后，根据 Statment 的不同类型，会进入 SqlSession 的不同方法中，如果是 Select 语句的话，最后会执行到 SqlSession 的 selectList，代码如下所示。

```
@Overridepublic <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {      MappedStatement ms = configuration.getMappedStatement(statement);      return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);}
```

在上文的代码中，SqlSession 把具体的查询职责委托给了 Executor。如果只开启了一级缓存的话，首先会进入 BaseExecutor 的 query 方法。代码如下所示。

```
@Overridepublic <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {    BoundSql boundSql = ms.getBoundSql(parameter);    CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);    return query(ms, parameter, rowBounds, resultHandler, key, boundSql);}
```

在上述代码中，会先根据传入的参数生成 CacheKey，进入该方法查看 CacheKey 是如何生成的，代码如下所示。

```
CacheKey cacheKey = new CacheKey();cacheKey.update(ms.getId());cacheKey.update(rowBounds.getOffset());cacheKey.update(rowBounds.getLimit());cacheKey.update(boundSql.getSql());//后面是update了sql中带的参数cacheKey.update(value);
```

在上述的代码中，我们可以看到它将 MappedStatement 的 Id、sql 的 offset、Sql 的 limit、Sql 本身以及 Sql 中的参数传入了 CacheKey 这个类，最终生成了 CacheKey。我们看一下这个类的结构。

```
private static final int DEFAULT_MULTIPLYER = 37;private static final int DEFAULT_HASHCODE = 17;private int multiplier;private int hashcode;private long checksum;private int count;private List<Object> updateList;public CacheKey() {    this.hashcode = DEFAULT_HASHCODE;    this.multiplier = DEFAULT_MULTIPLYER;    this.count = 0;    this.updateList = new ArrayList<Object>();}
```

首先是它的成员变量和构造函数，有一个初始的 hachcode 和乘数，同时维护了一个内部的 updatelist。在 CacheKey 的 update 方法中，会进行一个 hashcode 和 checksum 的计算，同时把传入的参数添加进 updatelist 中。如下代码所示。

```
public void update(Object object) {    int baseHashCode = object == null ? 1 : ArrayUtil.hashCode(object);     count++;    checksum += baseHashCode;    baseHashCode *= count;    hashcode = multiplier * hashcode + baseHashCode;        updateList.add(object);}
```

我们是如何判断 CacheKey 相等的呢，在 CacheKey 的 equals 方法中给了我们答案，代码如下所示。

```
@Overridepublic boolean equals(Object object) {    .............    for (int i = 0; i < updateList.size(); i++) {      Object thisObject = updateList.get(i);      Object thatObject = cacheKey.updateList.get(i);      if (!ArrayUtil.equals(thisObject, thatObject)) {        return false;      }    }    return true;}
```

除去 hashcode，checksum 和 count 的比较外，只要 updatelist 中的元素一一对应相等，那么就可以认为是 CacheKey 相等。只要两条 Sql 的下列五个值相同，即可以认为是相同的 Sql。

> Statement Id + Offset + Limmit + Sql + Params

BaseExecutor 的 query 方法继续往下走，代码如下所示。

```
list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;if (list != null) {    // 这个主要是处理存储过程用的。    handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);    } else {    list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);}
```

如果查不到的话，就从数据库查，在 queryFromDatabase 中，会对 localcache 进行写入。

在 query 方法执行的最后，会判断一级缓存级别是否是 STATEMENT 级别，如果是的话，就清空缓存，这也就是 STATEMENT 级别的一级缓存无法共享 localCache 的原因。代码如下所示。

```
if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {        clearLocalCache();}
```

在源码分析的最后，我们确认一下，如果是 insert/delete/update 方法，缓存就会刷新的原因。SqlSession 的 insert 方法和 delete 方法，都会统一走 update 的流程，代码如下所示。

```
@Overridepublic int insert(String statement, Object parameter) {    return update(statement, parameter);  }   @Override  public int delete(String statement) {    return update(statement, null);}
```

update 方法也是委托给了 Executor 执行。BaseExecutor 的执行方法如下所示。

```
@Overridepublic int update(MappedStatement ms, Object parameter) throws SQLException {    ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());    if (closed) {      throw new ExecutorException("Executor was closed.");    }    clearLocalCache();    return doUpdate(ms, parameter);}
```

每次执行 update 前都会清空 localCache。

至此，一级缓存的工作流程讲解以及源码分析完毕。

#### 3.3 总结

1.  Mybatis 一级缓存的生命周期和 SqlSession 一致。
    
2.  Mybatis 的缓存是一个粗粒度的缓存，没有更新缓存和缓存过期的概念，同时只是使用了默认的 hashmap，也没有做容量上的限定。
    
3.  Mybatis 的一级缓存最大范围是 SqlSession 内部，有多个 SqlSession 或者分布式的环境下，有操作数据库写的话，会引起脏数据，建议是把一级缓存的默认级别设定为 Statement，即不使用一级缓存。
    

### 4. 二级缓存

#### 4.1 二级缓存介绍

在上文中提到的一级缓存中，其最大的共享范围就是一个 SqlSession 内部，那么如何让多个 SqlSession 之间也可以共享缓存呢，答案是二级缓存。当开启二级缓存后，会使用 CachingExecutor 装饰 Executor，在进入后续执行前，先在 CachingExecutor 进行二级缓存的查询，具体的工作流程如下所示。

![](http://mmbiz.qpic.cn/mmbiz_png/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhFxPZGuWMFB4GwCiamG8JQ5mpibtxnDCntFfkN4O8iaewZfK2zxDibLt5BA/0?)在二级缓存的使用中，一个 namespace 下的所有操作语句，都影响着同一个 Cache，即二级缓存是被多个 SqlSession 共享着的，是一个全局的变量。当开启缓存后，数据的查询执行的流程就是 二级缓存 -> 一级缓存 -> 数据库。

#### 4.2 二级缓存配置

要正确的使用二级缓存，需完成如下配置的。1 在 Mybatis 的配置文件中开启二级缓存。

```
<setting />
```

2 在 Mybatis 的映射 XML 中配置 cache 或者 cache-ref 。

```
<cache/>  
```

cache 标签用于声明这个 namespace 使用二级缓存，并且可以自定义配置。

*   type: cache 使用的类型，默认是 PerpetualCache，这在一级缓存中提到过。
    
*   eviction: 定义回收的策略，常见的有 FIFO，LRU。
    
*   flushInterval: 配置一定时间自动刷新缓存，单位是毫秒
    
*   size: 最多缓存对象的个数
    
*   readOnly: 是否只读，若配置可读写，则需要对应的实体类能够序列化。
    
*   blocking: 若缓存中找不到对应的 key，是否会一直 blocking，直到有对应的数据进入缓存。
    

```
<cache-ref namespace="mapper.StudentMapper"/>
```

cache-ref 代表引用别的命名空间的 Cache 配置，两个命名空间的操作使用的是同一个 Cache。  

#### 4.3 二级缓存实验

在本章节，通过实验，了解 Mybatis 二级缓存在使用上的一些特点。在本实验中，id 为 1 的学生名称初始化为点点。  

实验 1  

测试二级缓存效果，不提交事务，sqlSession1 查询完数据后，sqlSession2 相同的查询是否会从缓存中获取数据。

```
@Testpublic void testCacheWithoutCommitOrClose() throws Exception {        SqlSession sqlSession1 = factory.openSession(true);         SqlSession sqlSession2 = factory.openSession(true);                 StudentMapper studentMapper = sqlSession1.getMapper(StudentMapper.class);        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);        System.out.println("studentMapper读取数据: " + studentMapper.getStudentById(1));        System.out.println("studentMapper2读取数据: " + studentMapper2.getStudentById(1));}
```

执行结果:![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhOVILyWyvVZYBBMj2ybcFYcia4zWkKMsUTqTgWaUd4Tcbkiaj1WtYoJxw/0?) 我们可以看到，当 sqlsession 没有调用 commit( ) 方法时，二级缓存并没有起到作用。

实验 2

测试二级缓存效果，当提交事务时，sqlSession1 查询完数据后，sqlSession2 相同的查询是否会从缓存中获取数据。

```
@Testpublic void testCacheWithCommitOrClose() throws Exception {        SqlSession sqlSession1 = factory.openSession(true);         SqlSession sqlSession2 = factory.openSession(true);                 StudentMapper studentMapper = sqlSession1.getMapper(StudentMapper.class);        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);        System.out.println("studentMapper读取数据: " + studentMapper.getStudentById(1));        sqlSession1.commit();        System.out.println("studentMapper2读取数据: " + studentMapper2.getStudentById(1));}
```

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhTJibkRdXgygPnj9yMTuzWxGick9WzJiav2OKicRx8WzvwLOvMbgSMUwhTw/0?)从图上可知，sqlsession2 的查询，使用了缓存，缓存的命中率是 0.5。

##### 实验 3

测试 update 操作是否会刷新该 namespace 下的二级缓存。

```
@Testpublic void testCacheWithUpdate() throws Exception {        SqlSession sqlSession1 = factory.openSession(true);         SqlSession sqlSession2 = factory.openSession(true);         SqlSession sqlSession3 = factory.openSession(true);                 StudentMapper studentMapper = sqlSession1.getMapper(StudentMapper.class);        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);        StudentMapper studentMapper3 = sqlSession3.getMapper(StudentMapper.class);                System.out.println("studentMapper读取数据: " + studentMapper.getStudentById(1));        sqlSession1.commit();        System.out.println("studentMapper2读取数据: " + studentMapper2.getStudentById(1));                studentMapper3.updateStudentName("方方",1);        sqlSession3.commit();        System.out.println("studentMapper2读取数据: " + studentMapper2.getStudentById(1));}
```

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhyecOsRxe37qLDI5QdF4QtWUB3mibABC8JCbAak43LzQwficKLSMyN4yg/0?)

我们可以看到，在 sqlSession3 更新数据库，并提交事务后，sqlsession2 的 StudentMapper namespace 下的查询走了数据库，没有走 Cache。

##### 实验 4

验证 Mybatis 的二级缓存不适应用于映射文件中存在多表查询的情况。一般来说，我们会为每一个单表创建一个单独的映射文件，如果存在涉及多个表的查询的话，由于 Mybatis 的二级缓存是基于 namespace 的，多表查询语句所在的 namspace 无法感应到其他 namespace 中的语句对多表查询中涉及的表进行了修改，引发脏数据问题。

```
@Testpublic void testCacheWithDiffererntNamespace() throws Exception {        SqlSession sqlSession1 = factory.openSession(true);         SqlSession sqlSession2 = factory.openSession(true);         SqlSession sqlSession3 = factory.openSession(true);             StudentMapper studentMapper = sqlSession1.getMapper(StudentMapper.class);        StudentMapper studentMapper2 = sqlSession2.getMapper(StudentMapper.class);        ClassMapper classMapper = sqlSession3.getMapper(ClassMapper.class);                System.out.println("studentMapper读取数据: " + studentMapper.getStudentByIdWithClassInfo(1));        sqlSession1.close();        System.out.println("studentMapper2读取数据: " + studentMapper2.getStudentByIdWithClassInfo(1));        classMapper.updateClassName("特色一班",1);        sqlSession3.commit();        System.out.println("studentMapper2读取数据: " + studentMapper2.getStudentByIdWithClassInfo(1));}
```

执行结果:![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhrZDBpB30icuxvfDw4E1IdBtXiaKK2PIrS2FLgRS8KGdJalrcJoibcbffg/0?)

在这个实验中，我们引入了两张新的表，一张 class，一张 classroom。class 中保存了班级的 id 和班级名，classroom 中保存了班级 id 和学生 id。

我们在 StudentMapper 中增加了一个查询方法 getStudentByIdWithClassInfo，用于查询学生所在的班级，涉及到多表查询。在 ClassMapper 中添加了 updateClassName，根据班级 id 更新班级名的操作。

当 sqlsession1 的 studentmapper 查询数据后，二级缓存生效。保存在 StudentMapper 的 namespace 下的 cache 中。

当 sqlSession3 的 classMapper 的 updateClassName 方法对 class 表进行更新时，updateClassName 不属于 StudentMapper 的 namespace，所以 StudentMapper 下的 cache 没有感应到变化，没有刷新缓存。

当 StudentMapper 中同样的查询再次发起时，从缓存中读取了脏数据。

##### 实验 5

为了解决 实验 4 的问题呢，可以使用 Cache ref，让 ClassMapper 引用 StudenMapper 命名空间，这样两个映射文件对应的 Sql 操作都使用的是同一块缓存了。执行结果:

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhFKjEeoPBCTBKCdFatCCBJu34wOjwtuNXnICaWwbv2baZR2LVhR04aA/0?)不过这样做的后果是，缓存的粒度变粗了，多个 Mapper namespace 下的所有操作都会对缓存使用造成影响，其实这个缓存存在的意义已经不大了。

#### 4.4 二级缓存源码分析

Mybatis 二级缓存的工作流程和前文提到的一级缓存类似，只是在一级缓存处理前，用 CachingExecutor 装饰了 BaseExecutor 的子类，实现了缓存的查询和写入功能，所以二级缓存直接从源码开始分析。

##### 源码分析

源码分析从 CachingExecutor 的 query 方法展开，源代码走读过程中涉及到的知识点较多，不能一一详细讲解，可以在文后留言，我会在交流环节更详细的表示出来。CachingExecutor 的 query 方法，首先会从 MappedStatement 中获得在配置初始化时赋予的 cache。

```
Cache cache = ms.getCache();
```

本质上是装饰器模式的使用，具体的执行链是 SynchronizedCache -> LoggingCache -> SerializedCache -> LruCache -> PerpetualCache。

![](http://mmbiz.qpic.cn/mmbiz_jpg/NgXdSVnfc6vpHiaKSgN58QZG1B0KXQJZhD5FHjwUibeyd0piaRPnpiaM66CzCAK9bp1iaaC4dBJohSaknADZDygYQzA/0?)以下是具体这些 Cache 实现类的介绍，他们的组合为 Cache 赋予了不同的能力。

*   SynchronizedCache: 同步 Cache，实现比较简单，直接使用 synchronized 修饰方法。
    
*   LoggingCache: 日志功能，装饰类，用于记录缓存的命中率，如果开启了 DEBUG 模式，则会输出命中率日志。
    
*   SerializedCache: 序列化功能，将值序列化后存到缓存中。该功能用于缓存返回一份实例的 Copy，用于保存线程安全。
    
*   LruCache: 采用了 Lru 算法的 Cache 实现，移除最近最少使用的 key/value。
    
*   PerpetualCache: 作为为最基础的缓存类，底层实现比较简单，直接使用了 HashMap。
    

然后是判断是否需要刷新缓存，代码如下所示。

```
flushCacheIfRequired(ms);
```

在默认的设置中 SELECT 语句不会刷新缓存，insert / update / delte 会刷新缓存。进入该方法。代码如下所示。

```
private void flushCacheIfRequired(MappedStatement ms) {    Cache cache = ms.getCache();    if (cache != null && ms.isFlushCacheRequired()) {            tcm.clear(cache);    }}
```

Mybatis 的 CachingExecutor 持有了 TransactionalCacheManager，即上述代码中的 tcm。TransactionalCacheManager 中持有了一个 Map，代码如下所示。

```
private Map<Cache, TransactionalCache> transactionalCaches = new HashMap<Cache, TransactionalCache>();
```

这个 Map 保存了 Cache 和用 TransactionalCache 包装后的 Cache 的映射关系。  

TransactionalCache 实现了 Cache 接口，CachingExecutor 会默认使用他包装初始生成的 Cache，作用是如果事务提交，对缓存的操作才会生效，如果事务回滚或者不提交事务，则不对缓存产生影响。

在 TransactionalCache 的 clear，有以下两句。清空了需要在提交时加入缓存的列表，同时设定提交时清空缓存，代码如下所示。

```
@Overridepublic void clear() {    clearOnCommit = true;    entriesToAddOnCommit.clear();}
```

CachingExecutor 继续往下走，ensureNoOutParams 主要是用来处理存储过程的，暂时不用考虑。  

```
if (ms.isUseCache() && resultHandler == null) {ensureNoOutParams(ms, parameterObject, boundSql);
```

之后会尝试从 tcm 中获取缓存的列表。

```
List<E> list = (List<E>) tcm.getObject(cache, key);
```

在 getObject 方法中，会把获取值的职责一路向后传，最终到 PerpetualCache。如果没有查到，会把 key 加入 Miss 集合，这个主要是为了统计命中率。

```
Object object = delegate.getObject(key);if (object == null) {    entriesMissedInCache.add(key);}
```

CachingExecutor 继续往下走，如果查询到数据，则调用 tcm.putObject 方法，往缓存中放入值。

```
if (list == null) {list = delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);    tcm.putObject(cache, key, list); // issue #578 and #116}
```

tcm 的 put 方法也不是直接操作缓存，只是在把这次的数据和 key 放入待提交的 Map 中。

```
@Overridepublic void putObject(Object key, Object object) {    entriesToAddOnCommit.put(key, object);}
```

从以上的代码分析中，我们可以明白，如果不调用 commit 方法的话，由于 TranscationalCache 的作用，并不会对二级缓存造成直接的影响。因此我们看看 Sqlsession 的 commit 方法中做了什么。代码如下所示。

```
@Overridepublic void commit(boolean force) {try {executor.commit(isCommitOrRollbackRequired(force));
```

因为我们使用了 CachingExecutor，首先会进入 CachingExecutor 实现的 commit 方法。

```
@Overridepublic void commit(boolean required) throws SQLException {    delegate.commit(required);    tcm.commit();}
```

会把具体 commit 的职责委托给包装的 Executor。主要是看下 tcm.commit( )，tcm 最终又会调用到 TrancationalCache。

```
public void commit() { if (clearOnCommit) { delegate.clear(); } flushPendingEntries(); reset();}
```

看到这里的 clearOnCommit 就想起刚才 TrancationalCache 的 clear 方法设置的标志位，真正的清理 Cache 是放到这里来进行的。具体清理的职责委托给了包装的 Cache 类。之后进入 flushPendingEntries 方法。代码如下所示。

```
private void flushPendingEntries() {    for (Map.Entry<Object, Object> entry : entriesToAddOnCommit.entrySet()) {      delegate.putObject(entry.getKey(), entry.getValue());    }    ................}
```

在 flushPendingEntries 中，就把待提交的 Map 循环后，委托给包装的 Cache 类，进行 putObject 的操作。后续的查询操作会重复执行这套流程。

如果是 insert | update | delete 的话，会统一进入 CachingExecutor 的 update 方法，其中调用了这个函数，代码如下所示，因此不再赘述。

```
private void flushCacheIfRequired(MappedStatement ms)
```

#### 总结

1.  Mybatis 的二级缓存相对于一级缓存来说，实现了 SqlSession 之间缓存数据的共享，同时粒度更加的细，能够到 Mapper 级别，通过 Cache 接口实现类不同的组合，对 Cache 的可控性也更强。
    
2.  Mybatis 在多表查询时，极大可能会出现脏数据，有设计上的缺陷，安全使用的条件比较苛刻。
    
3.  在分布式环境下，由于默认的 Mybatis Cache 实现都是基于本地的，分布式环境下必然会出现读取到脏数据，需要使用集中式缓存将 Mybatis 的 Cache 接口实现，有一定的开发成本，不如直接用 Redis，Memcache 实现业务上的缓存就好了。
    

### 全文总结

本文介绍了 Mybatis 的基础概念，Mybatis 一二级缓存的使用及源码分析，并对于一二级缓存进行了一定程度上的总结。

最终的结论是 Mybatis 的缓存机制设计的不是很完善，在使用上容易引起脏数据问题，个人建议不要使用 Mybatis 缓存，在业务层面上使用其他机制实现需要的缓存功能，让 Mybatis 老老实实做它的 ORM 框架就好了哈哈。

![](http://mmbiz.qpic.cn/mmbiz_png/NgXdSVnfc6tz9ibfEP3e1ghrwBUyBeG9PE8PLskbHApNZB7KrQC2QgJFbX7npic4EulxSNLuXicrfpQFf0AHVqpSQ/0?)

![](http://mmbiz.qpic.cn/mmbiz_jpg/0vU1ia3htaaN53ZLaehWt3aH170BVicZGYR261JmsHBh4lBLicuw3eW5BGqJzxgomMLXzT0WIpUXrPZh4pF5Fa2nA/0?wx_fmt=jpeg)

「阅读原文」查看本场 Chat 的交流实录