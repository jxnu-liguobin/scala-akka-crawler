package cn.edu.jxnu.akka.http

import org.jsoup.Connection

/**
 * 处理请求需要携带的信息
 *
 * @param requestInfo
 */
class ConnectionExecuter(requestInfo: RequestInfo) {

    /**
     * 封装了自定义的请求头处理
     */
    //    @throws
    def connect() = {
        setRequestParams().get()
    }

    /**
     * 只输入url不能调用本方法
     */
    private def setRequestParams(): Connection = {

        val connection = requestInfo.getConnection

        if (requestInfo.getCookies != null) {
            connection.cookies(requestInfo.getCookies)
        }
        if (requestInfo.getHeaders != null) {
            connection.headers(requestInfo.getHeaders)
        }
        if (requestInfo.getMethod != null) {
            connection.method(requestInfo.getMethod)
        }
        if (requestInfo.getIgnoreHttpErrors != null) {
            connection.ignoreHttpErrors(requestInfo.getIgnoreHttpErrors)
        }
        if (requestInfo.getProxy != null) {
            connection.proxy(requestInfo.getProxy)
        }
        if (requestInfo.getTimeout != null && requestInfo.getTimeout > 0) {
            connection.timeout(requestInfo.getTimeout)
        }
        if (requestInfo.getUserAgent != null) {
            connection.userAgent(requestInfo.getUserAgent)
        }

        connection
    }

}
