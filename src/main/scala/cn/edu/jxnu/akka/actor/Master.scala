package cn.edu.jxnu.akka.actor

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, UntypedAbstractActor}
import cn.edu.jxnu.akka.actor.message.{CommitMessage, CommittedMessage, IndexedMessage}
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.entity.PageContent
import cn.edu.jxnu.akka.store.VisitedPageStore
import org.slf4j.LoggerFactory

/**
 * 主actor，实现了具体的执行逻辑，可以继承本来重写逻辑
 *
 */
abstract class Master(latch: CountDownLatch) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[Master])
    private val visitedPageStore = VisitedPageStore

    override def onReceive(message: Any) = {

        logger.info("Master actor is " + self)
        logger.info("Master type is " + message.getClass.getSimpleName)
        message match {
            //启动
            case start: String => {
                logger.info("Parse start")
                visitedPageStore.add(start)
                getParser() ! visitedPageStore.getNext()
            }
            //页面
            case (content: PageContent, _) => {
                logger.info("Find pages")
                getIndexer() ! content
                //存储待访问页面链接
                visitedPageStore.addAll(content.getLinksToFollow())
                if (visitedPageStore.isFinished()) {
                    //完成了则提交
                    getIndexer() ! CommitMessage(Constant.message_commit)
                } else {
                    logger.info("Total number of links is {},ToVisit number of links is {},InProgress number of links is {} ",
                        VisitedPageStore.getAllPagesCount.toString, VisitedPageStore.getPagesToVisitCount.toString,
                        VisitedPageStore.getInProgressCount.toString)
                    //继续获取下一个页面
                    for (page <- visitedPageStore.getNextBatch()) {
                        getParser() ! (page, self)
                    }
                }
            }
            //索引
            case (indexedMessage: IndexedMessage, _) => {
                logger.info("Indexer finished")
                visitedPageStore.finished(indexedMessage.getPath)
                if (visitedPageStore.isFinished()) {
                    logger.info("Indexer all finished")
                    getIndexer() ! CommitMessage(Constant.message_commit)
                }

            }
            //提交
            case (_: CommittedMessage, _) => {
                logger.info("All parser and indexer finished,then shutting down")
                getContext().system.terminate()
                latch.countDown()
            }
            case _ => {
                logger.info("Unknown execution steps")
            }
        }

    }

    //无具体类型的发送者，需要子类型来重写
    protected def getIndexer(): ActorRef

    protected def getParser(): ActorRef
}
