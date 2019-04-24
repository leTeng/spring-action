Spring Data
----------------------------------
##### Spring Data Redis
> Spring Data 为 Redis的集成提供的支持。分别容器化的连接的管理、操作模板类、
  声明式缓存、自动化Repository等。
 
  1.容器化的连接的管理
-------------------------------------------------------------------------------
   ###### Spring Data Redis 提供四个连接工厂
   - `JedisConnectionFactory:` 一个社区驱动的连接器，由Spring Data Redis模块支持
   - `LettuceConnectionFactory:` 一个基于Netty的开源连接器，由Spring Data Redis支持
   - `JredisConnectionFactory:`
   - `SrpConnectionFactory:`
   
   ###### 连接工厂配置
   ```java
        public class RedisConfig{
            /**
             * 创建JedisConnectionFactory
             * @return
             */
            @Bean
            public RedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration){
                return new JedisConnectionFactory(redisStandaloneConfiguration);
            }
        }
   ```
   ```java
        public class RedisConfig{
            /**
            * 创建LettuceConnectionFactory
            * @param redisStandaloneConfiguration
            * @return
            */
            @Bean
            public RedisConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration){
              return new LettuceConnectionFactory(redisStandaloneConfiguration);
            }
        }
   ```
   ```java
        public class RedisConfig{
            /**
             * redis 的连接配置
             * @param host 主机
             * @param port 端口
             * @param dbIndex 槽
             * @param passwrod 密码
             */
            @Bean
            public RedisStandaloneConfiguration redisStandaloneConfiguration(@Value("redis.host") String host,
                                                                             @Value("redis.port") Integer port,
                                                                             @Value("redis.db.index") Integer dbIndex,
                                                                             @Value("redis.passwrod") String passwrod){
                RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
                config.setDatabase(dbIndex);
                config.setPort(port);
                config.setHostName(host);
                config.setPassword(RedisPassword.of(passwrod));
                return config;
            }
        }
   ```
   2.Redis 的高级应用配置
   ------------------------------------------------------------
   
   ###### 2.1 Redis 分片和主从复制
   > Redis 支持多个节点进行分片存储，通过工具来实现节点管理。Redis一共具有16384个槽，
     多个节点来分配这些槽。数据存储是通过`CRC16(key) % 16384`来计算所属节点槽。Redis
     还提供主从节点来实现高可用。即便主节点宕机，从节点依旧可以代替主节点来保证集群的运行。
     一个主节点可以具有N从节点。
   
   - 集群的搭建
     - 配置多个Redis配置文件
       ```text
         # 注释绑定的IP,允许所有的IP连接
         # bind 127.0.0.1
         # 关闭保护模式,允许所有的IP连接
         protected-mode 
         # 配置Redis监听的端口(默认是6379)
         port 6376
         # 设置超时时间
         timeout 15000
         # 允许后台运行
         daemonize yes
         # 指定日志输出
         logfile "/usr/local/soft/redis_cluster/6376/6376.log"
         # 开启集群
         cluster-enabled yes
         # 集群配置文件
         cluster-config-file nodes-6376.conf
         # 节点通讯超时时间
         cluster-node-timeout 15000
         .......
       ```
       每个节点都有一份独立的配置文件，并且将每个节点依据对应的配置启动。
     
     - 创建集群
       ###### 安装ruby环境
         ```text
            yum install ruby rubygems -y
         ```
       ###### 安装集群管理工具(gem redis) 
         ```text
            gem install redis
         ``` 
       ###### 拷贝Redis源码包的redis-trib.rb到redis的安装目录(注意拷贝后去掉后缀)
         ```text
           cp redis源码目录/redis-trib.rb redis安装目录/redis-trib
         ```
       ###### 创建集群
         ```text
            redis-trib create --replicas 1 [node1] [node2] [node3] ...
         ``` 
         - --replicas 1 --replicas表示为每个节点创建从节点，1表示创建一个从节点
         
   - 节点宕机
     > 当主节点宕机后,从节点会升为主节点，集群会正常的运行。如果从节点宕机后，集群会正常
       的运行。当主节点和全部从节点宕机，集群不能正常运行，需要手动修复。
     ```text
        # 正常运行的节点（主 -> 从 6370->6374,6371->6375,6372->6376,6373->6377）
        root     18810 18685  0 16:03 pts/1    00:00:00 grep --color=auto redis
        root     21568     1  0 10:30 ?        00:00:20 ./redis-server *:6371 [cluster]
        root     21570     1  0 10:30 ?        00:00:18 ./redis-server *:6372 [cluster]
        root     21572     1  0 10:30 ?        00:00:18 ./redis-server *:6373 [cluster]
        root     21576     1  0 10:30 ?        00:00:15 ./redis-server *:6375 [cluster]
        root     21578     1  0 10:30 ?        00:00:15 ./redis-server *:6376 [cluster]
        root     21584     1  0 10:30 ?        00:00:15 ./redis-server *:6377 [cluster]
        root     27465     1  0 11:32 ?        00:00:11 redis-server *:6374 [cluster]
        root     27485     1  0 11:33 ?        00:00:14 redis-server *:6370 [cluster] 
        
        # 检查节点
        [root@VM_0_10_centos redis-3.2.6]# ./redis-trib check 127.0.0.1:6370
        >>> Performing Cluster Check (using node 127.0.0.1:6370)
        M: e2c9459f4eb2e0cc774932cda4a501c45e9df1f5 127.0.0.1:6370
           slots:0-4095 (4096 slots) master
           1 additional replica(s)
        S: 161d4344ad7ec615594d6fd179d2607ee652bac1 127.0.0.1:6374
           slots: (0 slots) slave
           replicates 
        .......
        [OK] All nodes agree about slots configuration.
        >>> Check for open slots...
        >>> Check slots coverage...
        [OK] All 16384 slots covered.
        
        # 手动宕机6370节点
        kill -9 27485
   
        # 再检查节点，从节点(6374)以代替主节点(6370)
        M: 161d4344ad7ec615594d6fd179d2607ee652bac1 127.0.0.1:6374
           slots:0-4095 (4096 slots) master
           0 additional replica(s)
        .......
        [OK] All nodes agree about slots configuration.
        >>> Check for open slots...
        >>> Check slots coverage...
        [OK] All 16384 slots covered.
   
        # 手动恢复6370节点
        redis-server 6370_redis.conf
        
        # 再检查节点，6370为6374从节点
        M: 161d4344ad7ec615594d6fd179d2607ee652bac1 127.0.0.1:6374
           slots:0-4095 (4096 slots) master
           1 additional replica(s)
        S: e2c9459f4eb2e0cc774932cda4a501c45e9df1f5 127.0.0.1:6370
           slots: (0 slots) slave
           replicates 161d4344ad7ec615594d6fd179d2607ee652bac1 
     ```  
    
   - 添加节点(主从)
     > 为实现高扩展允许向Redis集群添加节点(主从节点)，添加后的节点是需要手动分配槽
       给节点。
     ```text
        # 添加节点6373到127.0.0.1:6370集群
          ./redis-trib add-node 127.0.0.1:6373 127.0.0.1:6370
          
        # 添加后(没有分配槽)
          M: eee70e03e97d3e0144bb800c20a3c501640d7146 127.0.0.1:6373
             slots: (0 slots) master
             0 additional replica(s)
        
        # 手动分配槽(从6371移动4096个槽到6373)
          # 重写分配槽
          ./redis-trib reshard 127.0.0.1:6370
          # 移动槽的个数(4096)
          How many slots do you want to move (from 1 to 16384)? 4096
          # 移动到目标的节点的id(6373)
          What is the receiving node ID? eee70e03e97d3e0144bb800c20a3c501640d7146
          Please enter all the source node IDs.
            Type 'all' to use all the nodes as source nodes for the hash slots.
            Type 'done' once you entered all the source nodes IDs.
          # 移动曹的源节点的id(6373)
          Source node #1:481d8f2b8ba27eac9eadd338377b6c3cca3ea07e
          Source node #2:done
          
        # 分配槽后
          M: eee70e03e97d3e0144bb800c20a3c501640d7146 127.0.0.1:6373
             slots:4096-8191 (4096 slots) master
             0 additional replica(s)
        # 添加从节点(6377)
          ./redis-trib add-node  --slave --master-id eee70e03e97d3e0144bb800c20a3c501640d7146 127.0.0.1:6377 127.0.0.1:6370
        
        # 从节点添加后
          M: eee70e03e97d3e0144bb800c20a3c501640d7146 127.0.0.1:6373
             slots:4096-8191 (4096 slots) master
             1 additional replica(s)
          S: 45ed33288f028027d0f216dba8b40fc6b179d27f 127.0.0.1:6377
             slots: (0 slots) slave
             replicates eee70e03e97d3e0144bb800c20a3c501640d7146  
     ```  
   - 删除节点(主从) 
     > 为实现高扩展允许向Redis集群添加节点(主从节点)，删除节点前是需要手动分配删除
       节点槽给其他的节点。
     ```text
     
         # 删除6373节点前需要移动拥有槽到别的节点(如6371)
           redis-trib reshard 127.0.0.1:6370
         
         # 设置移动的槽的个数(4096个)
           How many slots do you want to move (from 1 to 16384)? 4096
           
         # 设置移动到的目标节点(6371)
           What is the receiving node ID? 481d8f2b8ba27eac9eadd338377b6c3cca3ea07e
             
         # 设置移动的源节点(6373)
           Source node #1:24477c2a431714c3ca57ff9a49bceef71cf05ee6
           Source node #2:done
         
         # 移动槽
           Moving slot 16381 from 24477c2a431714c3ca57ff9a49bceef71cf05ee6
           Moving slot 16382 from 24477c2a431714c3ca57ff9a49bceef71cf05ee6
           Moving slot 16383 from 24477c2a431714c3ca57ff9a49bceef71cf05ee6
           Do you want to proceed with the proposed reshard plan (yes/no)? yes
        
         #移动后
           M: 24477c2a431714c3ca57ff9a49bceef71cf05ee6 193.112.51.188:6373
              slots: (0 slots) master
              0 additional replica(s)
           M: 481d8f2b8ba27eac9eadd338377b6c3cca3ea07e 127.0.0.1:6371
              slots:4096-8191,12288-16383 (8192 slots) master
              2 additional replica(s)   
         
         # 删除节点
           [root@VM_0_10_centos redis-3.2.6]# ./redis-trib del-node 127.0.0.1:6373 24477c2a431714c3ca57ff9a49bceef71cf05ee6
           >>> Removing node 24477c2a431714c3ca57ff9a49bceef71cf05ee6 from cluster 127.0.0.1:6373
           >>> Sending CLUSTER FORGET messages to the cluster...
           >>> SHUTDOWN the node.

         # 删除从节点
           [root@VM_0_10_centos redis-3.2.6]# ./redis-trib del-node 127.0.0.1:6377 a9517b42c5d7cf7d5ff5af1d3aa0d4fbe452a112
           >>> Removing node a9517b42c5d7cf7d5ff5af1d3aa0d4fbe452a112 from cluster 127.0.0.1:6377
           >>> Sending CLUSTER FORGET messages to the cluster...
           >>> SHUTDOWN the node
     ```  
     
   - 踩坑
     - [Node 127.0.0.1:6373 is not empty. Either the node already knows other nodes](http://blog.jobbole.com/113760/)
     - [call': ERR Slot 16011 is already busy (Redis::CommandError)](https://blog.csdn.net/tengdazhang770960436/article/details/49962979)
     - [一直卡在Waiting for the cluster to join ......](https://blog.csdn.net/XIANZHIXIANZHIXIAN/article/details/82392172)
     - [redis requires Ruby version >= 2.2.2](https://blog.csdn.net/XIANZHIXIANZHIXIAN/article/details/82391853)
     - [redis集群密码设置](https://blog.csdn.net/jtbrian/article/details/53691540)
     
   ##### 2.2 Redis 数据持久化
   
   ##### 2.3 Redis 事务
  
   3.操作模板类
   ------------------------------------------------------------
   - RedisTemplate 
   > 可以持久化各种类型的数据,不局限于字节数组。并可以提供自定义的序列化实现
   
   - StringRedisTemplate
   > 是RedisTemplate的扩展，只针对字符串类型数据的持久化
   
   - 配置
   ```java
     class RedisConfig{
        /**
         * 创建RedisTemplate
         * @param jedisConnectionFactory
         * @param jedisConnectionFactory
         * @return
         */
        @Bean
        public RedisTemplate redisTemplate(RedisConnectionFactory jedisConnectionFactory){
            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(jedisConnectionFactory);
            return redisTemplate;
        }  
     }
    
   ```
   
   3.1 模板类常用API
   -----------------------------------------------------------
   |            方法           |              子API接口            |                 功能              |
   |:------------------------:|:--------------------------------:|:--------------------------------:|
   |   opsForVaule()          |      ValueOperations<K,V>        |            操作字符串结构           |
   |   opsForList             |      ListOperations<K,V>         |            操作List结构            |
   |   opsForSet              |      SetOperations<K,V>          |            操作Set结构             |
   |   opsForZSet             |      ZSetOperations<K,V>         |            操作ZSet结构            |
   |   opsForHash             |      HashOperations<K,V>         |            操作Hash结构            |
   |   boundValueOps(K        |      BoundeValueOperations<K,V>  |            绑定Key操作字符串结构     |
   |   boundListOps(K)        |      BoundeListOperations<K,V>   |            绑定Key操作List结构      |
   |   boundSetOps(K)         |      BoundeSetOperations<K,V>    |            绑定Key操作Set结构       |
   |   boundZSetValueOps(K    |      BoundeZSetOperations<K,V>   |            绑定Key操作ZSet结构      |
   |   boundHashOps(K)        |      BoundeHashOperations<K,V>   |            绑定Key操作Hash结构      |
   
   3.2 代码示例
   ----------------------------------------------------------
   一. 操作字符串结构
   ```java
        @Repository
        public class ShoppingCartCacheRepository{
            /**
            * 存储String结构
            * 除了具备基本的功能外，该结构提供更多的对元素操作都是字符串或者数值类型。包括字符串操作
            * 自增等功能。
            * @param shoppingCart
            */
            public void opsForValue(ShoppingCart shoppingCart){
                // 添加或覆盖
                redisTemplate.opsForValue().set(shoppingCart.getId(),shoppingCart.getId());
                
                // 添加或者覆盖多个元素
                redisTemplate.opsForValue().multiSet(new HashMap());
                
                // 获取
                redisTemplate.opsForValue().get(shoppingCart.getId());
                
                // 获取多个元素
                redisTemplate.opsForValue().multiGet(new ArrayList());
                
                // 添加或覆盖并设置过期时间
                redisTemplate.opsForValue().set(shoppingCart.getId(),shoppingCart.getId(),10000,TimeUnit.MICROSECONDS);
                
                // 在原本的字符串后面追加字符串
                redisTemplate.opsForValue().append(shoppingCart.getId(),shoppingCart.getId());
                
                // 替换子串(在第10位开始替换)
                redisTemplate.opsForValue().set(shoppingCart.getId(),"244",10);
                
                // 获取子串(在第10位开始,获取5为)
                redisTemplate.opsForValue().get(shoppingCart.getId(),10,5);
                
                // 指定的Key的Value自增2
                redisTemplate.opsForValue().increment(shoppingCart.getId(),2);
                
                // 获取元素的长度
                redisTemplate.opsForValue().size(shoppingCart.getId());
            }
        }
   ```
   二. 操作List结构
   ```java
        @Repository
        public class ShoppingCartCacheRepository{
            /**
             * 存储List结构
             * 除了具备基本的功能外，该结构提供更多的对元素操作栈和队列的操作。包括进栈出栈、入队出队。
             * 还有可以对的基本操作。
             * @param shoppingCart
             */
            public void opsForList(ShoppingCart shoppingCart){
                // 队列操作
                // 左进右出
                redisTemplate.opsForList().leftPush(shoppingCart.getId(),shoppingCart.getProducts().get(0));
                redisTemplate.opsForList().rightPop(shoppingCart.getId());
                redisTemplate.opsForList().leftPushAll(shoppingCart.getId(),shoppingCart.getProducts());
                
                // 右进左出
                redisTemplate.opsForList().rightPush(shoppingCart.getId(),shoppingCart.getProducts().get(0));
                redisTemplate.opsForList().leftPop(shoppingCart.getId());
                redisTemplate.opsForList().rightPushAll(shoppingCart.getId(),shoppingCart.getProducts());
                
                // 栈操作
                // 左进左出
                redisTemplate.opsForList().leftPush(shoppingCart.getId(),shoppingCart.getProducts().get(0));
                redisTemplate.opsForList().leftPop(shoppingCart.getId());
                
                // 右进右出
                redisTemplate.opsForList().rightPush(shoppingCart.getId(),shoppingCart.getProducts().get(0));
                redisTemplate.opsForList().rightPop(shoppingCart.getId());
                
                // 删除
                redisTemplate.delete(shoppingCart.getId());
                
                // 根据索引查找(索引为1)
                redisTemplate.opsForList().index(shoppingCart.getId(),1);
                
                // 获取列表子视图(0到4)
                redisTemplate.opsForList().range(shoppingCart.getId(),0,4);
                
                // 替换指定位置的元素(替换位置为1的元素为a)
                redisTemplate.opsForList().set(shoppingCart.getId(),1,"a");
                
                // 获取列表的长度
                redisTemplate.opsForList().size(shoppingCart.getId());
                
                // 去除两边的元素
                redisTemplate.opsForList().trim(shoppingCart.getId(),4,5);
            }
        }
   ```
   三. 操作Set结构
   ```java
       @Repository
       public class ShoppingCartCacheRepository{
           /**
            * 存储Set结构
            * 除了具备基本的功能外，该结构提供更多的对多个集合进行操作。主要包括元素的寻找具有很大优势
            * @param shoppingCart
            */
           public void opsForSet(ShoppingCart shoppingCart){
               // 添加元素
               redisTemplate.opsForSet().add(shoppingCart.getId(),"1");
               
               // 添加多个元素
               redisTemplate.opsForSet().add(shoppingCart.getId(),"2","3");
               
               // 获取指定集合(shoppingCart.getId() + "1")的差集
               redisTemplate.opsForSet().difference(shoppingCart.getId(),shoppingCart.getId() + "1");
               
               // 获取指定集合(shoppingCart.getId() + "1")的差集并以shoppingCart.getId() + "2"为key重新存储
               redisTemplate.opsForSet().differenceAndStore(shoppingCart.getId(),shoppingCart.getId() + "1",shoppingCart.getId() + "2");
               
               // 获取指定集合(shoppingCart.getId() + "1")的交集
               redisTemplate.opsForSet().intersect(shoppingCart.getId(),shoppingCart.getId() + "1");
              
               // 获取指定集合(shoppingCart.getId() + "1")的交集并以shoppingCart.getId() + "2"为key重新存储
               redisTemplate.opsForSet().intersectAndStore(shoppingCart.getId(),shoppingCart.getId() + "1",shoppingCart.getId() + "2");
              
               // 获取指定集合(shoppingCart.getId() + "1")的并集
               redisTemplate.opsForSet().union(shoppingCart.getId(),shoppingCart.getId()+"1");
               
               // 获取指定集合(shoppingCart.getId() + "1")的并集并以shoppingCart.getId() + "1"为key重新存储
               redisTemplate.opsForSet().unionAndStore(shoppingCart.getId(),shoppingCart.getId()+"1",shoppingCart.getId() + "2");
               
               // 获取集合的所有元素
               redisTemplate.opsForSet().members(shoppingCart.getId());
              
               // 元素是否在集合中(元素1)
               redisTemplate.opsForSet().isMember(shoppingCart.getId(),"1");
               
               // 随机弹出一个元素
               redisTemplate.opsForSet().pop(shoppingCart.getId());
               
               // 随机获取一个元素
               redisTemplate.opsForSet().randomMember(shoppingCart.getId());
              
               // 随机获取多个元素(获取4个)
               redisTemplate.opsForSet().randomMembers(shoppingCart.getId(),4);
              
               // 删除元素
               redisTemplate.opsForSet().remove(shoppingCart.getId(),"1");
               
               // 将元素从集合1移动到集合2
               redisTemplate.opsForSet().move(shoppingCart.getId(),"1",shoppingCart.getId() + "1");
               
               // 获取集合的大小
               redisTemplate.opsForSet().size(shoppingCart.getId());
               
               // 从指定的位置开始迭代符合规则的元素
               redisTemplate.opsForSet().scan(shoppingCart.getId(),ScanOptions.scanOptions().match("*").count(3).build());
           }
       }
   ```
   四. 操作ZSet结构
   ```java
        @Repository
        public class ShoppingCartCacheRepository{   
            /**
             * 存储SortSet结构
             */
            public void opsForZSet(ShoppingCart shoppingCart){
                // 添加元素并指定元素的权重
                redisTemplate.opsForZSet().add(shoppingCart.getId(),"1",100);
                
                // 添加多个元素
                redisTemplate.opsForZSet().add(shoppingCart.getId(),new HashSet<ZSetOperations.TypedTuple>());
                
                // 统计指定权重区间内的元素个数
                redisTemplate.opsForZSet().count(shoppingCart.getId(),0,100);
                
                // 给指定的元素进行加权重
                redisTemplate.opsForZSet().incrementScore(shoppingCart.getId(),"1",10);
                
                // 获取指定元素的索引
                redisTemplate.opsForZSet().rank(shoppingCart.getId(),"1");
                
                // 获取集合的所有元素
                redisTemplate.opsForZSet().zCard(shoppingCart.getId());
                
                // 获取集合的大小
                redisTemplate.opsForZSet().size(shoppingCart.getId());
                
                // 获取指定元素的权重
                redisTemplate.opsForZSet().score(shoppingCart.getId(),"1");
                
                // 迭代集合符合规则的元素
                redisTemplate.opsForZSet().scan(shoppingCart.getId(),ScanOptions.scanOptions().count(2).match("*").build());
            
                // 获取指定集合(shoppingCart.getId() + "1")的交集并以shoppingCart.getId() + "2"为key重新存储
                redisTemplate.opsForZSet().intersectAndStore(shoppingCart.getId(),shoppingCart.getId()+"1",shoppingCart.getId() + "2");
                
                // 获取指定集合(shoppingCart.getId() + "1")的并集并以shoppingCart.getId() + "2"为key重新存储
                redisTemplate.opsForZSet().unionAndStore(shoppingCart.getId(),shoppingCart.getId()+"1",shoppingCart.getId() + "2");
            
                // 删除指定的多个元素
                redisTemplate.opsForZSet().remove(shoppingCart.getId(),"1","2");
                
                // 删除有序集合中指定区间的元素(区间为[0,1])
                redisTemplate.opsForZSet().removeRange(shoppingCart.getId(),0,1);
                
                // 删除有序集合中指定权重区间的元素(区间为[50,100])
                redisTemplate.opsForZSet().removeRangeByScore(shoppingCart.getId(),50,100);
            
                // 从有序集合中获取指定区间的元素(区间为[0,1])
                redisTemplate.opsForZSet().range(shoppingCart.getId(),0,1);
                
                // 从有序集合中获取指定权重区间的元素(区间为[50,100])
                redisTemplate.opsForZSet().rangeByLex(shoppingCart.getId(),RedisZSetCommands.Range.range().gte(50).lte(100));
                
                // 从有序集合中获取指定权重区间的元素(区间为[50,100]),并在结果集分页(限制为：limit 0,2)
                redisTemplate.opsForZSet().rangeByLex(shoppingCart.getId(),RedisZSetCommands.Range.range().gte(50).lte(100),RedisZSetCommands.Limit.limit().offset(0).count(2));
                
                // 从有序集合中获取指定权重区间的元素(区间为[50,100])
                redisTemplate.opsForZSet().rangeByScore(shoppingCart.getId(),50,100);
                
                // 从有序集合中获取指定权重区间的元素的权重(区间为[50,100])
                redisTemplate.opsForZSet().rangeByScoreWithScores(shoppingCart.getId(),50,100);
            
                // 倒序方式获取指定元素的索引
                redisTemplate.opsForZSet().reverseRank(shoppingCart.getId(),"1");
            
                // 倒序方式从有序集合中获取指定区间的元素(区间为[0,1])
                redisTemplate.opsForZSet().reverseRange(shoppingCart.getId(),0,1);
                
                // 倒序方式从有序集合中获取指定区间的元素(区间为[50,100])
                redisTemplate.opsForZSet().reverseRangeByScore(shoppingCart.getId(),50,100);
               
                // 倒序方式从有序集合中获取指定权重区间的元素(区间为[50,100]),并在结果集分页(限制为：limit 0,2)
                redisTemplate.opsForZSet().reverseRangeByScore(shoppingCart.getId(),50,100,0,2);
            
                // 倒序方式从有序集合中获取指定区间的元素权重(区间为[0,1])
                redisTemplate.opsForZSet().reverseRangeWithScores(shoppingCart.getId(),0,1);
                
                // 倒序方式从有序集合中获取指定权重区间的元素权重(区间为[50,100])
                redisTemplate.opsForZSet().reverseRangeByScoreWithScores(shoppingCart.getId(),50,100);
                
                // 倒序方式从有序集合中获取指定权重区间的元素权重(区间为[50,100]),并在结果集分页(限制为：limit 0,2)
                redisTemplate.opsForZSet().reverseRangeByScoreWithScores(shoppingCart.getId(),50,100,0,2);
            
            }
        }
   ```
   五. 操作Hash结构
   ```java
        @Repository
        public class ShoppingCartCacheRepository{
            /**
             * 存储Hash结构
             * @param shoppingCart
             */
            public void opsForHash(ShoppingCart shoppingCart){
                // 添加元素
                redisTemplate.opsForHash().put(shoppingCart.getId(),shoppingCart.getId(),"1");
                
                // 添加多个元素
                redisTemplate.opsForHash().putAll(shoppingCart.getId(),new HashMap());
                
                // 获取指定元素
                redisTemplate.opsForHash().get(shoppingCart.getId(),shoppingCart.getId());
                
                // 获取多个元素
                redisTemplate.opsForHash().multiGet(shoppingCart.getId(),new HashSet());
               
                // key 是否存在
                redisTemplate.opsForHash().hasKey(shoppingCart.getId(),shoppingCart.getId());
                
                // 获取全部的key
                redisTemplate.opsForHash().keys(shoppingCart.getId());
               
                // 元素自增
                redisTemplate.opsForHash().increment(shoppingCart.getId(),shoppingCart.getId(),2);
                
                // 获取所有的元素值
                redisTemplate.opsForHash().values(shoppingCart.getId());
                
                // 删除指定的元素
                redisTemplate.opsForHash().delete(shoppingCart.getId());
                
                // 迭代集合
                redisTemplate.opsForHash().scan(shoppingCart.getId(),ScanOptions.scanOptions().count(10).match("*").build());
            }
        }
   ```
   3.3 数据序列化
   ---------------------------------------------------------------
   ###### Spring Data Redis 持久化数据时提供四种序列化方案
   
   - `GenericToStringSerializer:`       Spring转换服务序列化
   - `JdkSerializationRedisSerializer:` Java序列化
   - `Jackson2JsonRedisSerializer:`     jackson2序列化
   - `StringRedisSerializer:`           String类型序列化
   - `OxmSerializer:`                   xml序列化
   
   ###### 配置序列化方案
   ```java
        class RedisConfig{
            /**
             * 创建RedisTemplate
             * @param jedisConnectionFactory
             * @param redisSerializer
             * @return
             */
            @Bean
            public RedisTemplate redisTemplate(RedisConnectionFactory jedisConnectionFactory,
                                               RedisSerializer redisSerializer){
                RedisTemplate redisTemplate = new RedisTemplate();
                redisTemplate.setConnectionFactory(jedisConnectionFactory);
                 // 设置序列化实现
                redisTemplate.setValueSerializer(redisSerializer);
                redisTemplate.setKeySerializer(redisSerializer);
                return redisTemplate;
            }  
            
            /**
             * 创建系列化的实现
             * @return
             */
            @Bean
            public RedisSerializer redisSerializer(){
                return new JdkSerializationRedisSerializer();
            }
        }
   ```
   
   4.声明式缓存
   ---------------------------------------------------------------
   
   4.1 描述
   ---------------------------------------------------------------
   >  Spring为缓存组件提供的集成，集成多种缓存实现(如Ehcache、Redis)。并提供基于应用程序内
      存的的实现(如ConCurrentMap)。Spring为缓存组件集成抽象出一个CacheManager。Spring
      或者Spring Data依赖于缓存实现来实现CacheManager。根据用户所配置的CacheManager，
      直接使用声明式进行缓存操作。缓存管理器是Spring对众多流行缓存组件的一个抽象，这个抽象就
      是键值对存取。因为每一种缓存组件都提供键值对存取这简单的功能,所有缓存管理器只能操作键值对。
      
   4.2 比较
   ---------------------------------------------------------------
   >  Spring Data Redis 2.0 版本的配置相比1.0版本Spring提供一个RedisCacheWriter来进
      行Redis的访问,并非RedisTemplateRedisCacheWriter需要RedisConnectionFactory来
      支撑。CacheManager只是定义了几个简单的键值对存储或者删除，所以声明式缓存之不能使用Redis
      更高级的数据结构。
      
   4.3 配置缓存管理器
   ---------------------------------------------------------------
   - Ehcache缓存管理器
     ```java
        public class EhcacheConfig{
            /**
             * 配置Spring提供的Ehcache缓存管理器(依赖于Ehcache的CacheManager)
             * @param cm
             * @return
             */
            @Bean
            public org.springframework.cache.CacheManager ehCacheCacheManager(CacheManager cm){
                return new EhCacheCacheManager(cm);
            }
        
            /**
             * 配置Spring提供的Ehcache缓存管理器工厂
             * @return
             */
            @Bean
            public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
                EhCacheManagerFactoryBean cacheManagerFactory = new EhCacheManagerFactoryBean();
                // 配置ehcache的配置文件
                cacheManagerFactory.setConfigLocation(new ClassPathResource("classpath:/ehcache.xml"));
                cacheManagerFactory.afterPropertiesSet();
                return cacheManagerFactory;
            }
        }
     ```
   - Redis缓存管理器(**由Spring Data Redis提供**)
       ```java
            public class RedisCacheConfig{
            
                /**
                 * Spring Data Redis 1.0 版本的配置
                 * @param redisTemplate
                 * @return
                 */
                @Bean
                public CacheManager redisCacheManagerByOne(RedisTemplate redisTemplate){
                    return new RedisCacheManager(redisTemplate);
                }
            
                /**
                 * Spring Data Redis 2.0 版本的配置
                 * @return
                 */
                @Bean
                public CacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory){
                    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
                    // 设置前缀
                    configuration.prefixKeysWith("shopping_cart");
                    // 设置过期时间
                    configuration.entryTtl(Duration.ofHours(1));
                    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                            // 缓存配置(使用默认配置)
                            .cacheDefaults(configuration)
                            // 支持开启事务
                            .transactionAware()
                            .build();
                }
            }
       ```
   - 其他缓存管理器
     
     - ConcurrentMapCacheManager
       ```java
            public class LocalCacheConfig{
            
                /**
                 * 配置Spring提供本地缓存管理器
                 * @return
                 */
                @Bean
                public CacheManager concurrentMapCacheManager(){
                    return  new ConcurrentMapCacheManager();
                }
            }
       ```
     - CompositeCacheManager
        ```java
            public class CompositeCacheConfig{
                @Bean
                public CacheManager compositeCacheManager(){
                    CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
                    ArrayList managers = new ArrayList();
                    managers.add(new NoOpCacheManager());
                    managers.add(new SimpleCacheManager());
                    compositeCacheManager.setCacheManagers(managers);
                    compositeCacheManager.setFallbackToNoOpCache(true);
                    return compositeCacheManager;
                }
            }
        ```
     - SimpleCacheManager
     - NoOpCacheManager
   
   4.4 注解声明缓存
   ---------------------------------------------------------------
   > Spring会为缓存抽象创建一个切面,通过通知的时机实现缓存。切点是那些标注了注解的方法或者类上。使用在类上的注解将
     所有的方法都作为切点，使用在单个方法该方法就是一个切点。
     
   ###### 注解列表
   
   |       注解         |         描述                     |               
   |:---------------------------:|:------------------------------:|
   |         Cacheable           |   调用方法之前查询缓存是否存在，如果找到返回值缓存的值。如果找不到就执行方法并将结果缓存                             |  
   |         CachePut            |   将返回结果缓存,但不会查询缓存的值返回。(只存不取)                             |  
   |         CacheEvict          |   删除一个或者多个缓存数据                             |  
   |         Caching             |   分组注解，能应用多个其他的缓存注解                             |  
   
   ###### 注解属性
   
   - `Cacheable`、`CachePut`属性
    
     |      属性名        |       类型       |                  描述                  |
     |:------------------|:----------------|:--------------------------------------|
     |     value         |      String[]   |   要使用的缓存名称(缓存管理器)             |
     |     condition     |      String     |   SpEL表达式,结果是false，将不应用缓存切面  |
     |     key           |      String     |   SpEL表达式,缓存的key                   |
     |     unless        |      String     |   SpEL表达式,结果是true，返回值将不缓存     |
     
   - `CacheEvict`属性
   
     |      属性名        |       类型       |                  描述                        |
     |:------------------|:----------------|:---------------------------------------------|  
     |     value         |   String[]      |   要使用的缓存名称(缓存管理器)                   |
     |     condition     |   String        |   SpEL表达式,结果是false，将不应用缓存切面        |
     |     key           |   String        |   SpEL表达式,缓存的key                         |
     |     allEntries    |   boolean       |   如果为true的话，特定缓存的有数据被移除           |
     |  beforeInvocation |   boolean       |   如果为true,将在调用之前移除数据。如果为flae,反之。|
     
   ###### SpEL的使用
   > 在缓存注解中的很多属性是通过SpEL表达式的计算结果来得出的。除了使用基本的SpEL之外，
     还提供了几个元数据，方便用户的使用。
   
   - 元数据表
   
   |       表达式             |           描述                        |
   |:------------------------|:-------------------------------------|
   |      #root.args         |       传递给缓存方法的参数,数组形式        |
   |      #root.cache        |       该方法执行时对应的缓存，数组形式      |
   |      #root.target       |       目标对象                         |
   |      #root.targetClass  |       目标对象的类                      |
   |      #root.method       |       目标方法                         |
   |      #root.methodName   |       目标方法的名称                    |
   |      #result            |       方法返回值(不能应用在@Cacheable上)  |
   |      #Argument          |       任意方法的参数名                   |
   
   ###### Cacheable与CachePut的区别
   > Cacheable首先会从缓存查找数据是否存在，如果存在直接返回，不调用目标方法。如果不存
     在调用目标方法并且将方法返回值存入缓存。CachePut不会去查询缓存数据，直接调用目标方
     法并且将目标方法的返回值存入缓存。
     
   ###### condition属性与unless属性的区别
   > 这两个属性很相似，但有一点区别。condition属性计算结果如果是false,将禁用缓存功能，
     即不从缓存查数据，也不将数据放入缓存。unless属性计算结果如果是fale,会从缓存中查询
     数据,但不会缓存数据
     
   ###### 注解的使用
   ```java
        public class ShoppingCartAutoCacheRepository{
        
            /**
             * Cacheable的使用
             * 使用redis的缓存管理器作为缓存的实现,使用参数的值作为缓存key,并且通过条件判断是否禁用缓存。
             * @param id
             * @return
             */
            @Cacheable(value = "redisCacheManager",condition = "!(#root.args[0].empty)")
            public ShoppingCart cacheable(String id){
                ShoppingCart cart = new ShoppingCart();
                cart.setId(id);
                cart.setProducts(new ArrayList<Product>());
                cart.setTotalPrice(15);
                return cart;
            }
        
            /**
             * cacheaPut的使用
             * 使用redis的缓存管理器作为缓存的实现,自定义key,并且通过条件判断是否禁用缓存。
             * @param cart
             * @return
             */
            @CachePut(value = "redisCacheManager",key = "#result.id",condition = "#root.args[0] != null ")
            public ShoppingCart cachePut(ShoppingCart cart){
                cart.setId(UUID.randomUUID().toString());
                return cart;
            }
        
            /**
             * cacheaEvict的使用
             * 使用redis的缓存管理器作为缓存的实现,自定义key,并且通过条件判断是否禁用缓存。
             * @param cart
             * @return
             */
            @CacheEvict(value = "redisCacheManager",key = "#root.args[0].id",condition = "#root.args[0] != null ")
            public boolean cacheEvict(ShoppingCart cart){
                return true;
            }
        }
   ```
   5.Redis Repository
   -------------------------------------------------------------------
   
     