package cn.edu.jxnu.akka.api.impl

import java.util
import java.util.List

import cn.edu.jxnu.akka.api.PageVisitor
import cn.edu.jxnu.akka.entity.PageContent
import cn.edu.jxnu.akka.util.ValidationUrl
import org.htmlparser.Tag
import org.htmlparser.tags.{BodyTag, ImageTag, LinkTag, TitleTag}
import org.htmlparser.visitors.NodeVisitor
import org.slf4j.LoggerFactory

/**
 * 页面访问逻辑
 *
 * @param recursive 是否递归
 */
class PageContentVisitor(recursive: Boolean) extends NodeVisitor(recursive) with PageVisitor {


    private val logger = LoggerFactory.getLogger(classOf[PageContentVisitor])

    private val linksToVisit: util.ArrayList[String] = new util.ArrayList[String]()
    private val imagePaths: List[String] = new util.ArrayList[String]()

    private var content: String = _
    private var title: String = _
    private var baseUrl: String = _
    private var currentUrl: String = _

    def this(recursive: Boolean, baseUrl: String, currentUrl: String) {
        this(recursive)
        this.baseUrl = baseUrl
        this.currentUrl = currentUrl
    }

    //TODO  慢，待改
    override def visitTag(tag: Tag) = {

        tag match {
            case linkTag: LinkTag => {
                //TODO 会爆内存，需要队列
                if (isProbablyHtml(linkTag.getLink())) {
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
            //TODO 不通用
            case imageTag: ImageTag => {
                val src = imageTag.getAttribute("src")
                imagePaths.add(src)
            }
            case _ => {
                logger.warn("Unknown type of label, unrecognizable")
            }
        }
    }

    override def isProbablyHtml(link: String): Boolean = ValidationUrl.contentUrl(link)

    override def getTitle(): String = title

    override def getBody(): String = content

    override def getPageContentWithImages(): PageContent = new PageContent(currentUrl, linksToVisit, title, content, imagePaths)

    override def getPageContentWithoutImages(): PageContent = PageContent(currentUrl, linksToVisit, title, content)

    def getImages(): List[String] = this.imagePaths

}
