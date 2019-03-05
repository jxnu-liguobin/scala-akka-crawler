package cn.edu.jxnu.akka.actor

import java.util.Optional

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{ActorRef, OneForOneStrategy, Props, SupervisorStrategy, UntypedAbstractActor, actorRef2Scala}
import cn.edu.jxnu.akka.actor.message.{ImageDownloadMessage, ImageDownloadedMessage}
import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.common.ExceptionConstant
import cn.edu.jxnu.akka.entity.PageContent
import cn.edu.jxnu.akka.exception.DownloadException
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration

/**
 * 页面解析actor
 */
class PageParsingActor(pageRetriever: PageRetriever) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[PageParsingActor])
    private val downloadImage = getContext().actorOf(Props.create(classOf[ImageDownloadActor]))

    override def onReceive(message: Any) = {

        logger.info("Page parsing actor is " + self)
        logger.info("PageParsingActor type is " + message.getClass.getSimpleName)
        message match {
            case msg: String => {
                val content: PageContent = pageRetriever.fetchPageContentWithJsoup(msg)
                sender ! (content, self)
                //页面解析完成后就开始下载图片，而不是等到索引完成，因为索引是单线程IO
                getDownloadImg ! ImageDownloadMessage(content.getImagePaths())

            }
            case imageMessaged: ImageDownloadedMessage => {

                if (imageMessaged.getMessage().equals(ExceptionConstant.DOWNLOAD_MESSAGE_IMAGE)) {
                    logger.error("Download fail")
                }
                logger.info("Download images finished")
                logger.info("Result message is " + imageMessaged.getMessage)
                logger.info("There are {} duplicate links", imageMessaged.getDuplicateUrl().size())
                logger.info("There are {} invalid links", imageMessaged.getInvalidUrl().size())
                //进一步交给master处理
            }
            case _ => unhandled(message)
        }
    }

    override def preRestart(reason: Throwable, message: Optional[Any]): Unit = {
        logger.info("Restarting PageParsingActor because of {}", reason.getClass)
        super.preRestart(reason, message)
    }

    protected def getDownloadImg(): ActorRef = downloadImage

    //一分钟内五次
    //OneForOneStrategy只会应用于崩溃的actor对象
    override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5, Duration.create("1 minute"), true) {

        //图片下载挂了继续干啊，重新下载！
        case _: DownloadException => Restart
        //其他异常上抛，不管了
        case _: Exception => Escalate

    }

}
