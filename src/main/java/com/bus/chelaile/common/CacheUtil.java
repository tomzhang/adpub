package com.bus.chelaile.common;


import net.spy.memcached.internal.OperationFuture;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bus.chelaile.common.cache.ICache;
import com.bus.chelaile.common.cache.OCSCacheUtil;
import com.bus.chelaile.common.cache.RedisAtraceCacheImplUtil;
import com.bus.chelaile.common.cache.RedisBUSCacheImplUtil;
import com.bus.chelaile.common.cache.RedisCacheImplUtil;
import com.bus.chelaile.common.cache.RedisTBKCacheImplUtil;
import com.bus.chelaile.common.cache.RedisTokenCacheImplUtil;
import com.bus.chelaile.model.PropertiesName;
import com.bus.chelaile.service.StaticAds;
import com.bus.chelaile.util.config.PropertiesUtils;

public class CacheUtil {
    //	访问得到token缓存
	private static ICache client;
	//	redis缓存
	private static ICache redisClient;
	// 存储token的redis
	private static ICache redisToken;
	// 存储tbk title的redis
    private static ICache redisTBK;
    // 存储 traceInfo的redis
    private static ICache redisAtrace;
    // 存储bus后台设置的cshow值的redis
    private static ICache redisBUS;
	//  用来获取用户头像的redis
//	private static ICache redisWow;
	//	保存用户访问量等信息
	private static ICache cacheNewClient;
	//	获取access_token的 
	private static ICache cacheApiTokenClient;
//	支付信息
	private static ICache cachePayInfoClient;
//	//  专为活动[屈臣氏]所设,放置有效用户，以及领取过
//	private static ICache cacheActivitiesClient;
	
	private static boolean isInitSuccess = false;
	
	protected static final Logger logger = LoggerFactory
			.getLogger(CacheUtil.class);
	
    private static final int DEFAULT_EXPIRE = 60 * 60;
    
    private static String cacheType = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "cacheType","ocs");
    /**
     * 推送的时候读取token
     */
    private static final String PROP_OCS_HOST = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.host");
    private static final String PROP_OCS_PORT = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.port");
    private static final String PROP_OCS_USERNAME =  PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.username");
    private static final String PROP_OCS_PASSWORD = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.password");
    /**
     * 存储用户信息
     */
    private static final String CC_PROP_OCS_HOST = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.new.host");
    private static final String CC_PROP_OCS_PORT = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.new.port");
    private static final String CC_PROP_OCS_USERNAME = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.new.username");
    private static final String CC_PROP_OCS_PASSWORD = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.new.password");
    
    /**
     * 第三方信息
     */
    private static final String API_PROP_OCS_HOST = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.api.host");
    private static final String API_PROP_OCS_PORT = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.api.port");
    private static final String API_PROP_OCS_USERNAME = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.api.username");
    private static final String API_PROP_OCS_PASSWORD = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.api.password");
    
    /**
     *基础数据获取‘线路是否支持支付’
     */
    private static final String PAY_PROP_OCS_HOST = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.pay.host");
    private static final String PAY_PROP_OCS_PORT = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.pay.port");
    private static final String PAY_PROP_OCS_USERNAME = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.pay.username");
    private static final String PAY_PROP_OCS_PASSWORD = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.pay.password");
    //private static String cacheType = PropertiesReaderWrapper.read("cacheType", "ocs");
    
    private static final String defauleTBKTitle = PropertiesUtils.getValue(PropertiesName.PUBLIC.getValue(), "default.tbk.title");
    
//    /**
//     * 屈臣氏活动专用
//     */
//    private static final String ACTIVE_PROP_OCS_HOST = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.activities.host");
//    private static final String ACTIVE_PROP_OCS_PORT = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.activities.port");
//    private static final String ACTIVE_PROP_OCS_USERNAME = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.activities.username");
//    private static final String ACTIVE_PROP_OCS_PASSWORD = PropertiesUtils.getValue(PropertiesName.CACHE.getValue(), "ocs.activities.password");

