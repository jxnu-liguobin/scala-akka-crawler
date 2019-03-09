package cn.edu.jxnu.akka.http.proxy

import java.util

/**
 * 代理页面解析器获取工厂
 */
object ProxyListPageParserFactory {

    private val map = new util.HashMap[String, ProxyListPageParser]

    def getProxyListPageParser(clazz: Class[_]): ProxyListPageParser = {
        var parserName = clazz.getSimpleName
        if (ProxyListPageParserFactory.map.containsKey(parserName)) return ProxyListPageParserFactory.map.get(parserName)
        else try {
            val parser = clazz.newInstance.asInstanceOf[ProxyListPageParser]
            parserName = clazz.getSimpleName
            ProxyListPageParserFactory.map.put(parserName, parser)
            return parser
        } catch {
            case e: InstantiationException =>
                e.printStackTrace()
            case e: IllegalAccessException =>
                e.printStackTrace()
        }
        null
    }
}