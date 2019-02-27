package cn.edu.jxnu.akka.api

import java.util.List

import cn.edu.jxnu.akka.entity.PageContent

/**
 * @author 梦境迷离
 * @time 2019-02-27
 */
trait PageVisitor {

    /**
     * 获得标题
     *
     * @return
     */
    def getTitle(): String

    /**
     * 获得内容
     *
     * @return
     */
    def getBody(): String

    /**
     * 获得图片地址
     *
     * @return
     */
    def getImages(): List[String]

    /**
     * 获取含有图片地址的页面内容
     *
     * @return
     */
    def getPageContentWithImages(): PageContent


    /**
     * 获取没有图片链接地址的页面内容
     *
     * @return
     */
    def getPageContentWithoutImages(): PageContent


    /**
     * 是否需要加入爬去链接
     *
     * @return
     */
    def isProbablyHtml(link: String): Boolean

}
