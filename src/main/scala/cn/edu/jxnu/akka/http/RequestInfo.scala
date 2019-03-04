package cn.edu.jxnu.akka.http

import org.jsoup.Connection

import scala.beans.BeanProperty

/**
 * 请求携带的实体
 */
class RequestInfo {


    //    @BeanProperty var url: String = _

    @BeanProperty var proxyIp: String = _

    @BeanProperty var proxyPort: Int = _

    @BeanProperty var userAgent: String = _

    //毫秒
    @BeanProperty var timeout: Int = _

    @BeanProperty var method: Connection.Method = _

    @BeanProperty var ignoreHttpErrors: Boolean = _

    @BeanProperty var headers: java.util.Map[String, String] = _

    @BeanProperty var cookies: java.util.Map[String, String] = _

    @BeanProperty var connection: Connection = _


    override def toString = s"RequestInfo($proxyIp, $proxyPort, $userAgent, $timeout, $method, $ignoreHttpErrors, $headers, $cookies, $connection)"
}
