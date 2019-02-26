package cn.edu.jxnu.akka.api

import cn.edu.jxnu.akka.entity.PageContent

/**
 * 页面解析接口定义
 */
abstract class PageRetriever {
    /**
     * 获取content内容
     *
     * @param url
     * @return
     */
    def fetchPageContent(url: String): PageContent
}
