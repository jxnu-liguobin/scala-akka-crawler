package cn.edu.jxnu.akka.http

import java.io.{IOException, InterruptedIOException}
import java.net.UnknownHostException

import javax.net.ssl.{SSLException, SSLHandshakeException}
import org.apache.http.client.HttpRequestRetryHandler
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.conn.ConnectTimeoutException
import org.apache.http.protocol.HttpContext
import org.apache.http.{HttpEntityEnclosingRequest, NoHttpResponseException}

/**
 * HTTP重试处理器
 */
class RetryHandler extends HttpRequestRetryHandler {


    override def retryRequest(exception: IOException, executionCount: Int, context: HttpContext): Boolean = {

        exception match {
            // 如果服务器丢掉了连接，那么就重试
            case _: NoHttpResponseException => true
            // 不要重试SSL握手异常
            case _: SSLHandshakeException => false
            // 超时
            case _: InterruptedIOException => true
            // 目标服务器不可达
            case _: UnknownHostException => false
            // 连接被拒绝
            case _: ConnectTimeoutException => false
            // ssl握手异常
            case _: SSLException => false
        }
        // 如果已经重试了3次，就放弃
        if (executionCount >= 3) {
            return false
        }
        val clientContext = HttpClientContext.adapt(context)
        val request = clientContext.getRequest
        // 如果请求是幂等的，就再次尝试
        if (!request.isInstanceOf[HttpEntityEnclosingRequest]) return true
        false
    }
}
