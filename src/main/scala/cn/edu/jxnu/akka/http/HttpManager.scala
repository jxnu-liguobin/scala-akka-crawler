package cn.edu.jxnu.akka.http

import java.io.IOException
import java.net.{InetSocketAddress, Socket}
import java.security.cert.X509Certificate
import java.security.{KeyManagementException, NoSuchAlgorithmException}
import java.util.Random

import cn.edu.jxnu.akka.common.{Constant, UserAgents}
import cn.edu.jxnu.akka.entity.Proxy
import javax.net.ssl.{SSLContext, TrustManager, X509TrustManager}
import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpRequestBase}
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.config.{Registry, RegistryBuilder}
import org.apache.http.conn.socket.{ConnectionSocketFactory, PlainConnectionSocketFactory}
import org.apache.http.conn.ssl.{NoopHostnameVerifier, SSLConnectionSocketFactory}
import org.apache.http.impl.client.{BasicCookieStore, CloseableHttpClient, HttpClients}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpHost, NoHttpResponseException}
import org.slf4j.LoggerFactory

/**
 * 含代理的HTTP管理器
 */
class HttpManager private() {

    private val logger = LoggerFactory.getLogger(classOf[HttpManager])


    /**
     * 创建新的HttpClient
     *
     * @return
     */
    def createHttpClient: CloseableHttpClient = createHttpClient(Constant.timeout.asInstanceOf[Int], null,
        null)


    /**
     * 获取Http客户端连接对象
     *
     * @param timeOut 超时时间
     * @param proxy   代理
     * @param cookie  Cookie
     * @return Http客户端连接对象
     */
    def createHttpClient(timeOut: Int, proxy: HttpHost, cookie: BasicClientCookie): CloseableHttpClient = {
        // 创建Http请求配置参数
        val builder = RequestConfig.custom.setConnectionRequestTimeout(timeOut).
          setConnectTimeout(timeOut).
          setSocketTimeout(timeOut).
          setCookieSpec(CookieSpecs.STANDARD)

        if (proxy != null) {
            logger.info("Use proxy for http")
            builder.setProxy(proxy)
        }

        val requestConfig = builder.build

        val httpClientBuilder = HttpClients.custom()

        // 把请求相关的超时信息设置到连接客户端
        httpClientBuilder.setDefaultRequestConfig(requestConfig).
          // 把请求重试设置到连接客户端
          setRetryHandler(new RetryHandler).
          // 配置连接池管理对象
          setConnectionManager(HttpManager.connManager)

        if (cookie != null) {
            val cookieStore = new BasicCookieStore
            cookieStore.addCookie(cookie)
            logger.info("Use Cookie for http")
            httpClientBuilder.setDefaultCookieStore(cookieStore)
        }

        httpClientBuilder.build
    }

    def getResponse(url: String): CloseableHttpResponse = getResponse(url, null)

    def getResponse(url: String, proxy: Proxy): CloseableHttpResponse = {
        val request = new HttpGet(url)
        getResponse(request, proxy)
    }

    def getResponse(request: HttpRequestBase, proxy: Proxy): CloseableHttpResponse = {
        request.setHeader("User-Agent", UserAgents.userAgentArray(new Random().nextInt(UserAgents.userAgentArray.length)))
        val httpClientContext = HttpClientContext.create
        var response: CloseableHttpResponse = null
        try {
            if (proxy == null) {
                response = createHttpClient.execute(request, httpClientContext)
            }
            else {
                response = createHttpClient(Constant.timeout, proxy.toHttpHost, null).
                  execute(request, httpClientContext)
            }
        }
        catch {
            case e: NoHttpResponseException => {
                logger.info(e.getMessage)
            }
            case e: IOException => {
                logger.info(e.getMessage)
            }
        }
        response
    }


    def checkProxy(proxy: Proxy): Boolean = {
        if (proxy == null) return false
        var socket: Socket = null
        try {
            socket = new Socket
            val endpointSocketAddr = new InetSocketAddress(proxy.getIp, proxy.getPort)
            socket.connect(endpointSocketAddr, Constant.proxy_timeout)
            true
        } catch {
            case e: IOException => {
                logger.info(e.getMessage)
                false
            }
        } finally {
            if (socket != null) {
                socket.close()
            }
        }
    }


    def checkProxy(proxy: HttpHost): Boolean = {
        if (proxy == null) return false
        var socket: Socket = null
        try {
            socket = new Socket
            val endpointSocketAddr = new InetSocketAddress(proxy.getHostName, proxy.getPort)
            socket.connect(endpointSocketAddr, Constant.proxy_timeout)
            true
        } catch {
            case e: IOException => {
                logger.info(e.getMessage)
                false
            }
        } finally {
            if (socket != null) {
                socket.close()
            }
        }
    }

    @throws[IOException]
    def getWebPage(url: String): ResponseInfo = getWebPage(url, Constant.charset, null)

    @throws[IOException]
    def getWebPage(url: String, proxy: Proxy): ResponseInfo = getWebPage(url, Constant.charset, proxy)

    @throws[IOException]
    def getWebPage(url: String, charset: String, proxy: Proxy): ResponseInfo = {
        val page = new ResponseInfo
        var response: CloseableHttpResponse = null
        if (proxy == null) {
            response = HttpManager.get.getResponse(url)
        }
        else {
            response = HttpManager.get.getResponse(url, proxy)
        }
        if (response != null) {
            page.setStatusCode(response.getStatusLine.getStatusCode)
            page.setUrl(url)
            try {
                if (page.getStatusCode == 200) {
                    page.setHtml(EntityUtils.toString(response.getEntity, charset))
                }
            }
            catch {
                case e: IOException => {
                    logger.info(e.getMessage)
                    e.printStackTrace()
                }
            } finally {
                if (response != null) {
                    response.close()
                }
            }
        }
        else {
            page.setUrl(url)
        }
        page
    }

}

object HttpManager {

    /**
     * 全局连接池对象
     */
    private var connManager: PoolingHttpClientConnectionManager = _

    /**
     * 工厂方法
     */
    def get: HttpManager = new HttpManager()


    /**
     * 配置连接池信息，支持http/https
     */
    var sslcontext: SSLContext = null
    try {
        //获取TLS安全协议上下文
        sslcontext = SSLContext.getInstance("TLS")
        sslcontext.init(null, Array[TrustManager](new X509TrustManager() {
            override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}

            override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}

            override def getAcceptedIssuers: Array[X509Certificate] = Array[X509Certificate]()
        }), null)

        val scsf: SSLConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE)
        //        val defaultConfig: RequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).
        //          setExpectContinueEnabled(true).
        //          setTargetPreferredAuthSchemes(util.Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).
        //          setProxyPreferredAuthSchemes(util.Arrays.asList(AuthSchemes.BASIC)).build()

        val sfr: Registry[ConnectionSocketFactory] = RegistryBuilder.create[ConnectionSocketFactory]().
          register("http", PlainConnectionSocketFactory.INSTANCE).register("https", scsf).build()

        connManager = new PoolingHttpClientConnectionManager(sfr)
        // 设置最大连接数
        connManager.setMaxTotal(200)
        // 设置每个连接的路由数
        connManager.setDefaultMaxPerRoute(20)
    } catch {
        case e1: NoSuchAlgorithmException => {
            e1.printStackTrace()
        }
        case e2: KeyManagementException => {
            e2.printStackTrace()
        }
    }
}
