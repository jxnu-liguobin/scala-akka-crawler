package cn.edu.jxnu.akka.api.impl

import java.util

import cn.edu.jxnu.akka.api.PageVisitor
import cn.edu.jxnu.akka.entity.PageContent

/**
 * 使用jsoup的新api的页面访问器
 */
class JsoupPageContentVisitor extends PageVisitor {


    /**
     * 获得标题
     *
     * @return
     */
    override def getTitle(): String = ???

    /**
     * 获得内容
     *
     * @return
     */
    override def getBody(): String = ???

    /**
     * 获得图片地址
     *
     * @return
     */
    override def getImages(): util.List[String] = ???

    /**
     * 获取含有图片地址的页面内容
     *
     * @return
     */
    override def getPageContentWithImages(): PageContent = ???

    /**
     * 获取没有图片链接地址的页面内容
     *
     * @return
     */
    override def getPageContentWithoutImages(): PageContent = ???

    /**
     * 是否需要加入爬取链接
     *
     * @return
     */
    override def isProbablyHtml(link: String): Boolean = ???
}
