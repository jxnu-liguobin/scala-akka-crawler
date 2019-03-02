package cn.edu.jxnu.akka.actor.message

/**
 * 图片下载开始消息
 */
case class ImageDownloadMessage(url: java.util.List[String]) {

    def getUrls(): java.util.List[String] = url

}
