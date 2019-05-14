package cn.edu.jxnu.akka.api.impl

import java.net.SocketTimeoutException

import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.common.{Constant, ExceptionConstant}
import cn.edu.jxnu.akka.entity.{PageContent, Proxy}
import cn.edu.jxnu.akka.exception.{ConnectionException, ProxyException, RetrievalException}
import cn.edu.jxnu.akka.http.RequestInfo
import cn.edu.jxnu.akka.http.proxy.ProxyPool
import cn.edu.jxnu.akka.util.ValidationUrl
import org.htmlparser.Parser
import org.htmlparser.util.ParserException
import org.jsoup.Connection
import org.jsoup.helper.HttpConnection
import org.jsoup.select.NodeTraversor
import org.slf4j.LoggerFactory

/**
 * 具体的页面爬取器实现
 *
 */
class HtmlParserPageRetriever(baseUrl: String) extends PageRetriever {

    private val logger = LoggerFactory.getLogger(classOf[HtmlParserPageRetriever])

    override def fetchPageContent(url: String): PageContent = {

        logger.info("Use htmlparser Fetching {}", url)
        try {

            val parser: Parser = new Parser(url)
            parser.setEncoding("utf-8")
            val visitor = new PageContentVisitor(true, baseUrl, url)
            parser.visitAllNodesWith(visitor)
            visitor.getPageContentWithImages()

        } catch {
            case ex: ParserException => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE, url = url)
            }
        }
    }

    override def fetchPageContentWithJsoup(url: String): PageContent = {
        logger.info("Use jsoup Fetching {}", url)
        try {

            val jsoupPageContentVisitor = new JsoupPageContentVisitor(Constant.depth_dom, baseUrl, url)
            val traversor = new NodeTraversor(jsoupPageContentVisitor)
            //自己传入需要设置的参数，和需要使用的HTTPConnection
            val requestInfo: RequestInfo = new RequestInfo()
            requestInfo.setMethod(Connection.Method.GET)
            requestInfo.setTimeout(Constant.timeout)
            if (!ValidationUrl.contentUrl(url)) {
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_URL, url = url)
            }
            val con: Connection = HttpConnection.connect(url)
            requestInfo.setConnection(con)
            traversor.traverse(jsoupPageContentVisitor.startParser(requestInfo))
            jsoupPageContentVisitor.getPageContentWithImages()

        } catch {
            case ce: ConnectionException => {
                logger.error(ce.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_JSOUP_CONNECT, url = url)
            }
            case ex: ParserException => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_JSOUP, url = url)
            }
            case ex: Exception => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_OTHER, url = url)
            }
        }
    }

    override def fetchPageContent(url: String, useProxy: Boolean): PageContent = {
        logger.info("Use jsoup Fetching {},and proxy switch is {}", url, useProxy)

        try {
            val jsoupPageContentVisitor = new JsoupPageContentVisitor(Constant.depth_dom, baseUrl, url)
            val traversor = new NodeTraversor(jsoupPageContentVisitor)
            //自己传入需要设置的参数，和需要使用的HTTPConnection
            val requestInfo: RequestInfo = new RequestInfo()
            requestInfo.setMethod(Connection.Method.GET)
            requestInfo.setTimeout(Constant.timeout)
            if (!ValidationUrl.contentUrl(url)) {
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_URL, url = url)
            }
            if (useProxy) {
                val proxy: Proxy = ProxyPool.getProxy
                val javaProxy: java.net.Proxy = proxy.toJavaNetProxy
                requestInfo.setProxy(javaProxy)
            }
            val con: Connection = HttpConnection.connect(url)
            requestInfo.setConnection(con)
            val doc = jsoupPageContentVisitor.startParser(requestInfo)
            traversor.traverse(doc)
            jsoupPageContentVisitor.getPageContentWithImages()

        } catch {
            case ex: ParserException => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_JSOUP, url = url)
            }
            case ex: SocketTimeoutException => {
                logger.error(ex.getMessage)
                throw new ProxyException(ExceptionConstant.PROXY_MESSAGE_TIMEOUT, url = url)

            }
            case ex: Exception => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_OTHER, url = url)
            }
        }
    }
}