package cn.edu.jxnu.akka

import cn.edu.jxnu.akka.http.proxy.website.XiaoHeXiaProxyListPageParser
import cn.edu.jxnu.akka.http.proxy.{ProxyManager, ProxyPageCallable, ProxyPool}
import javax.servlet.{ServletContextEvent, ServletContextListener}
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 初始化代理
 */
@Component
class InitProxyListener extends ServletContextListener {

    private val log = LoggerFactory.getLogger(classOf[ProxyPageCallable])

    val proxyManager = ProxyManager.get

    override def contextInitialized(sce: ServletContextEvent): Unit = {
        log.info("Init proxy")
        ProxyPool.proxyMap.put("http://www.xiaohexia.cn/", classOf[XiaoHeXiaProxyListPageParser])
        ProxyPool.proxyList.clear()
        proxyManager.start()
    }

    override def contextDestroyed(sce: ServletContextEvent): Unit = {}
}
