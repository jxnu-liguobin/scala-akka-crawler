package cn.edu.jxnu.akka.actor

import akka.actor.{UntypedAbstractActor, actorRef2Scala}
import cn.edu.jxnu.akka.PageContent
import cn.edu.jxnu.akka.api.PageRetriever
import org.slf4j.LoggerFactory

/**
 * 页面解析actor
 */
class PageParsingActor(pageRetriever: PageRetriever) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[PageParsingActor])

    override def onReceive(message: Any) = {
        message match {
            case msg: String => {
                val content: PageContent = pageRetriever.fetchPageContent(msg)
                sender ! (content, self)
                logger.info("page parsing,actor is:" + self)
            }
            case _ => unhandled(message)
        }
    }
}
