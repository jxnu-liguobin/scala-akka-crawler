package cn.edu.jxnu.akka.actor.message

/**
 * 下载完成
 */
case class ImageDownloadedMessage(message: String, duplicateUrl: java.util.List[String], invalidUrl: java.util.List[String]) {

    //下载失败，或者无效链接则返回url

    def getMessage(): String = message

    def getDuplicateUrl(): java.util.List[String] = duplicateUrl

    def getInvalidUrl(): java.util.List[String] = invalidUrl

}
