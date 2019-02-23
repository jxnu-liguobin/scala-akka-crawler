package cn.edu.jxnu.akka.api.impl

import cn.edu.jxnu.akka.{PageContent, RetrievalException}

/**
 * 随机
 */
class ChaosMonkeyPageRetriever(baseUrl: String) extends HtmlParserPageRetriever(baseUrl) {

    override def fetchPageContent(url: String): PageContent = { // this error rate is derived from scientific measurements
        if (System.currentTimeMillis % 20 == 0) throw new RetrievalException("Something went horribly wrong when fetching the page.")
        super.fetchPageContent(url)
    }
}
