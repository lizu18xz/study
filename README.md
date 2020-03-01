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


### ReentrantReadWriteLock
````
ReentrantReadWriteLock--顾名思义是可重入的读写锁，允许多个读线程获得ReadLock，但只允许一个写线程获得WriteLock
读写锁的机制：
   "读-读" 不互斥
   "读-写" 互斥
   "写-写" 互斥
	
ReentrantReadWriteLock会使用两把锁来解决问题，一个读锁，一个写锁。
　　线程进入读锁的前提条件：
     　1. 没有其他线程的写锁
　　　　2. 没有写请求，或者有写请求但调用线程和持有锁的线程是同一个线程
　　进入写锁的前提条件：
　　　　1. 没有其他线程的读锁
　　　　2. 没有其他线程的写锁
````


### 责任链模式
````
责任链模式
责任链（chain of responsibility）模式很像异常的捕获和处理，当一个问题发生的时候，当前对象看一下自己是否能够处理，不能的话将问题抛给自己的上级去处理，
但是要注意这里的上级不一定指的是继承关系的父类，这点和异常的处理是不一样的。
所以可以这样说，当问题不能解决的时候，将问题交给另一个对象去处理，就这样一直传递下去直至当前对象找不到下线了，处理结束


责任链使用了模板方法和委托的思想构建了一个链表，通过遍历链表来一个个询问链表中的每一个节点谁可以处理某件事情，如果某个节点能够胜任，则直接处理，否则继续向下传递，如果都不能处理next==null，则处理结束。
责任链会造成处理的时延，但是能够解耦合，提高可扩展性，使得处理方法的类更专注于处理把自己的事情，便于扩展和改变处理的优先级，应用也非常的广泛。
````

### 一致性哈希
````
先构造一个长度为232的整数环（这个环被称为一致性Hash环），根据节点名称的Hash值（其分布为[0, 232-1]）将服务器节点放置在这个Hash环上，然后根据数据的Key值计算得到其Hash值
（其分布也为[0, 232-1]），接着在Hash环上顺时针查找距离这个Key值的Hash值最近的服务器节点，完成Key到服务器的映射查找。

这种算法解决了普通余数Hash算法伸缩性差的问题，可以保证在上线、下线服务器的情况下尽量有多的请求命中原来路由到的服务器。
````

### cpu负载高学习
````
1-代码模拟死循环的情况

1core  2G
load average: 0.16, 0.14, 0.07
load average: 0.11, 0.12, 0.07
load average: 0.09, 0.12, 0.07

2-部署代码之后
load average: 2.20, 1.65, 1.02
load average: 1.14, 1.45, 1.10
load average: 1.20, 1.35, 1.11


4712 root      20   0 2527648 162612  13824 S 99.3  8.7  21:07.32 java   


top -H -p  4712
 4782 root      20   0 2546136 184120  13848 R 85.1  9.8   1:34.16 java                                                                                                                                                                                                      
 4714 root      20   0 2546136 184120  13848 S 14.2  9.8   0:16.34 java 


打印整个进程的
jstack  4712 > 4712.txt

printf "%x" 4712

看到RUNNABLE

"http-nio-8080-exec-1" #18 daemon prio=5 os_prio=0 tid=0x00007f7cc49dc000 nid=0x12ae waiting on condition [0x00007f7c91e34000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.String.substring(String.java:1969)
        at com.fayayo.study.cpuLoad.CpuLoad.cpuLoad(CpuLoad.java:43)
        at com.fayayo.study.cpuLoad.CpuController.load(CpuController.java:29)


````


### 适配器模式分类模式
````
类适配器 (通过引用适配者进行组合实现)
对象适配器(通过继承适配者进行实现)
接口适配器 （通过抽象类来实现适配）

````

### JUC
```text
lock synchronized

synchronized不够灵活
读写锁更灵活

tryLock()方法立刻返回。

可见性保证
happens-before
lock和synchronized有同样的内存语义
下一个线程加锁后可以看到所有前一个线程解锁前发生的所有操作


锁的分类:
一个锁可能有多种类型

互斥同步锁:阻塞和唤醒带来性能劣势
悲观锁 lock synchronized
适合并发写入多：临界区有IO操作
临界区代码复杂或者循环大
竞争激烈


非互斥同步锁
乐观锁:
在更新的时候去检查
基于CAS算法实现：原子类
典型例子:
数据库添加一个字段lock_version
先查询这个更新语句的version:select * from table;
然后update set num=2,version=version+1 where version=1 and id=5;
适合并发写入少,大部分是读取场景


公平锁和非公平锁
为啥要有非公平锁:
提高效率
避免唤醒带来的空档期


共享锁和排他锁
排他锁:独占独享
共享:读锁，可以查看查询

要么多读,要么一个写。

交互方式:
选择规则
读线程插队
升降级

ReentrantReadWriteLock
读锁插队策略:
公平锁:不允许插队

非公平:
假设线程2和4正在同时读取，线程3想要加入写入，拿不到锁，
于是进入等待队列，线程5不在队列里面，现在想要过来读取

此时有两种策略:
1-5插队，读可以插队，效率高。容易造成饥饿。
2-避免饥饿 ，将5塞入队列 ReentrantReadWriteLock选择了这种
读锁只是在队列中第一个节点是写锁的情况下不能插队
写锁可以随时插队


支持锁降级,不支持升级。
从写锁降级到读锁,可能后期只需要读取操作,不需要写



自旋锁和阻塞锁

```


