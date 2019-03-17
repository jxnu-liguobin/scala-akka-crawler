package cn.edu.jxnu.akka.actor

import akka.actor.Actor
import cn.edu.jxnu.akka.actor.message.{ImageDownloadMessage, ImageDownloadedMessage}
import cn.edu.jxnu.akka.common.{Constant, ExceptionConstant}
import cn.edu.jxnu.akka.exception.DownloadException
import cn.edu.jxnu.akka.store.ImageUrlStore
import cn.edu.jxnu.akka.util.DownloadUtil
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
                //临时保存全局链接，这里并不判断或记录图片是否完成，只判断了下载是否正常，需要换成缓存、队列
                ImageUrlStore.rightPushAll(imgs: _*)
                //可能阻塞，future有回调
                try {
                    logger.info("Total img size is {}", ImageUrlStore.getImageQueue().size())
                    logger.info("To download img size is {}", ImageUrlStore.getImagesUrlToDownloadQueue().size())
                    var message = ""
                    val downloadRet = DownloadUtil.downloadFuture(ImageUrlStore.leftPollAll(),
                        Constant.is_tailoring_img, Constant.delete_the_original_image)
                    //无效和下载失败的链接需要等待图片下载完成才能获取，否则可能获取不到最新值
                    if (downloadRet.isCompleted && downloadRet.value.get.isSuccess) {
                        if (!ImageUrlStore.getImageInvalidList().isEmpty) {
                            logger.info("Pictures that can not be downloaded")
                            message = "Pictures that can not be downloaded"
                        }
                    }

                    if (!ImageUrlStore.getImageDuplicateList().isEmpty) {
                        if (!message.equals("")) {
                            message = message + "and there are duplicate links"
                        } else {
                            message = "There are duplicate links"
                            logger.info("There are duplicate links")
                        }
                    }
                    this.sender() ! (ImageDownloadedMessage(message, ImageUrlStore.getImageDuplicateList(),
                        ImageUrlStore.getImageInvalidList()), self)

                } catch {
                    case ex: DownloadException => {
                        logger.info("Download fail and message is {}", ex.getMessage)
                        this.sender() ! (ImageDownloadedMessage(ExceptionConstant.DOWNLOAD_MESSAGE_IMAGE, null, null), self)
                    }
                }
            }
        }

        case _ => {
            logger.warn("Unmatched message types")
            this.unhandled()
        }
    }

}
