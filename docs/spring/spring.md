# spring 

> * Spring是一个基于IOC和AOP的结构J2EE系统的框架 
> * IOC 反转控制 是Spring的基础，Inversion Of Control 
> * 简单说就是创建对象由以前的程序员自己new 构造方法来调用，变成了交由Spring创建对象 
> * DI 依赖注入 Dependency Inject. 简单地说就是拿到的对象的属性，已经被注入好相关值了，直接使用即可

## spring

### ioc di

```java
// 1. xml 文件
    <bean name="c" class="com.how2java.pojo.Category">
        // 注入属性值
        <property name="name" value="category 1" />
    </bean>
    <bean name="p" class="com.how2java.pojo.Product">
        <property name="name" value="product1" />
        // 创建Product的时候注入一个Category对象
        <property name="category" ref="c" />
    </bean>
// 2. 主函数
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
 
        Product p = (Product) context.getBean("p");
 
        System.out.println(p.getName());
        System.out.println(p.getCategory().getName());
    }
```

### 注解 ioc di

1. bean 对象在 xml 中

```java
// 1. 实体类
public class Product {
    private int id;
    // 1.1 添加注解 
    @Autowired // 等价于 @Resource(name="c")
    private Category category;
    public Category getCategory() {
        return category;
    }
    // 1.2 添加注解达到 1.1 的效果
    public void setCategory(Category category) {
        this.category = category;
    }
}
// 2. xml
    <context:annotation-config/>
    <bean name="c" class="com.how2java.pojo.Category">
        <property name="name" value="category 1" />
    </bean>
    <bean name="p" class="com.how2java.pojo.Product">
        <property name="name" value="product1" />
    </bean>
```

2. bean 对象不在 xml

```java
// 1. 实体类
@Component("p")
public class Product {

}
// 2. xml
<beans>
    <context:component-scan base-package="com.how2java.pojo"/>  
</beans>
```

### aop

> AOP 即 Aspect Oriented Program 面向切面编程 
> 首先，在面向切面编程的思想里面，把功能分为核心业务功能，和周边功能。 
> 所谓的核心业务，比如登陆，增加数据，删除数据都叫核心业务 
> 所谓的周边功能，比如性能统计，日志，事务管理等等 
> 周边功能在Spring的面向切面编程AOP思想里，即被定义为切面 
> 在面向切面编程AOP的思想里面，核心业务功能和切面功能分别独立进行开发 
> 然后把切面功能和核心业务功能 "编织" 在一起，这就叫AOP

```java
// 声明业务和切面对象
<bean name="s" class="com.how2java.service.ProductService">
</bean> 
<bean id="loggerAspect" class="com.how2java.aspect.LoggerAspect"/>
// 切面
<aop:config>
    // 声明切入点，切入点 id : loggerCutpoint
    <aop:pointcut id="loggerCutpoint"
        // 满足expression中的方法调用之后，就会去进行切面操作，类似于触发了切面，业务类调用辅助类
        expression="execution(* com.how2java.service.ProductService.*(..)) "/>
    // 定义一个 logAspect 切面，ref 方法所在的类，method 方法    
    <aop:aspect id="logAspect" ref="loggerAspect">
        // 该切面和切入点关联起来
        <aop:after pointcut-ref="loggerCutpoint" method="log"/>
    </aop:aspect>
</aop:config> 

// 业务类
public class ProductService {    
    public void doSomeService(){
        System.out.println("doSomeService");         
    }  
}
// 辅助类
public class LoggerAspect {
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("start log:" + joinPoint.getSignature().getName());
        Object object = joinPoint.proceed();
        System.out.println("end log:" + joinPoint.getSignature().getName());
        return object;
    }
}
// 主函数
public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
        ProductService s = (ProductService) context.getBean("s");
        s.doSomeService();
    }
}
```

### 注解方式 aop

```java
// 业务类
@Component("s")
public class ProductService {
    public void doSomeService(){
        System.out.println("doSomeService");
    }
     
}
// 切面类
@Aspect
@Component
public class LoggerAspect {   
    @Around(value = "execution(* com.how2java.service.ProductService.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("start log:" + joinPoint.getSignature().getName());
        Object object = joinPoint.proceed();
        System.out.println("end log:" + joinPoint.getSignature().getName());
        return object;
    }
}
// xml
<context:component-scan base-package="com.how2java.aspect"/>
<context:component-scan base-package="com.how2java.service"/>
<aop:aspectj-autoproxy/> 
```

### 注解测试

```java
// 1. @RunWith(SpringJUnit4ClassRunner.class) 
// 表示这是一个 Spring的测试类
@RunWith(SpringJUnit4ClassRunner.class)
// 2. @ContextConfiguration("classpath:applicationContext.xml")
// 定位Spring的配置文件
@ContextConfiguration("classpath:applicationContext.xml")
public class TestSpring {
//     3. @Autowired
// 给这个测试类装配Category对象
    @Autowired
    Category c;
//  4. @Test
// 测试逻辑，打印c对象的名称
    @Test
    public void test(){
        System.out.println(c.getName());
    }
}
```

## spring mvc

