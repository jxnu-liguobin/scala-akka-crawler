package cn.edu.jxnu.akka.actor

import akka.actor.UntypedAbstractActor
import cn.edu.jxnu.akka.PageContent
import cn.edu.jxnu.akka.actor.message.{COMMITTED_MESSAGE, COMMIT_MESSAGE, IndexedMessage}
import cn.edu.jxnu.akka.api.Indexer
import org.slf4j.LoggerFactory

/**
 * 执行索引的actor
 *
 */
class IndexingActor(indexer: Indexer) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[IndexingActor])

    //无类型化actor，与之相对的TypedActor
    /**
     * 无类型化具体实现方法
     * actor
     * 该方法其实是actor的receive方法的实现，源码：final def receive = { case msg ⇒ onReceive(msg) }
     *
     * @param message
     */
    override def onReceive(message: Any) = {

        logger.info("indexing,actor is:" + self)
        logger.info("IndexingActor当前消息类型：" + message.getClass.getSimpleName)
        message match {
            //页面消息
            case (content: PageContent, _) => {
                indexer.index(content)
                this.getSender() ! IndexedMessage(content.getPath())
            }
            //索引提交消息
            case (_: COMMIT_MESSAGE, _) => {
                indexer.commit()
                //getSelf可以获取actor的ActorRef引用
                this.getSender() ! COMMITTED_MESSAGE
            }
            //其他消息
            case _ => {
                this.unhandled(message)
            }
        }
    }
}
