package cn.edu.jxnu.akka.actor

import akka.actor.{Props, UntypedAbstractActor}
import cn.edu.jxnu.akka.actor.message.{CommitMessage, CommittedMessage, IndexedMessage, SearchMessage}
import cn.edu.jxnu.akka.api.Indexer
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.common.util.DownloadUtil
import cn.edu.jxnu.akka.entity.PageContent
import org.slf4j.LoggerFactory

/**
 * 执行索引的actor
 *
 */
class IndexingActor(indexer: Indexer) extends UntypedAbstractActor {

    private val search = getContext().actorOf(Props.create(classOf[SearchActor]))

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

        logger.info("Indexing actor is:" + self)
        logger.info("IndexingActor type is：" + message.getClass.getSimpleName)
        message match {
            //页面消息
            case content: PageContent => {
                indexer.index(content)
                //查出来
                this.search ! SearchMessage(Constant.message_search_all)
                //下载图片
                DownloadUtil.downloadBatch(content.getImagePaths())
                //报告给主线程
                this.getSender() ! IndexedMessage(content.getPath())
            }
            //索引提交消息
            case _: CommitMessage => {
                //报告索引完成
                indexer.commit()
                //getSelf可以获取actor的ActorRef引用
                this.getSender() ! CommittedMessage(Constant.message_committed)
            }
            //其他消息
            case _ => {
                this.unhandled(message)
            }
        }
    }
}