> 原理：
> 1. 用户访问 /index
> 2. 根据web.xml中的配置 所有的访问都会经过DispatcherServlet
> 3. 根据 根据配置文件springmvc-servlet.xml ，访问路径/index会进入IndexController类
> 4. 在IndexController中指定跳转到页面index.jsp，并传递message数据
> 5. 在index.jsp中显示message信息

1. 普通方法
   
```java
<servlet>
    // 1. 配置 servlet,在 webxml 中
    <servlet-name>springmvc</servlet-name>
    <servlet-class>
        org.springframework.web.servlet.DispatcherServlet
    </servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
// 2. 配置 servlet 文件，映射配置文件
<beans>
    <bean id="simpleUrlHandlerMapping"
        class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
            // 表示访问路径/index会交给id=indexController的bean处理
                <prop key="/index">indexController</prop>
            </props>
        </property>
    </bean>
    // id=indexController的bean配置为类：IndexController
    <bean id="indexController" class="controller.IndexController"></bean>
</beans>
// controller
public class IndexController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("index.jsp");
        mav.addObject("message", "Hello Spring MVC");
        return mav;
    }
}
// jsp文件
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<h1>${message}</h1>
```

2. 注解

```java
// 1. servletxml
// 去 controller 包下面寻找 controller
<context:component-scan base-package="controller" />
<bean id="irViewResolver"
        class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/page/" />
        // 页面后缀设置
        <property name="suffix" value=".jsp" />
    </bean>
// 2. controller
@Controller
public class IndexController {
    @RequestMapping("/index")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("message", "Hello Spring MVC");
        return mav;
    }
    // session 使用
    @RequestMapping("/check")
    public ModelAndView check(HttpSession session) {
        Integer i = (Integer) session.getAttribute("count");
        if (i == null)
            i = 0;
        i++;
        session.setAttribute("count", i);
        ModelAndView mav = new ModelAndView("check");
        return mav;
    }
}

```
## servlet

## mybatis

> 1. 应用程序找Mybatis要数据
> 2. mybatis从数据库中找来数据
>   1. 通过mybatis-config.xml 定位哪个数据库
>   2. 通过Category.xml执行对应的select语句
>   3. 基于Category.xml把返回的数据库记录封装在Category对象中
>   4. 把多个Category对象装在一个Category集合中
> 3. 返回一个Category集合

### 使用

1. 普通方式

```java
// 1. 建立配置文件，mybatis-config.xml
// 1.2 其作用主要是提供连接数据库用的驱动，数据库名称，编码方式，账号密码
<property name="driver" value="com.mysql.jdbc.Driver"/>
<property name="url" value="jdbc:mysql://localhost:3306/how2java?characterEncoding=UTF-8"/>
<property name="username" value="root"/>
<property name="password" value="admin"/>
// 1.2 实体类地址 
<typeAliases>
    <package name="com.how2java.pojo"/>
</typeAliases>
// 1.3 映射 xml 地址
<mappers>
    <mapper resource="com/how2java/pojo/Category.xml"/>
</mappers>

// 2. xml 文件
<mapper namespace="com.how2java.pojo">
    // 这条sql语句用id: listCategory 进行标示以供后续代码调用。resultType="Category" 表示返回的数据和Category关联起来
    <select id="listCategory" resultType="Category">
        select * from   category_     
    </select>
    // 更新
    <update id="updateCategory" parameterType="Category" >
        update category_ set name=#{name} where id=#{id}   
    </update>
</mapper>
// 3.使用

String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
SqlSession session=sqlSessionFactory.openSession

Category c= session.selectOne("getCategory",3);
c.setName("修改了的Category名稱");
// 3.1 调用
session.update("updateCategory",c);
List<Category> cs=session.selectList("listCategory");
for (Category c : cs) {
    System.out.println(c.getName());
}
```

2. 注解方式

```java
// 1. 增加 mapper 接口
public interface CategoryMapper {
  
    @Insert(" insert into category_ ( name ) values (#{name}) ") 
    public int add(Category category); 
        
    @Delete(" delete from category_ where id= #{id} ") 
    public void delete(int id); 
        
    @Select("select * from category_ where id= #{id} ") 
    public Category get(int id); 
      
    @Update("update category_ set name=#{name} where id=#{id} ") 
    public int update(Category category);  
        
    @Select(" select * from category_ ") 
    public List<Category> list(); 

    @InsertProvider(type=CategoryDynaSqlProvider.class,method="add") 
    public int add(Category category); 
}
// 1.1 辅助查询类 CategoryDynaSqlProvider
public class CategoryDynaSqlProvider {
    public String add(){
        return new SQL()
                .INSERT_INTO("category_")
                .VALUES("name", "#{name}")
                .toString();
    }
}
// 2. 修改 mybatis 配置文件
<mapper class="com.how2java.mapper.CategoryMapper"/>

// 3. 使用
String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
SqlSession session = sqlSessionFactory.openSession();
CategoryMapper mapper = session.getMapper(CategoryMapper.class);

Category c= mapper.get(8);
System.out.println(c.getName());
```

### 原理

## 参考

1. [aop 配置](https://www.cnblogs.com/mxck/p/7027912.html?utm_source=itdadao&utm_medium=referral)