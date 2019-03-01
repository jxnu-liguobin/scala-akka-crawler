package cn.edu.jxnu.akka.api.impl

import java.util
import java.util.List

import cn.edu.jxnu.akka.common.CollectTag
import cn.edu.jxnu.akka.common.util.ValidationUrl
import cn.edu.jxnu.akka.entity.PageContent
import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.select.{Elements, NodeVisitor}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions

/**
 * 使用jsoup的新api的页面访问器
 */
class JsoupPageContentVisitor(depth: Int) extends NodeVisitor {

    private val logger = LoggerFactory.getLogger(classOf[JsoupPageContentVisitor])
    private val linksToVisit: util.ArrayList[String] = new util.ArrayList[String]()
    private val imagePaths: List[String] = new util.ArrayList[String]()

    private var content: String = _
    private var title: String = _
    private var baseUrl: String = _
    private var currentUrl: String = _
    private var preContent: String = _

    /**
     * 深度暂时没有用
     *
     * @param depth
     * @param baseUrl
     * @param currentUrl
     */
    def this(depth: Int, baseUrl: String, currentUrl: String) {
        this(depth)
        this.baseUrl = baseUrl
        this.currentUrl = currentUrl
    }

    /**
     * 需要重写
     *
     * @param node
     * @param depth
     */
    override def head(node: Node, depth: Int): Unit = {}

    /**
     * 需要重写
     *
     * @param node
     * @param depth
     */
    override def tail(node: Node, depth: Int): Unit = {}

    /**
     * 目前只是简单解析第一层
     *
     * @return
     */
    def parse(): PageContent = {
        val document = Jsoup.connect(this.baseUrl).get();
        val emements: Elements = document.getAllElements
        for (ele <- JavaConversions.asScalaBuffer(emements)) {
            ele.tagName() match {
                case CollectTag.Tag_A => {
                    //TODO 会爆内存，需要队列
                    if (ValidationUrl.contentUrl(ele.absUrl("href"))) {
                        logger.info("Using link pointing to {}", ele.absUrl("href"))
                        linksToVisit.add(ele.absUrl("href"))
                    } else {
                        logger.info("Skipping link pointing to {}", ele.absUrl("href"))
                    }
                }
                case CollectTag.Tag_Body => {
                    content = ele.text()
                }
                case CollectTag.Tag_Title => {
                    title = ele.text()
                }
                case CollectTag.Tag_Img => {
                    val image = ele.attr("src")

                    if (ValidationUrl.imgUrl(image)) {
                        imagePaths.add(image)
                    }

                }
                case CollectTag.Tag_Pre => {
                    preContent = ele.text()
                }
                case _ => {
                    logger.warn("Unknown type of label, unrecognizable")
                }
            }
        }
        getPageContentWithImages
    }

    def getTitle(): String = title

    def getBody(): String = content

    def getPageContentWithImages(): PageContent = new PageContent(currentUrl, linksToVisit, title, content, imagePaths)

    def getPageContentWithoutImages(): PageContent = PageContent(currentUrl, linksToVisit, title, content)

    def getImages(): List[String] = this.imagePaths
}