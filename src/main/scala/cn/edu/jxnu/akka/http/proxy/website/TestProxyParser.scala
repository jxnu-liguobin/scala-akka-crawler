package cn.edu.jxnu.akka.http.proxy.website

import java.util

import cn.edu.jxnu.akka.http.proxy.{ProxyManager, ProxyPool}

/**
 * 测试代理页面
 */
object TestProxyParser extends App {

    override def main(args: Array[String]): Unit = {
        System.out.println("Start...")
        val proxyMap: util.HashMap[String, Class[_]] = new util.HashMap[String, Class[_]]()
        proxyMap.put("http://www.xiaohexia.cn/", classOf[XiaoHeXiaProxyListPageParser])
        ProxyPool.proxyMap = proxyMap
        val proxyManager = ProxyManager.get
        proxyManager.start
        System.out.println("End...")
    }

}

