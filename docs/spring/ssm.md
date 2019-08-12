# 框架

## ssm

### ssm 目录结构

```java
├──pom.xml
├──main
|  ├──java
|  |  └──com
|  |     └──web
|  |        ├── core
|  |        |  ├── controller                      //控制器包
|  |        |  ├── entity                          //POJO包
|  |        |  ├── mapper                          //Mapper接口包
|  |        |  ├── service                         //Service接口包
|  |        |  └── serviceImpl                     //service实现类包
|  |        └──util                                //工具包 
|  ├── resources                                    //资源文件夹（配置文件）
|  |  ├──applicationContext.xml                 //Spring配置文件
|  |  ├──dbconfig.properties                        //数据库配置文件
|  |  ├──log4j.properties                       //log4j配置文件
|  |  ├──mybatis-config.xml                     //mybatis配置文件
|  |  ├──spring-mvc.xml                         //springMvc配置文件
|  |  ├──spring-mybatis.xml                     //spring-mybatis整合配置
|  |  └── mapping                               //mapper.xml文件夹
|  |     └── StudentMapper.xml
|  └── webapp                                   ///web应用部署根目录
|     ├──login.html                             //登录页
|     ├──pages                                  //jsp文件将爱
|     |  └── studentList.jsp
|     ├──static                                 //静态资源文件夹
|     |  ├──css
|     |  |  └── login.css
|     |  ├──images
|     |  |  ├──login-img.png
|     |  |  └── login_logo.png
|     |  └── js
|     |     └── JQuery.js
|     └── WEB-INF                               //Java的WEB应用的安全目录
|        └── web.xml
└──test
  ├── java
  ├── resources
```

### 具体文件作用

```java
// 1. controller
// 告诉spring mvc这是一个控制器类
@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
 
    @RequestMapping("listCategory")
    public ModelAndView listCategory(){
        ModelAndView mav = new ModelAndView();
        List<Category> cs= categoryService.list();
         
        // 放入转发参数
        mav.addObject("cs", cs);
        // 放入jsp路径
        mav.setViewName("listCategory");
        return mav;
    }
 
}
// 2. 服务层
@Service
public class CategoryServiceImpl  implements CategoryService{
    @Autowired
    CategoryMapper categoryMapper;
     
    public List<Category> list(){
        return categoryMapper.list();
    }
}
// 3. 服务接口层
public interface CategoryService {
    List<Category> list();
}
// 4. mapper 类

// 5. mapper 数据库查询辅助类


// 2. mapper

```

## spring boot

> 是 spring 的简化部署版本

```java
// 1. application.properties
// 配置文件
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
server.port=8888
server.context-path=/test
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
// PA(Java Persistence API)是Sun官方提出的Java持久化规范，用来方便大家操作数据库。
// 真正干活的可能是Hibernate,TopLink等等实现了JPA规范的不同厂商,默认是Hibernate。
spring.jpa.properties.hibernate.hbm2ddl.auto=update
// 2. pom.xml
<!-- mysql-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.21</version>
</dependency>
// 3. 实体类
// @Entity 注解表示这是个实体类
@Entity
// @Table(name = "category_") 表示这个类对应的表名是 category_ ，注意有下划线哦
@Table(name = "category_")
public class Category {
// @Id 表明主键
// @GeneratedValue(strategy = GenerationType.IDENTITY) 表明自增长方式
// @Column(name = "id") 表明对应的数据库字段名
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
     
    @Column(name = "name")
    private String name;
    public int getId() {
        return id;
    }
}
// 4. Dao 层
// 创建dao接口CategoryDAO，继承了JpaRepository，这个父接口，就提供了CRUD, 分页等等一系列的查询了，直接拿来用，都不需要二次开发的了。
public interface CategoryDAO extends JpaRepository<Category,Integer>{
 
}
// 5. controller ,注解 @RestController 是spring4里的新注解，是@ResponseBody和@Controller的缩写
@Controller
public class CategoryController {
    @Autowired CategoryDAO categoryDAO;
     
    @RequestMapping("/listCategory")
    public String listCategory(Model m) throws Exception {
        List<Category> cs=categoryDAO.findAll();   
        m.addAttribute("cs", cs);
        // 返回 listCategory.jsp
        return "listCategory";
    }
}
```

### 分页

