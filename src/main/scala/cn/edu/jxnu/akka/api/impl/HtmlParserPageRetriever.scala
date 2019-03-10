package cn.edu.jxnu.akka.api.impl

import java.net.SocketTimeoutException

import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.common.util.ValidationUrl
import cn.edu.jxnu.akka.common.{Constant, ExceptionConstant}
import cn.edu.jxnu.akka.entity.{PageContent, Proxy}
import cn.edu.jxnu.akka.exception.{ProxyException, RetrievalException}
import cn.edu.jxnu.akka.http.RequestInfo
import cn.edu.jxnu.akka.http.proxy.ProxyPool
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

    @deprecated
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
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE)
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
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_URL)
            }
            val con: Connection = HttpConnection.connect(url)
            requestInfo.setConnection(con)
            traversor.traverse(jsoupPageContentVisitor.startParser(requestInfo))
            jsoupPageContentVisitor.getPageContentWithImages()

        } catch {
            case ex: ParserException => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_JSOUP)
            }
        }
    }

    override def fetchPageContent(url: String, useProxy: Boolean): PageContent = {
        logger.info("Use jsoup Fetching {},and proxy switch is {}", url, useProxy)
        if (!useProxy) {
            fetchPageContentWithJsoup(url)
        }
        else {
            try {
                val jsoupPageContentVisitor = new JsoupPageContentVisitor(Constant.depth_dom, baseUrl, url)
                val traversor = new NodeTraversor(jsoupPageContentVisitor)
                //自己传入需要设置的参数，和需要使用的HTTPConnection
                val requestInfo: RequestInfo = new RequestInfo()
                requestInfo.setMethod(Connection.Method.GET)
                requestInfo.setTimeout(Constant.timeout)
                if (!ValidationUrl.contentUrl(url)) {
                    throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_URL)
                }
                val proxy: Proxy = ProxyPool.getProxy
                val javaProxy: java.net.Proxy = proxy.toJavaNetProxy
                requestInfo.setProxy(javaProxy)
                val con: Connection = HttpConnection.connect(url)
                requestInfo.setConnection(con)
                traversor.traverse(jsoupPageContentVisitor.startParser(requestInfo))
                jsoupPageContentVisitor.getPageContentWithImages()
            } catch {
                case ex: ParserException => {
                    logger.error(ex.getMessage)
                    throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_JSOUP)
                }
                case ex: SocketTimeoutException => {
                    logger.error(ex.getMessage)
                    throw new ProxyException(ExceptionConstant.PROXY_MESSAGE_TIMEOUT)

                }
                case ex: NullPointerException => {
                    logger.error("Can not find proxy")
                    logger.error(ex.getMessage)
                    fetchPageContentWithJsoup(url)
                    //                    throw new ProxyException(ExceptionConstant.PROXY_MESSAGE_NNP)
                }
            }
        }
    }

}