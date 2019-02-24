package cn.edu.jxnu.akka.api.impl

import java.util._

import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.{PageContent, RetrievalException}
import org.htmlparser.tags.{BodyTag, LinkTag, TitleTag}
import org.htmlparser.util.ParserException
import org.htmlparser.visitors.NodeVisitor
import org.htmlparser.{Parser, Tag}
import org.slf4j.LoggerFactory

/**
 * 具体的页面解析实现
 *
 */
class HtmlParserPageRetriever(baseUrl: String) extends PageRetriever {

    private val logger = LoggerFactory.getLogger(classOf[HtmlParserPageRetriever])

    /**
     * 获取html页content内容
     *
     * @param url
     * @return
     */
    override def fetchPageContent(url: String): PageContent = {

        logger.info("Fetching {}", url)
        try {
            val parser: Parser = new Parser(url)
            val visitor: PageContentVisitor = new PageContentVisitor(baseUrl, url)
            parser.visitAllNodesWith(visitor)
            visitor.getContent()
        } catch {
            case ex: ParserException => throw new RetrievalException(ex.getMessage)
        }
    }

    /**
     * 页面访问逻辑
     *
     * @param recursive 是否递归
     */
    class PageContentVisitor(recursive: Boolean) extends NodeVisitor(recursive) {

        private val linksToVisit: List[String] = new ArrayList[String]()
        private var content: String = _
        private var title: String = _
        private var baseUrl: String = _
        private var currentUrl: String = _

        def this(baseUrl: String, currentUrl: String) {
            this(true)
            this.baseUrl = baseUrl
            this.currentUrl = currentUrl

        }

        override def visitTag(tag: Tag) = {
            tag match {
                case linkTag: LinkTag => {
                    if (linkTag.getLink().startsWith(baseUrl) && isProbablyHtml(linkTag.getLink())) {
                        logger.info("Using link pointing to {}", linkTag.getLink())
                        linksToVisit.add(linkTag.getLink())
                    } else {
                        logger.info("Skipping link pointing to {}", linkTag.getLink())
                    }
                }
                case titleTag: TitleTag => {
                    title = titleTag.getTitle()
                }
                case bodyTag: BodyTag => {
                    content = bodyTag.toPlainTextString()
                }
                case _ => {
                    logger.warn("Unknown type of label, unrecognizable")
                }
            }
        }

        def getContent(): PageContent = new PageContent(currentUrl, linksToVisit, title, content)

        private def isProbablyHtml(link: String): Boolean = link.startsWith("http://") ||
          link.startsWith("https://") || link.endsWith("/")
    }

}