```java
// 1. pom.xml 增加 pageHelper 支持
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
	<version>4.1.6</version>
</dependency>
// 2. 配置类
// 注解@Configuration 表示PageHelperConfig 这个类是用来做配置的。
// 注解@Bean 表示启动PageHelper这个拦截器。
@Configuration
public class PageHelperConfig {
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
// controller
@RequestMapping("/listCategory")
    public String listCategory(Model m,@RequestParam(value = "start", defaultValue = "0") int start,@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        PageHelper.startPage(start,size,"id desc");
        List<Category> cs=categoryMapper.findAll();
        PageInfo<Category> page = new PageInfo<>(cs);
        m.addAttribute("page", page);        
        return "listCategory";
    }
```

### Restful

> 既然method值如此丰富，那么就可以考虑使用同一个url，但是约定不同的method来实施不同的业务，这就是Restful的基本考虑。

#	| 传统风格| #|Restful风格 |	#
---|---|---|---|---
# |url	|method|	url|	method
增加|	/addCategory?name=xxx	|POST	|/categories	|POST
删除|	/deleteCategory?id=123	|GET	|/categories/123	|DELETE
修改|	/updateCategory?id=123&name=yyy	|POST	|/categories/123	|PUT
获取|	/getCategory?id=123	|GET	|/categories/123	|GET
查询|	/listCategory	|GET	|/categories |	

```java
// 只需要更改 controller 就可以了
@Controller
public class CategoryController {
    @Autowired CategoryDAO categoryDAO;
     
    @GetMapping("/categories")
    public String listCategory(Model m,@RequestParam(value = "start", defaultValue = "0") int start,@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> page =categoryDAO.findAll(pageable);
        m.addAttribute("page", page);
        return "listCategory";
    }
 
    @PostMapping("/categories")
    public String addCategory(Category c) throws Exception {
        categoryDAO.save(c);
        return "redirect:/categories";
    }
    @DeleteMapping("/categories/{id}")
    public String deleteCategory(Category c) throws Exception {
        categoryDAO.delete(c);
        return "redirect:/categories";
    }
    @PutMapping("/categories/{id}")
    public String updateCategory(Category c) throws Exception {
        categoryDAO.save(c);
        return "redirect:/categories";
    }
    @GetMapping("/categories/{id}")
    public String getCategory(@PathVariable("id") int id,Model m) throws Exception {
        Category c= categoryDAO.getOne(id);
        m.addAttribute("c", c);
        return "editCategory";
    }
    /*页面跳转 部分*/
    @RequestMapping(value="/listHero", method=RequestMethod.GET)
    public ModelAndView listHero(){
        ModelAndView mv = new ModelAndView("listHero");
        return mv;
    }
}
// js 调用

var vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list(1);
        },       
        methods: {     
            list:function(start){
                var url = "heroes?start="+start;
                // ajax 或者 axios 调用 url
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    console.log(vue.pagination);
                    vue.heros = response.data.list;
                })    
            },
        }
}
```

## redis

### redis 常用命令

1. 五种数据类型
   1. String（字符串）
   2. List（列表）
   3. Hash（字典）
   4. Set（集合）
   5. Sorted Set（有序集合）
2. Jedis
   
