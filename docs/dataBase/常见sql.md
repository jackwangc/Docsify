# sql 使用

## 1. 建表

```sql
-- 1. 设计表, 分析需求，画出er图，用例图
--    er 图：实体，属性，关系
-- 2. 创建使用数据库
drop database if exists tmall_ssm;
create table;

create table user (
    id int not null auto_increment,
    name varchar(255) default null,
    -- 2.1 主键建立
    primary key(id)
    -- 2.2 设置存储引擎
) engine = Innodb default charset = utf8;

CREATE TABLE property (
  id int(11) NOT NULL AUTO_INCREMENT,
  cid int(11) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  -- 2.3 建立
  CONSTRAINT fk_property_category FOREIGN KEY (cid) REFERENCES category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## 2. 查询

```sql
select * from student
select sname, ssex from student
select depart from teacher;
-- distinct
-- 必须放在开头
-- 单列，查询单列不重复的
select distinct prof ,tsex from teacher
-- 多列，查询多列不重复的
select distinct prof ,tsex from teacher
-- count 统计去重后的结果，无法统计多列 count
select count(distinct tsex) from teacher
select * from score where degree between 60 and 80
select * from student where class = 'class5' or ssex = 'female'
-- desc 降序
select * from student order by class desc
-- 查询包含最大值的信息
SELECT sno, cno FROM score
WHERE degree = (SELECT MAX(degree) FROM score);
-- group by 的使用要和聚合函数在一块使用才会起作用，否则显示的查询的第一条数据
select count(*) from course  group by cno
-- 左右连接 left join right join
SELECT course.cno, course.cname, AVG(degree) AS degree FROM course LEFT JOIN score ON course.cno = score.cno
 GROUP BY cno;

-- 模糊查询 like
-- having 对结果进行筛选，最后执行
select avg(degree) from score where cno like '3%' group by cno having count(*) >= 5;
```

## 3. 索引

```sql
-- 直接创建普通索引
CREATE INDEX index_name ON table(index_col_name)
-- 修改表结构的方式添加索引
ALTER TABLE table_name ADD INDEX index_name ON (index_col_name)
-- 创建表的时候同时创建索引
CREATE TABLE `table` (
    `id` int(11) NOT NULL AUTO_INCREMENT ,
    `title` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
    `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
    `time` int(10) NULL DEFAULT NULL ,
    PRIMARY KEY (`id`),
    INDEX index_name (index_col_name)
)
-- 删除索引
DROP INDEX index_name ON table
-- 唯一索引，全文索引，空间索引
CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name [USING index_type] ON table_name (index_col_name,...)
-- 查看索引
SHOW INDEX FROM table_name [FROM db_name]
```

## 4. 事务

```sql
-- 开始事务
START TRANSACTION;
-- 插入数据
INSERT INTO customers (cust_name,item_price,item_quantity) VALUES ('1',5,18);
SELECT * FROM customers;
-- 创建保留点
SAVEPOINT insertinto;
INSERT INTO customers (cust_name,item_price,item_quantity) VALUES ('2',5,18);
-- 回滚，使得保留点之前的数据有效
ROLLBACK TO insertinto;
-- 手动提交事务，事务必须进行手动提交才能执行成功
commit

```

## 4. 练习

### 4.1 建表

```sql
use test;
CREATE TABLE IF NOT EXISTS student(
sno TINYINT UNSIGNED  NOT NULL,
sname VARCHAR(20) NOT NULL,
ssex ENUM('male', 'female') DEFAULT 'male',
sbirthday DATE,
class VARCHAR(20) NOT NULL,
PRIMARY KEY(sno)
);
INSERT INTO student VALUES
(1, '阿信', DEFAULT, 19751206, 'class5'),
(2, '怪兽', DEFAULT, 19761128, 'class5'),
(3, '玛莎', DEFAULT, 19770425, 'class5'),
(4, '石头', DEFAULT, 19751211, 'class5'),
(5, '冠佑', DEFAULT, 19730728, 'class5'),
(6, '小马', DEFAULT, 19960628, 'class2'),
(7, '小兰', 'female', 19951126, 'class2'),
(8, '况儿子', DEFAULT, 19960715, 'class4'),
(9, '纯妞', 'female', 19960428, 'class4'),
(10, '豆豆', 'female', 19941211, 'class2');

CREATE TABLE IF NOT EXISTS course(
cno TINYINT UNSIGNED NOT NULL,
cname VARCHAR(20) NOT NULL,
tno TINYINT NOT NULL,
PRIMARY KEY(cno)
);

INSERT INTO course VALUES
(1, '数据结构与算法', 1),
(2, '计算机网络', 2),
(3, '计算机组成原理', 3),
(4, '操作系统', 4);
CREATE TABLE IF NOT EXISTS score(
sno TINYINT UNSIGNED NOT NULL,
cno TINYINT UNSIGNED NOT NULL,
degree DECIMAL(4, 1)
);
INSERT INTO score VALUES
(1, 1, 86),
(1, 2, 75),
(1, 3, 68),
(2, 2, 92),
(2, 3, 88),
(3, 4, 76),
(4, 1, 91),
(5, 1, 40),
(6, 3, 30),
(7, 3, 59),
(8, 4, 66),
(9, 1, 100),
(10, 1, 100),
(6, 1, 66),
(9, 2, 10),
(8, 3, 40),
(7, 1, 77),
(6, 4, 14);

CREATE TABLE IF NOT EXISTS teacher(
tno TINYINT UNSIGNED NOT NULL,
tname VARCHAR(10) NOT NULL,
tsex ENUM('male', 'female') DEFAULT 'male',
tbirthday DATE,
prof VARCHAR(26),
depart VARCHAR(10) NOT NULL,
PRIMARY KEY(tno)
);

INSERT INTO teacher VALUES
(1, '卢本伟', 'male', 19581202, '副教授', '计算机系'),
(2, '五五开', 'male', 19690312, '讲师', '电子工程系'),
(3, '德云色', 'female', 19720505, '助教', '计算机系'),
(4, '卢本皇', 'female', 19770814, '助教', '电子工程系');
```