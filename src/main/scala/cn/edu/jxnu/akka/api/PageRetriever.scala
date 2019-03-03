package cn.edu.jxnu.akka.api

import cn.edu.jxnu.akka.entity.PageContent

/**
 * 页面爬取器接口
 */
abstract class PageRetriever {

    /**
     * 使用htmlparser
     *
     * @param url
     * @return
     */
    @deprecated
    def fetchPageContent(url: String): PageContent

    /**
     * 使用jsoup
     *
     * @param url
     * @return
     */
    def fetchPageContentWithJsoup(url: String): PageContent = null
}