```java
public class TestRedisManyCommands { 
    JedisPool pool; 
    Jedis jedis; 
    @Before 
    public void setUp() { 
   
        jedis = new Jedis("localhost"); 
           
    } 
 
    /**
     * Redis存储初级的字符串
     * CRUD
     */ 
    @Test 
    public void testBasicString(){ 
        //-----添加数据---------- 
        jedis.set("name","meepo");//向key-->name中放入了value-->meepo 
        System.out.println(jedis.get("name"));//执行结果：meepo 
 
        //-----修改数据----------- 
        //1、在原来基础上修改 
        jedis.append("name","dota");   //很直观，类似map 将dota append到已经有的value之后 
        System.out.println(jedis.get("name"));//执行结果:meepodota 
 
        //2、直接覆盖原来的数据 
        jedis.set("name","poofu"); 
        System.out.println(jedis.get("name"));//执行结果：poofu 
 
        //删除key对应的记录 
        jedis.del("name"); 
        System.out.println(jedis.get("name"));//执行结果：null 
 
        /**
         * mset相当于
         * jedis.set("name","meepo");
         * jedis.set("dota","poofu");
         */ 
        jedis.mset("name","meepo","dota","poofu"); 
        System.out.println(jedis.mget("name","dota")); 
 
    } 
 
    /**
     * jedis操作Map
     */ 
    @Test 
    public void testMap(){ 
        Map<String,String> user=new HashMap<String,String>(); 
        user.put("name","meepo"); 
        user.put("pwd","password"); 
        jedis.hmset("user",user); 
        //取出user中的name，执行结果:[meepo]-->注意结果是一个泛型的List 
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数 
        List<String> rsmap = jedis.hmget("user", "name"); 
        System.out.println(rsmap); 
 
        //删除map中的某个键值 
//        jedis.hdel("user","pwd"); 
        System.out.println(jedis.hmget("user", "pwd")); //因为删除了，所以返回的是null 
        System.out.println(jedis.hlen("user")); //返回key为user的键中存放的值的个数1 
        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true 
        System.out.println(jedis.hkeys("user"));//返回map对象中的所有key  [pwd, name] 
        System.out.println(jedis.hvals("user"));//返回map对象中的所有value  [meepo, password] 
 
        Iterator<String> iter=jedis.hkeys("user").iterator(); 
        while (iter.hasNext()){ 
            String key = iter.next(); 
            System.out.println(key+":"+jedis.hmget("user",key)); 
        } 
 
    } 
 
    /**
     * jedis操作List
     */ 
    @Test 
    public void testList(){ 
        //开始前，先移除所有的内容 
        jedis.del("java framework"); 
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(jedis.lrange("java framework",0,-1)); 
       //先向key java framework中存放三条数据 
       jedis.lpush("java framework","spring"); 
       jedis.lpush("java framework","struts"); 
       jedis.lpush("java framework","hibernate"); 
       //再取出所有数据jedis.lrange是按范围取出， 
       // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有 
       System.out.println(jedis.lrange("java framework",0,-1)); 
    } 
 
    /**
     * jedis操作Set
     */ 
    @Test 
    public void testSet(){ 
        //添加 
        jedis.sadd("sname","meepo"); 
        jedis.sadd("sname","dota"); 
        jedis.sadd("sname","poofu"); 
        jedis.sadd("sname","noname");
        //移除noname 
        jedis.srem("sname","noname"); 
        System.out.println(jedis.smembers("sname"));//获取所有加入的value 
        System.out.println(jedis.sismember("sname", "meepo"));//判断 meepo 是否是sname集合的元素 
        System.out.println(jedis.srandmember("sname")); 
        System.out.println(jedis.scard("sname"));//返回集合的元素个数 
    } 
 
    @Test 
    public void test() throws InterruptedException { 
        //keys中传入的可以用通配符 
        System.out.println(jedis.keys("*")); //返回当前库中所有的key  [sose, sanme, name, dota, foo, sname, java framework, user, braand] 
        System.out.println(jedis.keys("*name"));//返回的sname   [sname, name] 
        System.out.println(jedis.del("sanmdde"));//删除key为sanmdde的对象  删除成功返回1 删除失败（或者不存在）返回 0 
        System.out.println(jedis.ttl("sname"));//返回给定key的有效时间，如果是-1则表示永远有效 
        jedis.setex("timekey", 10, "min");//通过此方法，可以指定key的存活（有效时间） 时间为秒 
        Thread.sleep(5000);//睡眠5秒后，剩余时间将为<=5 
        System.out.println(jedis.ttl("timekey"));   //输出结果为5 
        jedis.setex("timekey", 1, "min");        //设为1后，下面再看剩余时间就是1了 
        System.out.println(jedis.ttl("timekey"));  //输出结果为1 
        System.out.println(jedis.exists("key"));//检查key是否存在 
        System.out.println(jedis.rename("timekey","time")); 
        System.out.println(jedis.get("timekey"));//因为移除，返回为null 
        System.out.println(jedis.get("time")); //因为将timekey 重命名为time 所以可以取得值 min 
 
        //jedis 排序 
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的） 
        jedis.del("a");//先清除数据，再加入数据进行测试 
        jedis.rpush("a", "1"); 
        jedis.lpush("a","6"); 
        jedis.lpush("a","3"); 
        jedis.lpush("a","9"); 
        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1] 
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果 
        System.out.println(jedis.lrange("a",0,-1)); 
 
    } 
}
```
3. spring boot 配置redis