//    static {
//        initClient();
//    }

    public static void initClient() {
    	
       if( isInitSuccess ){
    	   logger.info(cacheType+"已经reload成功");
    	   return;
       }
    	
       if( cacheType.equals("redis") ){
    	   client = new RedisCacheImplUtil();
    	   cacheNewClient = new RedisCacheImplUtil();
    	   cacheApiTokenClient = new RedisCacheImplUtil();
//    	   cacheActivitiesClient = new RedisCacheImplUtil();
    	   logger.info("redis cache");
       }else	if( cacheType.equals("ocs") ){
    	   client = new OCSCacheUtil(PROP_OCS_HOST,PROP_OCS_PORT,PROP_OCS_USERNAME,PROP_OCS_PASSWORD);
    	   cacheNewClient = new OCSCacheUtil(CC_PROP_OCS_HOST,CC_PROP_OCS_PORT,CC_PROP_OCS_USERNAME,CC_PROP_OCS_PASSWORD);
    	   
    	   cacheApiTokenClient = new OCSCacheUtil(API_PROP_OCS_HOST,API_PROP_OCS_PORT,API_PROP_OCS_USERNAME,API_PROP_OCS_PASSWORD);
    	   cachePayInfoClient = new OCSCacheUtil(PAY_PROP_OCS_HOST,PAY_PROP_OCS_PORT,PAY_PROP_OCS_USERNAME,PAY_PROP_OCS_PASSWORD);
//    	   cacheActivitiesClient = new OCSCacheUtil(ACTIVE_PROP_OCS_HOST,ACTIVE_PROP_OCS_PORT,ACTIVE_PROP_OCS_USERNAME,ACTIVE_PROP_OCS_PASSWORD);
    	   logger.info("ocs cache");
       }else{
    	   throw new IllegalArgumentException("未找到cacheType类型");
       }
       redisClient = new RedisCacheImplUtil();
       redisToken = new RedisTokenCacheImplUtil();
       redisTBK = new RedisTBKCacheImplUtil();
       redisBUS = new RedisBUSCacheImplUtil();
       redisAtrace = new RedisAtraceCacheImplUtil();
       isInitSuccess = true;
    }
    

    
    /**
     * @param key the Cache Key
     * @param exp the expiration time of the records, should not exceeds 60 * 60 * 24 * 30(30 天), 单位: 秒
     * @param obj 缓存的对象
     */
    public static void set(String key, int exp, Object obj) {
  
        client.set(key, exp, obj);
      
    }
    
    public static void setToRedis(String key,int exp,Object obj){
    	redisClient.set(key, exp, obj);
    }
    
    public static Object getFromRedis(String key){
    	return redisClient.get(key);
    }
    
    public static void incrToCache(String key, int exp){
    	redisClient.IncValue(key, exp);
    }
    
    public static void redisDelete(String key){
    	redisClient.delete(key);
    }
    
    
    public static Map<String, Object> getValueFromRedisByList(List<String> list){
    	return redisClient.getByList(list);
    }
    
    public static boolean acquireLock() {
    	return redisClient.acquireLock();
    }
    
    public static boolean releaseLock() {
    	return redisClient.releaseLock();
    }
    
    public static void redisIncrBy(String key, int number, int exp) {
    	redisClient.incrBy(key, number, exp);
    }
    
    // redis 有序集合 3个方法
    public static void setSortedSet(String key, long score,String value, int expire) {
    	redisClient.setSortedSet(key, score, value, expire);
    }
    
    public static Set<String> getRangeSet(String key, long startScore, long endScore, int count) {
    	return redisClient.zrangeByScore(key, startScore, endScore, count);
    }
    
    public static Set<String> getRevRangeSet(String key, long startScore, long endScore, int count) {
    	return redisClient.zrevRangeByScore(key, endScore, startScore, count);
    }
    
    /**
     * 设置缓存， 默认缓存时间是1小时。
     * @param key 缓存的key
     * @param obj 缓存的对象
     */
    public static void set(String key, Object obj) {
        set(key, DEFAULT_EXPIRE, obj);
    }
    
    public static Object get(String key) {
       return client.get(key);
    }
    
    public static Object getNew(String key) {
        return cacheNewClient.get(key);
     }
    
    public static void setNew(String key, int exp, Object obj) {
    	cacheNewClient.set(key, exp, obj);
    }
    
    public static OperationFuture<Boolean> deleteNew(String key) {
        return cacheNewClient.delete(key);
    }

    public static OperationFuture<Boolean> delete(String key) {
    	return client.delete(key);
    }

    public static Map<String, Object> getByList(List<String> list) {
    	return cachePayInfoClient.getByList(list);
    }
    
    public static Object getApiInfo(String key){
    	return cacheApiTokenClient.get(key);
    }
    
