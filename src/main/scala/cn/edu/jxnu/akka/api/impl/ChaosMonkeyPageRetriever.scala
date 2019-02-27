package cn.edu.jxnu.akka.api.impl

import cn.edu.jxnu.akka.entity.PageContent
import cn.edu.jxnu.akka.{ExceptionConstant, RetrievalException}

/**
 * 随机
 */
@Deprecated
class ChaosMonkeyPageRetriever(baseUrl: String) extends HtmlParserPageRetriever(baseUrl) {

    override def fetchPageContent(url: String): PageContent = {
        if (System.currentTimeMillis % 20 == 0) throw new RetrievalException(ExceptionConstant.ETRIEVAL_CODE_MONKEY,
            ExceptionConstant.ETRIEVAL_MESSAGE_MONKEY)
        super.fetchPageContent(url)
    }
}
