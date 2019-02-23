package cn.edu.jxnu.akka.actor.message

/**
 * 索引消息
 */
case class IndexedMessage(path: String) {

    def getPath = path
}