### CAS原理
```text
并发
我认为V的值是A,如果是的话就把它改成B
如果不是A(说明被别人修改过了),那我就不修改了

CAS有三个操作数:内存值V,预期值A,要修改的值B,
并且只有当预期值A和内存值V相同时,才将内存值修改为B.
否则什么都不做。


AtomicInteger加载Unsafe工具,用来直接操作内存数据
用Unsafe来实现底层操作
用volatile修改value字段


1-通过unsafe加载AtomicInteger中value字段的地址  valueOffset
    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

 private volatile int value;


public final int getAndAdd(int delta) {
        return unsafe.getAndAddInt(this, valueOffset, delta);
    }


  public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
        //先执行循环体
            var5 = this.getIntVolatile(var1, var2);
            //如果为false就跳出循环
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
    }

Undafe是CAS的核心类
Java无法直接访问底层的操作系统
而是通过本地方法来访问。
不过尽管如此,JVM提供了Unsafe,提供了硬件级别的原子操作。

valueOffset表示的是变量值在内存中的偏移地址。
Unsafe就是根据内存偏移地址获取数据的原值的,这样我们就能通过unsafe来实现CAS了。


存在问题:
ABA
可以使用版本号代替具体的值
自旋时间长

```

### 栈封闭
````text
变量写在线程内部-栈封闭
在方法里面新建的局部变量，实际上是存储在
每个线程私有的栈空间里，而每个栈空间是不能被其他线程所访问到的，
所以不会有线程安全问题。


````

### 集合
```text
hashmap  arrarlist

变为线程安全的list
Collections.synchronizedList(new ArrayList())
内部同步代码块,和vector和hashtable性能差不多。

ConcurrentHashMap和CopyOnWriteArrayList 取代上面的性能不好的集合方法、




CopyOnWriteArrayList：
适合读多写少



HashMap每次put操作是都会检查一遍 size（当前容量）>initailCapacity*loadFactor 是否成立。
如果不成立则HashMap扩容为以前的两倍（数组扩成两倍），
然后重新计算每个元素在数组中的位置，然后再进行存储。


ConcurrentHashMap
1.7
最外层为多个segment,每个segment的底层数据结构和hashMap类似
每个segment独立上了ReentrantLock锁,每个segment之间互不影响，提高并发
默认16个segment。


1.8
不采用segment,使用CAS和sychronized

并发度提高
数据结构不同
拉链+红黑树
默认链表长度达到8个（哈希冲突达到8）转为红黑树,概率很小(除非hash算法有问题,哈希碰撞严重)



CopyOnWriteArrayList:并发容器
代替Vector和SynchronizedList

适用:多操作尽可能快,而写即使慢一些也没关系
比如:黑名单,白名单。
读多写少.

读写规则:
回顾读写锁:读读共享,其他互斥

CopyOnWriteArrayList：
读取完全不用加锁,写入也不会阻塞读取操作,只有写入和写入之间需要进行同步等待。

CopyOnWrite的含义:
会对原有数据进行复制,修改新的内容，最后再替换
创建新副本,读写分离

缺点:
数据一致性问题,只能保证最终一致性.不能保证实时的一致性。
内存占用问题:使用了复制机制,进行写操作的时候内存会驻扎两个对象。

 /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }


```

### 并发队列
````text
put take
add romove element
offer poll peek


ArrayBlockingQueue在生产者放入数据和消费者获取数据，都是共用同一个锁对象，由此也意味着两者无法真正并行运行，
而LinkedBlockingQueue之所以能够高效的处理并发数据，
还因为其对于生产者端和消费者端分别采用了独立的锁来控制数据同步，
这也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据。

````


### 线程合作
```text
Condition作用
当线程1需要等待某个条件,就执行condition.await(),线程进入阻塞状态

假设线程2,执行对应条件,达成
线程2执行condition.signal,这时候JVM就会从被阻塞的线程里找,找到那些等待该condition的线程

```



### AQS
```text

实现内部类:
Sync extends AbstractQueuedSynchronizer

重点类:
AbstractQueuedSynchronizer


内部原理:
state
控制线程抢锁和配合的FIFO队列
期望协作工具类去实现的获取/释放等重要方法

 protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
  }
  
  
队列用来存放等待的线程  
  
获取操作依赖state变量,经常会阻塞,比如获取不到锁  
释放操作不会阻塞
  
  
AQS用法:
第一步:类,想好协作逻辑,实现获取/释放方法
第二部:内部写一个Sync类继承AbstractQueuedSynchronizer
第三部:根据是否独占来重写tryAcquire/tryRelease  
或者tryAcquireShard/tryReleaseShard
  
  
  
CountDownLatch:
  构造函数
  int getCount() {
      return getState();
  }
 
 
  //如果小于0就加入等待队列 doAcquireSharedInterruptibly
  public final void acquireSharedInterruptibly(int arg)
              throws InterruptedException {
          if (Thread.interrupted())
              throw new InterruptedException();
          if (tryAcquireShared(arg) < 0)
              doAcquireSharedInterruptibly(arg);
      }
 
tryAcquireShared:
 protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
 }  
  
如果倒数还没结束,就将线程放入等待队列 ，并且把线程进行阻塞
 private final boolean parkAndCheckInterrupt() {
         LockSupport.park(this);
         return Thread.interrupted();
     } 
 
  
countDown方法:
       CAS操作:
       protected boolean tryReleaseShared(int releases) {
              // Decrement count; signal when transition to zero
              for (;;) {
                  int c = getState();
                  if (c == 0)
                      return false;
                  int nextc = c-1;
                  if (compareAndSetState(c, nextc))
                      return nextc == 0;
              }
          }
 
  
  doReleaseShared(); 唤醒之前阻塞的线程
   LockSupport.unpark(s.thread);
  
```



### Future和Callable
````text
没有返回值
Runnable不能抛出checked Exception


Callable
V call() throws Exception


Future类
和Callable相互配合

get()
get(long timeout,timeUnit unit)
cancel()
isDone()
isCanceleD

````







