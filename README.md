# study
# 工作中学习的小案例

### 简单实现一个中间数据的传输层
- 基于ArrayBlockingQueue
- 通过自定义的格式进行数据的写入和写出

### mysql load 方式导入大批量的数据
- 创建测试表
- 实现自定义的inputStream
- 通过load方式导入
- 借助中间传输层实现一个小demo

### netty实现im案例
- 客户端服务端启动类
- 定义通信协议
- 实现序列化和编解码
- 拆包粘包的实现
- 互聊
- 群聊
- 心跳


### 线程池的使用
- 自定义线程池


### Mysql BinLog
- https://github.com/shyiko/mysql-binlog-connector-java
- 原理:
````

 数据库为了主从复制结构和容灾，都会有一份提交日志 (commit log)，通过解析这份日志，
 理论上说可以获取到每次数据库的数据更新操作。获取到这份日志有两种方式：
 在 MySQL server 上通过外部程序监听磁盘上的 binlog 日志文件
 借助于 MySQL 的 Master-Slave 结构，使用程序伪装成一个单独的 Slave，通过网络获取到 MySQL 的binlog 日志流
 
 这里有一个注意的点： MySQL 的 binlog 支持三种格式：Statement 、 Row 和 Mixed 格式：
 
 
 Statement 格式就是说日志中记录 Master 执行的 SQL
 
 Row 格式就是说每次将更改的数据记录到日志中
 
 Mixed 格式就是让 Master 自主决定是使用 Row 还是 Statement 格式
 
 由于伪装成 Slave 的解析程序很难像 MySQL slave 一样通过 Master 执行的 SQL 来获取数据更新，因此要将 MySQL Master 的 binlog 格式调整成 Row 格式才方便实现数据更新获取服务
 
 
 binlog 状态维护模块
 在 MySQL 中， Master-slave 之间只用标识：
 
 serverId：master一般设置为1， 各个 server 之间必须不同
 binlog 文件名称：当前读取到了哪一个 binlog 文件
 binlog position：当前读取的 binlog 文件的位置
 
 由于同步服务会重启，因此必须自行维护 binlog 的状态。一般存储到 MySQL 或者 Zookeeper 中。当服务重启后，自动根据存储的 binlog 位置，继续同步数据
 
````


### 算法
- 排序算法
- 二分查找


### kafka 学习 版本 0.9.0.1
- 生产者
- 三种语义的消费模型
- 参数 max.partition.fetch.bytes,batch.size
- 多线程消费

### es的使用  新闻搜索案列
- 索引mapping 建立
- api的使用
- 修改了结构后，重新索引数据
- 别名的使用
````
创建别名
PUT /comet_v1/_alias/comet

查看信息
GET /*/_alias/comet

重新索引别名
POST /_aliases
{
    "actions": [
        { "remove": { "index": "comet_v1", "alias": "comet" }},
        { "add":    { "index": "comet_v2", "alias": "comet" }}
    ]
} 
````

### wait notify VS lock&condition
- 锁使用的变化学习
````
条件队列可以让一组线程（叫做：等待集wait set）以某种方式等待相关条件变为真，条件队列的元素不同于一般的队列，
一般队列的元素是数据项，条件队列的元素是线程。每个java对象都有一个内部锁，同时还有一个内部条件队列。
一个对象的内部锁和内部条件队列是关联在一块的。Object.wait会自动释放锁，并请求os挂起当前线程，
这样就给其他线程获得锁并修改对象状态的机会，当线程被唤醒以后，它会重新去获取锁。调用wait以后，
线程就进入了对象的内部条件队列里面等待，调用notify以后，就从对象的内部条件队列里面选择一个等待线程，唤醒。 
因为会有多个线程因为不同的原因在同一个条件队列中等待，因此，用notify而不用notifyAll是危险的！有的线程是在take()的时候阻塞，
它等待的条件是队列不空，有的线程是在put()的时候阻塞，它等待的条件是队列非满。 
如果调用了take()以后notify的是总是阻塞在take上的线程，就挂了！

从空变为了非空，唤醒的应该是那些阻塞在take()上的，从满变为了不满唤醒的应该是那些阻塞在put()上的线程，
而notifyAll会把所有条件队列里面的所有的等待的线程全部唤醒，
这就显现出了内部条件队列有一个缺陷：内部锁只能有一个与之关联的条件队列。显式的condition的出现就是为了解决这个问题。

正如Lock提供了比内部锁更丰富的特征一样，condition也提供了比内部条件队列更丰富更灵活的功能。
一个lock可以有多个condition，一个condition只关联到一个Lock。
````


### 工具类使用
- shell
- resources

### Mysql索引
````
- 索引常用的操作
新增一个普通索引
alter table faya_job_log add index index_job_id (job_id) ;

删除索引
drop index index_job_id on faya_job_log ;

查询表存在的索引
SHOW INDEX FROM faya_job_log;

- EXPLAIN命令
先了解 这个命令具体的返回
EXPLAIN select * from faya_job_log;

id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra 
......

一般我会关注几个字段
rows：扫描行的数量，就是这个sql执行会扫描的数据行数，行数越大意味着查询会越慢。

type:代表MySQL在表中找到所需行的方式，常见的如下，性能由差到最好：

type=ALL，全表扫描，MySQL遍历全表来找到匹配行
type=index，索引全扫描，MySQL遍历整个索引来查询匹配行，并不会扫描表
type=range	索引范围扫描
type=ref	非唯一索引扫描
type=eq_ref	唯一索引扫描
type=const,system	单表最多有一个匹配行,出现在根据主键primary key或者 唯一索引 unique index 进行的查询


possible_keys: 表示查询可能使用的索引

key: 实际使用的索引
````