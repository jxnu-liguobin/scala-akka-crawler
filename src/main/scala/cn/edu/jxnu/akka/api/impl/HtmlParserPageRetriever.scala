package cn.edu.jxnu.akka.api.impl

import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.entity.PageContent
import cn.edu.jxnu.akka.{ExceptionConstant, RetrievalException}
import org.htmlparser.Parser
import org.htmlparser.util.ParserException
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
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE)
            }
        }
    }

    override def fetchPageContentWithJsoup(url: String): PageContent = {
        logger.info("Use jsoup Fetching {}", url)
        try {

            val jsoupPageContentVisitor = new JsoupPageContentVisitor(Constant.depth_dom, baseUrl, url)
            val traversor = new NodeTraversor(jsoupPageContentVisitor)
            traversor.traverse(jsoupPageContentVisitor.getRoot())
            jsoupPageContentVisitor.getPageContentWithImages()

        } catch {
            case ex: ParserException => {
                logger.error(ex.getMessage)
                throw new RetrievalException(ExceptionConstant.ETRIEVAL_MESSAGE_JSOUP)
            }
        }
    }

}