//    public static Set<String> getWowDatas(String key) {
//    	return redisWow.getSet(key);
//    }
    
    // ocs查询
    public static String getFromCommonOcs(String key) {
    	return (String) cachePayInfoClient.get(key);
    }
    
    // ocs设置值
    public static void setToCommonOcs(String key, int exp, Object obj) {
    	cachePayInfoClient.set(key, exp, obj);
    }
    
    // token存储升级，在这一步做一些处理，保证输出跟老版本一致 2018-04-02
    // 输出类似：120c83f760217151408|android|3
    public static String getTokenFromRedis(String udid) {
        Map<String, String> tokenMap = redisToken.getHsetAll("UDID2TOKEN#" + udid);
        logger.info("tokenMap.content={}", tokenMap.toString());
        logger.info("tokenMap.size={}", tokenMap.size());
        if(tokenMap == null || tokenMap.size() < 3) {
            return null;
        } else {
            String token = tokenMap.get("token");
            String sys = tokenMap.get("sys");
            String tokenType = tokenMap.get("tokenType");
            return new StringBuilder().append(token).append("|").append(sys).append("|").append(tokenType).toString();
        }
    }
    
    // 从redis中获取淘宝客title，随机拿出一个
    public static String getTBKTitle(int advId) {
        if (StaticAds.advTBKTitleKey.containsKey(advId)) {
            String key = StaticAds.advTBKTitleKey.get(advId);
            String value = (String) redisTBK.get(key);
            if (StringUtils.isNotEmpty(value)) {
                String[] titles = value.split("#");
                int size = titles.length;
                return titles[new Random().nextInt(size)];
            }
        }
        return defauleTBKTitle;
    }
    
    // 从BUS redis中获取数据
    public static Object getFromBUSRedis(String key){
        return redisBUS.get(key);
    }
    
    // 从redis中获取所有的keys，慎用！ 
    public static Set<String> allKeys(String pattern) {
        return redisClient.allKeys(pattern);
    }
 
    // 将traceInfo保存到redis中, 永久
    public static void setToAtrace(String key, String value) {
        redisAtrace.set(key, -1, value);
    }
    // 同上
    public static void setToAtrace(String key, String value, int exp) {
        redisAtrace.set(key, exp, value);
    }
    
//	public static Object getActiveOcs(String key) {
//		return cacheActivitiesClient.get(key);
//	}
//
//	public static void setActiveOcs(String key, int exp, Object obj) {
//		cacheActivitiesClient.set(key, exp, obj);
//	}
//	
//	public static OperationFuture<Boolean> activitiesDelete(String key) {
//        return cacheActivitiesClient.delete(key);
//    }
    
    public static void main(String[] args) throws InterruptedException {
//    	initClient();
//    	setToRedis("a", -1, "12");
//    	System.out.println(getFromRedis("a"));
//    	
//    	System.out.println(System.currentTimeMillis());
//    	setSortedSet("wuli_hot", System.currentTimeMillis(), "{\"text\":\"11111\"}", -1);
//    	Thread.sleep(1);
//    	setSortedSet("wuli_hot", System.currentTimeMillis(), "{\"text\":\"22222\"}", -1);
//    	Thread.sleep(1);
//    	setSortedSet("wuli_hot", System.currentTimeMillis(), "{\"text\":\"33333\"}", -1);
//    	Thread.sleep(1);
//    	setSortedSet("wuli_hot", System.currentTimeMillis(), "{\"text\":\"44444\"}", -1);
//    	Thread.sleep(1);
//    	setSortedSet("wuli_hot", System.currentTimeMillis(), "{\"text\":\"55555\"}", -1);
//    	Thread.sleep(1);
//    	setSortedSet("wuli_hot", System.currentTimeMillis(), "{\"text\":\"66666\"}", -1);
//    	System.out.println(System.currentTimeMillis());
//    	
//    	
//    	Set<String> tops = getRangeSet("wuli_hot", 1513067900290l, System.currentTimeMillis(), 20); // 最新内容
//    	System.out.println(tops);
//    	
//    	
//    	Set<String> ends = getRevRangeSet("wuli_hot", 0L, 1513067900290L, 20);  // 历史 
//    	System.out.println(ends);
    	
    	
    	ICache client1 = new OCSCacheUtil("121.40.210.76","7078","9c0e27d0f09544c9","Yuanguang2014");
    	client1.set("qkk_test1", Constants.ONE_DAY_TIME, "1111");
    	System.out.println(client1.get("qkk_test1"));
    	System.out.println(client1.get("qkk_test2"));
    	
    	System.out.println(client1.get("REMINDERTOKEN#fb6d0547-b3ba-435b-ba29-001a1bbe261b"));
    	
    	
    	// ************8  redis test
    	JedisPoolConfig config = new JedisPoolConfig();
        String host = "127.0.0.1";
        int port = 6379;
        config.setMaxTotal(400);
        //config.setMaxActive(400);
        config.setMaxIdle(200);
        config.setMinIdle(20);

        //config.setMaxWait(2000000);
        //config.setMaxWaitMillis();
        config.setTestWhileIdle(true);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        JedisPool pool = new JedisPool(config, host, port);
        Map<String, String> result = null;
        Jedis conn = null;
        //set
        conn = pool.getResource();
        long result1 = conn.hset("testMap", "1", "1");
        pool.returnResource(conn);
        
        conn = pool.getResource();
        result = conn.hgetAll("testMap");
        pool.returnResource(conn);
        
        for(Entry<String, String> s : result.entrySet()) {
            System.out.println(s.getKey() + "--->" + s.getValue());
        }
        System.out.println(result.get("1"));
    	System.exit(1);
    }
}
