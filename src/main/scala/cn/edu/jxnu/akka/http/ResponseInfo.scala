package cn.edu.jxnu.akka.http

import scala.beans.BeanProperty

/**
 * 验证代理的http返回体
 */
case class ResponseInfo() {

    @BeanProperty
    var url: String = _

    @BeanProperty
    var statusCode: Int = _

    @BeanProperty
    var html: String = _

    def this(url: String, statusCode: Int, html: String) {
        this()
        this.html = html
        this.statusCode = statusCode
        this.url = url
    }
}
