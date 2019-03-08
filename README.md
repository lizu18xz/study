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
 
 
 //    Write---------------
 //    WriteRowsEventData{tableId=85, includedColumns={0, 1, 2}, rows=[
 //    [10, 10, 宝马]
 //]}
 //    Update--------------
 //    UpdateRowsEventData{tableId=85, includedColumnsBeforeUpdate={0, 1, 2},
 // includedColumns={0, 1, 2}, rows=[
 //        {before=[10, 10, 宝马], after=[10, 11, 宝马]}
 //]}
 //    Delete--------------
 //    DeleteRowsEventData{tableId=85, includedColumns={0, 1, 2}, rows=[
 //    [11, 10, 奔驰]
 //]}
 
 
 //    Write---------------
 //    WriteRowsEventData{tableId=70, includedColumns={0, 1, 2, 3, 4, 5, 6, 7}, rows=[
 //    [12, 10, plan, 1, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019]
 //]}
 
 
````


### 算法
- 排序算法
- 二分查找


### kafka 学习 版本 0.9.0.1
- 生产者
- 三种语义的消费模型
- 参数 max.partition.fetch.bytes,batch.size
- 多线程消费
````
批次管理
批次创建后会逗留linger.ms时间，它集聚该段时间内归属该分组（区）的消息。如果生产速率特别高又或者有超大消息流入很快将分区打满，则实际逗留时间会低于linger.ms。
想象一下极端场景，批次大小默认16k，如果消息以5k、12k间隔发，则内存实际利用率只有（5+12）/(2*16)。

另一方面，积累器挤出前先要做就绪节点检查，挤出动作也只针对leader在这些节点上的分区批次，但节点ready to drain后，可能因为连接或者inflightRequests超限等问题，
被从发送就绪列表移除，从而导致这些节点的可发送批次不会被挤出。它们始终占据分组队列的最高挤出优先级，这会导致：1）后追加的消息被积压，即使连接恢复后新入的消息也只能等待顺序处理，
整体投递延时猛增。2）批次占据的内存得不到释放，有可能发生雪崩：因为只有追加没有挤出，问题节点的批次有可能占满全部内存空间导致其他正常节点分区无法为新批次申请空间。
Kafka提供请求超时timeout.ms解决这个问题，从逗留截止开始计算批次超时则被废弃–释放内存空间并从分组队列移除。

理想状况下，单位时间内追入和挤出应该恰好相等且内存被充分使用。长期观察下调好linger.ms、batch.size、timeout.ms以及batch.size和buffer.memory这几个参数将有助于达到这个目标。

内存管理
消息集内存直接分配在堆上，如果对它不加以限制在消息生产速率足够高时很可能频繁出现fgc乃至oom，另一方面频繁的内存申请和释放操作也很吃系统资源，因此Kafka自建了内存池BufferPool管理内存。

````

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


### Hadoop 服务库和事件库的学习
````
事件驱动
YARN采用了基于事件驱动的并发模型，该模型能够大大增强并发性，从而提高系统整体性能。
Hadoop服务库与事件库的使用及其工作流程
中央异步调度器AsyncDispatcher
hadoop服务库:
YARN采用了基于服务的对象管理模型,主要特点有:
被服务化的对象分4个状态:NOTINITED,INITED,STARTED,STOPED
任何服务状态变化都可以触发另外一些动作
可通过组合方式对任意服务进行组合,统一管理
具体类请参见 org.apache.hadoop.service包下.核心接口是Service,抽象实现是AbstractService
YARN中,ResourceManager和NodeManager属于组合服务,内部包含多个单一和组合服务.以实现对内部多种服务的统一管理.
Hadoop事件库:
YARN采用事件驱动并发模型, 把各种逻辑抽象成事件,进入事件队列,然后由中央异步调度器负责传递给相应的事件调度器处理,或者调度器之间再传递,直至完成任务.
流程:
	创建一个AsyncDispatcher对象
	向AsyncDispatcher中注册枚举类型和事件的处理器(EventHandler)
	然后启动,启动后会创建一个eventHandlingThread线程
	线程执行的流程是，从等待队列中取出事件，从事件对象中获取事件类型，然后根据事件类型获取事件处理器，然后进行事件的处理
	提交任务的流程:
	将任务放入等待的队列，就可以离开了

````

```text
具体源码分析可以参考之前写的一篇文章:
https://www.imooc.com/article/276053
```

### J.U.C  Future使用
````
Future可以看成线程在执行时留给调用者的一个存根，通过这个存在可以进行查看线程执行状态(isDone)、
取消执行(cancel)、阻塞等待执行完成并返回结果(get)、异步执行回调函数(callback)等操作。
自己实现一个ResponseFuture

关于:condition.await()
当前线程调用condition.await()方法后，会使得当前线程释放lock然后加入到等待队列中，
直至被signal/signalAll后会使得当前线程从等待队列中移至到同步队列中去，直到获得了lock后才会从await方法返回，或者在等待时被中断会做中断处理。

调用condition的signal或者signalAll方法可以将等待队列中等待时间最长的节点移动到同步队列中，使得该节点能够有机会获得lock。

线程awaitThread先通过lock.lock()方法获取锁成功后调用了condition.await方法进入等待队列，
而另一个线程signalThread通过lock.lock()方法获取锁成功后调用了condition.signal或者signalAll方法，
使得线程awaitThread能够有机会移入到同步队列中，当其他线程释放lock后使得线程awaitThread能够有机会获取lock，
从而使得线程awaitThread能够从await方法中退出执行后续操作。如果awaitThread获取lock失败会直接进入到同步队列。

````

### 使用docker 搭建web运行环境
````
搭建一个以 Tomcat 为核心的 Web 应用运行环境。 在这个环境中，我们还要组合进 MySQL 作为数据存储，Redis 作为 KV 存储。

app ：用于存放程序工程，即代码、编译结果以及相关的库、工具等；
compose ：用于定义 Docker Compose 项目；
mysql ：与 MySQL 相关配置等内容；
redis ：与 Redis 相关配置等内容；
tomcat ：与 Tomcat 相关配置等内容。






使用docker 打包一个 springboot项目
docker run -idt -p 6379:6379 -v /home/hadoop/images/web_dev/redis/data:/data --name redis -v /home/hadoop/images/web_dev/redis/redis.conf:/etc/redis/redis.conf redis:3.2
````











