package cn.edu.jxnu.akka.actor

import java.util.Optional

import akka.actor.{UntypedAbstractActor, actorRef2Scala}
import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.entity.PageContent
import org.slf4j.LoggerFactory

/**
 * 页面解析actor
 */
class PageParsingActor(pageRetriever: PageRetriever) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[PageParsingActor])

    override def onReceive(message: Any) = {

        logger.info("Page parsing actor is:" + self)
        logger.info("PageParsingActor type is：" + message.getClass.getSimpleName)
        message match {
            case msg: String => {
                val content: PageContent = pageRetriever.fetchPageContent(msg)
                sender ! (content, self)
            }
            case _ => unhandled(message)
        }
    }

    override def preRestart(reason: Throwable, message: Optional[Any]): Unit = {
        logger.info("Restarting PageParsingActor because of {}", reason.getClass)
        super.preRestart(reason, message)
    }

}
