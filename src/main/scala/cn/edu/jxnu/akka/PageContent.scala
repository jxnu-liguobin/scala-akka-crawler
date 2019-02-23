package cn.edu.jxnu.akka

import java.util.List

/**
 * 页面对象
 */
@SerialVersionUID(1)
case class PageContent(path: String, linksToFollow: List[String], title: String, content: String) {

    def getContent(): String = content

    def getLinksToFollow(): List[String] = linksToFollow

    def getTitle(): String = title

    def getPath(): String = path


}
