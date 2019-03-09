package cn.edu.jxnu.akka.http.proxy.website

import cn.edu.jxnu.akka.http.proxy.{ProxyManager, ProxyPool}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * 定期更新代理
 */
@Component
class ScheduleProxy {
    private val log = LoggerFactory.getLogger(classOf[ScheduleProxy])

    @Autowired
    var cacheManager: CacheManager = _

    val proxyManager: ProxyManager = ProxyManager.get

    @Scheduled(cron = "${cronJob.schedule}") def cronJob(): Unit = {
        ProxyPool.proxyMap.put("http://www.xiaohexia.cn/", classOf[XiaoHeXiaProxyListPageParser])
        if (cacheManager.getCache("proxys") != null) {
            cacheManager.getCache("proxys").clear
            log.info("Has exist and clear")
        }
        ProxyPool.proxyList.clear()
        proxyManager.start()
    }
}
