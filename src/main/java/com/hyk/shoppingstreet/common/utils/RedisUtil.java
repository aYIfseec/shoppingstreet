package com.hyk.shoppingstreet.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil {

    private final static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static JedisPool jedisPool;

    public static void setJedisPool(JedisPool jedisPool) {
        RedisUtil.jedisPool = jedisPool;
    }

    public enum TokenType {

        USER_TOKEN("shoppingstreet-user-","普通用户") {
            @Override
            public <T> List<T> getList(List<String> keys) {
                return null;
            }

            @Override
            public List<String> getAllKeys(String pattern) {
                return super.getKeysByPattern(pattern);
            }

            @Override
            public String get(String key) {
                return super.get(key, String.class);
            }
        },

        ;

        private String prefix;
        private String describe;

        TokenType(String prefix, String describe) {
            this.prefix = prefix;
            this.describe = describe;
        }

//        public <T> void setByPipeline(List keys, List values) {
//            Jedis jedis = null;
//            try {
//                jedis = jedisPool.getResource();
//                Pipeline pipelined = jedis.pipelined();
//
//                for (int i = 0; i < keys.size(); i++) {
//                    pipelined.sadd((prefix + keys.get(i)).getBytes("utf-8"),
//                        JSON.toJSONBytes(values.get(i), SerializerFeature.DisableCircularReferenceDetect));
//                }
//                if (pipelined != null) {
//                    pipelined.close();
//                }
//            } catch (UnsupportedEncodingException e) {
//                logger.error("redis util setByPipeline error", e);
//            } catch (IOException e) {
//                logger.error("redis util setByPipeline error", e);
//            } finally {
//                if (jedis != null) {
//                    jedis.close();
//                }
//            }
//        }

        public void set(String key, Object value) {
            this.set(key, value, 0);
        }

        public void set(String key, Object value, int expireTime) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.set((prefix + key).getBytes("utf-8"),
                    JSON.toJSONBytes(value, SerializerFeature.DisableCircularReferenceDetect));

                if (expireTime > 0) {
                    jedis.expire((prefix + key).getBytes("utf-8"), expireTime);
                }
            } catch (UnsupportedEncodingException e) {
//                logger.error("redis util set error", e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }

        private List<String> getKeysByPattern(String pattern) {
            Jedis jedis = jedisPool.getResource();
            List<String> res = Lists.newArrayList(jedis.keys((prefix + pattern)));
            if (jedis != null) {
                jedis.close();
            }
            return res;
        }


        private <T> T get(String key, Class<T> clazz) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                byte[] data = jedis.get((prefix + key).getBytes("utf-8"));
                if (data == null) {
                    return null;
                }
                return JSON.parseObject(data, clazz, Feature.DisableCircularReferenceDetect);
            } catch (UnsupportedEncodingException e) {
                logger.error("redis util get error", e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            return null;
        }

        public void del(String key) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.del((prefix + key).getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("redis util del error", e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }


        private <T> List<T> getList(List<String> keyList, Class<T> clazz) {
            Jedis jedis = null;
            List<T> res = Lists.newArrayList();

            if (CollectionUtils.isEmpty(keyList)) {
                return res;
            }

            try {
                jedis = jedisPool.getResource();

                String[] keys = new String[keyList.size()];
                for (int i = 0; i < keys.length; i++) {
                    keys[i] = prefix + keyList.get(i);
                }
                List<String> data = jedis.mget(keys);

                for (int i = 0; i < data.size(); ++i) {
                    T obj = JSON.parseObject(data.get(i), clazz, Feature.DisableCircularReferenceDetect);
                    if (obj != null) res.add(obj);
                }

            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            return res;
        }


//        public abstract <T> T setByPipeline(List<T> values);

        public abstract <T> T get(String key);

        public abstract <T> List<T> getList(List<String> keys);

        public abstract <T> List<T> getAllKeys(String pattern);
    }


    public static <T> List<T> getList(List<String> keyList, Class<T> clazz) {
        Jedis jedis = null;
        List<T> res = Lists.newArrayList();

        if (CollectionUtils.isEmpty(keyList)) {
            return res;
        }

        try {
            jedis = jedisPool.getResource();

            String[] keys = new String[keyList.size()];
            keyList.toArray(keys);
            List<String> data = jedis.mget(keys);

            for (int i = 0; i < data.size(); ++i) {
                T obj = JSON.parseObject(data.get(i), clazz, Feature.DisableCircularReferenceDetect);
                if (obj != null) {
                    res.add(obj);
                }
            }

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }



    /**
     * 将一个member及其socre加入到set中
     *
     * @param key
     * @param score
     * @param member
     */
    public static void zadd(String key, double score, String member) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.zadd(key, score, member);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 为集合成员member的score增加增量increment并返回新的score值，
     * increment可以为负数但必须是float类型否则会返回error，若key不存在则会先新增member成员后再执行操作
     *
     * @param key
     * @param increment
     * @param member
     */
    public static Double zincrby(String key, double increment, String member) {

        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zincrby((key), increment, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 按索引返回key的成员区间，start和stop是有序集合的索引下标（从0开始），
     * 若stop<start返回空，若start或stop为负数表示从后开始如-1表示倒数第一个元素
     *
     * @param key
     * @param start
     * @param stop
     * @return
     */
//    public Set<String> zrange(K key, int start, int stop) {
//        ShardedJedis shardedJedis = shardedJedisPool.getResource();
//        try {
//            return shardedJedis.zrange(cacheKey(key), start, stop);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Sets.newConcurrentHashSet();
//        } finally {
//            if (shardedJedis != null) {
//                shardedJedisPool.returnResource(shardedJedis);
//            }
//        }
//    }

    /**
     * 和zrange类似，不过zrevrange的元素时倒序排列的
     *
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public static Set<String> zrevrange(String key, int start, int stop) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrevrange((key), start, stop);
        } catch (Exception e) {
            e.printStackTrace();
            return Sets.newConcurrentHashSet();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
