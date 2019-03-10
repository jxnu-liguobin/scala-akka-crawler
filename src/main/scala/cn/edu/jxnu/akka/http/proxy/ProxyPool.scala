package cn.edu.jxnu.akka.http.proxy

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

import cn.edu.jxnu.akka.entity.Proxy
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.{CacheEvict, Cacheable}

/**
 * 代理池
 */
object ProxyPool {

    private val log = LoggerFactory.getLogger(ProxyPool.getClass)

    //Java中使用时变成静态方法，调用需要括号
    var proxyList = new CopyOnWriteArrayList[Proxy]
    var proxyMap = new java.util.HashMap[String, Class[_]]()
    val index = new AtomicInteger

    /**
     * 采用round robin算法获取Proxy
     */
    @Cacheable(value = Array("proxys"))
    def getProxy: Proxy = {
        var result: Proxy = null
        if (ProxyPool.proxyList.size > 0) {
            if (ProxyPool.index.get > ProxyPool.proxyList.size - 1) {
                ProxyPool.index.set(0)
            }
            result = ProxyPool.proxyList.get(ProxyPool.index.get)
            ProxyPool.index.incrementAndGet
        }
        log.info("Proxy is {}", result)
        result
    }

    @CacheEvict
    def addProxy(proxy: Proxy): Unit = {
        if (proxy != null) ProxyPool.proxyList.add(proxy)
    }

    def addProxyList(proxies: java.util.List[Proxy]): Unit = {
        if (proxies != null && proxies.size() != 0) ProxyPool.proxyList.addAll(proxies)
    }
}