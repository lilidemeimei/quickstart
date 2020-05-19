package org.myorg.redisUtil;

import com.google.common.io.Resources;
import org.myorg.data.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import org.apache.commons.io.IOUtils;
import java.util.Properties;

public class RedisUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    private JedisPool jedisPool = null;
    private String redis_host;
    private int redis_port;
    private String redis_pass;
    private int redis_max_wait;
    private int redis_time_out;

    public RedisUtil() {
        Properties properties = new Properties();
        try {
            properties.load(Resources.getResource("config.properties").openStream());
        } catch (Exception e) {
            throw new RuntimeException("Load config fail.", e);
        }

        this.redis_host = properties.getProperty("redis_host");
        this.redis_port = Integer.parseInt(properties.getProperty("redis_port"));
        this.redis_pass = properties.getProperty("redis_pass");
        this.redis_max_wait = Integer.parseInt(properties.getProperty("redis_max_wait"));
        this.redis_time_out = Integer.parseInt(properties.getProperty("redis_time_out"));
    }

    public void open() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(redis_max_wait);
        jedisPool = new JedisPool(config, redis_host, redis_port, redis_time_out, redis_pass);
    }

    public void set() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "hh";
            Message.Info.Builder builder = Message.Info.newBuilder();
            builder.setId(123);
            byte[] infoByte = builder.build().toByteArray();
//            jedis.set(key, );

        } catch (Exception e) {
            LOGGER.warn("Get srcg into  exception: " + e);
        } finally {
            IOUtils.closeQuietly(jedis);
        }
    }

    public static void main(String []args) {
       RedisUtil rc = new RedisUtil();
       rc.set();
    }
}
