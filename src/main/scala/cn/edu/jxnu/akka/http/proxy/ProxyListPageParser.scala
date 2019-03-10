package cn.edu.jxnu.akka.http.proxy

import cn.edu.jxnu.akka.entity.Proxy

/**
 * 代理获取接口
 *
 * 必须是抽象类
 */
abstract class ProxyListPageParser {

    /**
     * 是否只要匿名代理
     */
    val anonymousFlag = true

    def parse(content: String): java.util.List[Proxy]

}
