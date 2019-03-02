package cn.edu.jxnu.akka.actor

import akka.actor.Actor
import cn.edu.jxnu.akka.ImageUrlStore
import cn.edu.jxnu.akka.actor.message.{ImageDownloadMessage, ImageDownloadedMessage}
import cn.edu.jxnu.akka.common.util.DownloadUtil
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions

/**
 * 图片下载
 *
 * 使用简单actor
 */
class ImageDownloadActor extends Actor {

    private val logger = LoggerFactory.getLogger(classOf[ImageDownloadActor])

    override def receive: Receive = {
        case imageMessage: ImageDownloadMessage => {
            logger.info("Image download actor is " + self)
            logger.info("ImageDownloadActor type is " + this.getClass.getSimpleName)
            //开始图片下载
            if (imageMessage.getUrls().isEmpty) {
                logger.info("Pictures that do not need to be downloaded")
            } else {
                val imgs = JavaConversions.asScalaBuffer(imageMessage.getUrls())
                //临时保存
                ImageUrlStore.rightPushs(imgs: _*)
                //控制下载速度
                DownloadUtil.downloadImageBatch(ImageUrlStore.leftPollAll())
                var message = ""
                if (!ImageUrlStore.getImageInvalidList().isEmpty) {
                    logger.info("Pictures that can not be downloaded")
                    message = "Pictures that can not be downloaded"
                }
                if (!ImageUrlStore.getImageDuplicateList().isEmpty) {
                    if (!message.equals("")) {
                        message = message + "and there are duplicate links"
                    }
                    message = "There are duplicate links"
                    logger.info("There are duplicate links")
                }
                this.sender() ! ImageDownloadedMessage(message, ImageUrlStore.getImageDuplicateList(),
                    ImageUrlStore.getImageInvalidList())
            }
        }
        case _ => {
            logger.warn("Unmatched message types")
            this.unhandled()
        }
    }
}
