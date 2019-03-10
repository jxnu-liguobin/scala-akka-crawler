package cn.edu.jxnu.akka.http.proxy

import java.io.IOException
import java.util
import java.util.List
import java.util.concurrent.Callable

import cn.edu.jxnu.akka.entity.Proxy
import cn.edu.jxnu.akka.http.{HttpManager, ResponseInfo}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions

/**
 * 代理获取回调
 */
class ProxyPageCallable extends Callable[List[Proxy]] {

    private val log = LoggerFactory.getLogger(classOf[ProxyPageCallable])

    protected var url: String = _

    def this(url: String) {
        this()
        this.url = url
    }

    override def call(): List[Proxy] = {
        val requestStartTime = System.currentTimeMillis
        try {
            val page = HttpManager.get.getWebPage(url)
            val status = page.getStatusCode
            val requestEndTime = System.currentTimeMillis
            var sb = new StringBuilder()
            sb.append(Thread.currentThread().getName()).append(" ").
              append("  ,executing request ").append(page.getUrl()).append(" ,response statusCode:").append(status).
              append("  ,request cost time:").append(requestEndTime - requestStartTime).append("ms")
            val logStr = sb.toString
            import org.apache.http.HttpStatus
            if (status == HttpStatus.SC_OK) {
                log.info("Success: " + logStr)
                return handle(page)
            }
            else if (status >= 400) {
                var proxy: Proxy = null
                for (i <- 0 until 3) {
                    proxy = ProxyPool.getProxy // 从代理池中获取数据
                    val requestStartTime = System.currentTimeMillis
                    val page = HttpManager.get.getWebPage(url, proxy)
                    val status = page.getStatusCode
                    val requestEndTime = System.currentTimeMillis
                    sb = new StringBuilder
                    sb.append(Thread.currentThread.getName).append(" ").append("  ,executing request ").append(page.getUrl).append(" ,response statusCode:").append(status).append("  ,request cost time:").append(requestEndTime - requestStartTime).append("ms")
                    import org.apache.http.HttpStatus
                    if (status == HttpStatus.SC_OK) {
                        log.info("Success: " + logStr)
                        return handle(page)
                    }
                    else {
                        log.info("Failure: " + logStr)
                    }
                }
            } else {
                log.info("Failure: " + logStr)
            }
        } catch {
            case e: IOException => {
                log.info("IOException: e=" + e.getMessage)
            }
        }
        new util.ArrayList[Proxy]
    }

    /**
     * 将下载的proxy放入代理池
     *
     * @param page
     */
    private def handle(page: ResponseInfo): java.util.ArrayList[Proxy] = {
        if (page == null || page.getHtml == null) return new java.util.ArrayList[Proxy]
        val result = new java.util.ArrayList[Proxy]
        val parser = ProxyListPageParserFactory.getProxyListPageParser(ProxyPool.proxyMap.get(url))
        if (parser != null) {
            val proxyList = parser.parse(page.getHtml)
            if (proxyList != null && proxyList.size != 0) {
                for (p <- JavaConversions.asScalaIterator(proxyList.iterator())) {
                    if (!ProxyPool.proxyList.contains(p)) result.add(p)
                }
            }
        }
        result
    }
}
