package cn.edu.jxnu.akka.api.impl

import java.util
import java.util.List

import cn.edu.jxnu.akka.common.CollectTag
import cn.edu.jxnu.akka.common.util.ValidationUrl
import cn.edu.jxnu.akka.entity.PageContent
import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Node}
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
     * 但目前不需要
     *
     * @param node
     * @param depth
     */
    override def tail(node: Node, depth: Int): Unit = {}

    /**
     * 若看到开始标签、则认为存在该标签，不管结束与否
     *
     * @param node
     * @param depth
     */
    override def head(node: Node, depth: Int): Unit = {
        //只需要HTML元素
        if (depth < this.depth && node.isInstanceOf[Element]) {
            val element = node.asInstanceOf[Element]
            val text = element.text().trim
            element.tagName() match {
                case CollectTag.Tag_A => {
                    //TODO 会爆内存，需要队列
                    if (ValidationUrl.contentUrl(element.absUrl("href"))) {
                        logger.info("Using link pointing to {}", element.absUrl("href"))
                        if (linksToVisit.contains(element.absUrl("href"))) {
                            logger.info(element.absUrl("href") + " already exist")
                        } else {
                            linksToVisit.add(element.absUrl("href"))
                        }

                    } else {
                        logger.info("Skipping link pointing to {}", element.absUrl("href"))
                    }
                }
                case CollectTag.Tag_Body => {
                    content = text
                }
                case CollectTag.Tag_Title => {
                    title = text
                }
                case CollectTag.Tag_Img => {
                    val image = element.attr("src")
                    if (ValidationUrl.imgUrl(image)) {
                        imagePaths.add(image)
                    }
                }
                case CollectTag.Tag_Pre => {
                    preContent = text
                }
                case _ => {
                    logger.warn("{} node that do not require parsing in PageContent", node.nodeName())
                }
            }
        } else {
            logger.warn("Not a element")
        }

        if (depth >= this.depth) {
            logger.info("Depth exceeds the specified depth to be resolved")
        }
    }

    def getRoot(): Node = {

        val document = Jsoup.connect(this.baseUrl).get();
        document.root()
    }

    @volatile
    def getTitle(): String = title

    @volatile
    def getBody(): String = content

    @volatile
    def getPageContentWithImages(): PageContent = new PageContent(currentUrl, linksToVisit, title, content, imagePaths)

    @volatile
    def getPageContentWithoutImages(): PageContent = PageContent(currentUrl, linksToVisit, title, content)

    @volatile
    def getImages(): List[String] = this.imagePaths


    /**
     * 目前只是简单解析第一层
     *
     * @return
     */
    @deprecated
    def parse(): PageContent = {
        val document = Jsoup.connect(this.baseUrl).get();
        val emements: Elements = document.getAllElements
        for (ele <- JavaConversions.asScalaBuffer(emements)) {
            ele.tagName() match {
                case CollectTag.Tag_A => {
                    //TODO 会爆内存，需要队列
                    if (ValidationUrl.contentUrl(ele.absUrl("href"))) {
                        logger.info("Using link pointing to {}", ele.absUrl("href"))
                        if (linksToVisit.contains(ele.absUrl("href"))) {
                            logger.info(ele.absUrl("href") + "already exist")
                        }
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

}