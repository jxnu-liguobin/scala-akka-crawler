package cn.edu.jxnu.akka.entity

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

    //图片src属性是个set
    protected var imagePaths: List[String] = _

    def getImagePaths(): List[String] = imagePaths

    def this(path: String, linksToFollow: List[String], title: String, content: String, imagePath: List[String]) = {
        this(path, linksToFollow, title, content)
        this.imagePaths = imagePath
    }

}
