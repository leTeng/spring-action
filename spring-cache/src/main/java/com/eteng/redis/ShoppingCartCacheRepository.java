package com.eteng.redis;

import com.eteng.pojo.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @FileName UserCacheRepository.java
 * @Author eTeng
 * @Date 2019/4/18 16:02
 * @Description
 */
@Repository
public class ShoppingCartCacheRepository{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
        // redisTemplate.opsForValue().increment(shoppingCart.getId(),2);
        // 获取元素的长度
        redisTemplate.opsForValue().size(shoppingCart.getId());
    }

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
//        redisTemplate.opsForList().leftPushAll(shoppingCart.getId(),shoppingCart.getProducts());
        // 右进左出
        redisTemplate.opsForList().rightPush(shoppingCart.getId(),shoppingCart.getProducts().get(0));
        redisTemplate.opsForList().leftPop(shoppingCart.getId());
//        redisTemplate.opsForList().rightPushAll(shoppingCart.getId(),shoppingCart.getProducts());
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

    public void opsForCluster(){
//        redisTemplate.opsForCluster().
    }
}
