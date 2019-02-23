package cn.edu.jxnu.akka

import cn.edu.jxnu.akka.api.impl.HtmlParserPageRetriever
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.slf4j.LoggerFactory

class HtmlParserPageRetrieverTest {

    private val pageRetriever = new HtmlParserPageRetriever("https://www.baidu.com")
    private val logger = LoggerFactory.getLogger(classOf[HtmlParserPageRetrieverTest])

    @Test def contentIsExtracted() {
        val content = pageRetriever.fetchPageContent("https://blog.csdn.net/farYang/article/details/52685351")
        assertNotNull(content)
        assertNotNull(content.getContent())
        assertNotNull(content.getTitle())
        logger.info(content.toString())
        println(content)
    }

}
