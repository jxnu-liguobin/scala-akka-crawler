package cn.edu.jxnu.akka.http.proxy.website

import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.http.proxy.ProxyListPageParser
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.collection.JavaConversions

class XiaoHeXiaProxyListPageParser extends ProxyListPageParser {

    override def parse(html: String): java.util.List[cn.edu.jxnu.akka.http.proxy.Proxy] = {
        val document = Jsoup.parse(html)
        val elements: Elements = document.select("div[class=table-responsive] table tbody tr:gt(0)")
        val proxyList = new java.util.ArrayList[cn.edu.jxnu.akka.http.proxy.Proxy]
        for (element <- JavaConversions.asScalaIterator(elements.iterator())) {
            val ip = element.select("td:eq(0)").first.text
            val port = element.select("td:eq(1)").first.text
            val isAnonymous = element.select("td:eq(2)").first.text
            val `type` = element.select("td:eq(3)").first.text
            if (!anonymousFlag || isAnonymous.contains("匿") || isAnonymous.contains("anonymous")) {
                proxyList.add(new cn.edu.jxnu.akka.http.proxy.Proxy(ip, Integer.valueOf(port), `type`, Constant.time_interval))
            }
        }
        proxyList
    }
}
