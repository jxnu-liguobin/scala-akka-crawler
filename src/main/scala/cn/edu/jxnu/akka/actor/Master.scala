package cn.edu.jxnu.akka.actor

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, UntypedAbstractActor}
import cn.edu.jxnu.akka.actor.message.{COMMITTED_MESSAGE, COMMIT_MESSAGE, IndexedMessage}
import cn.edu.jxnu.akka.{PageContent, VisitedPageStore}
import org.slf4j.LoggerFactory

/**
 * 主actor，实现了具体的执行逻辑，可以继承本来重写逻辑
 *
 */
abstract class Master(latch: CountDownLatch) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[Master])
    private val visitedPageStore = new VisitedPageStore()

    override def onReceive(message: Any) = {

        logger.info("master,actor is:" + self)
        logger.info("Master当前消息类型：" + message.getClass.getSimpleName)
        message match {
            //启动
            case start: String => {
                logger.info("=======================start================")
                visitedPageStore.add(start)
                getParser() ! visitedPageStore.getNext()
            }
            //页面
            case (content: PageContent, _) => {
                logger.info("========================page==============")
                getIndexer() ! content
                //存储待访问页面链接
                visitedPageStore.addAll(content.getLinksToFollow())
                if (visitedPageStore.isFinished()) {
                    //完成了则提交
                    getIndexer() ! COMMIT_MESSAGE
                } else {
                    //继续获取下一个页面
                    for (page <- visitedPageStore.getNextBatch()) {
                        getParser() ! page
                    }
                }

            }
            //索引
            case indexedMessage: IndexedMessage=> {
                logger.info("====================index=================")
                visitedPageStore.finished(indexedMessage.getPath)
                if (visitedPageStore.isFinished())
                    getIndexer() ! COMMIT_MESSAGE

            }
            //提交
            case (COMMITTED_MESSAGE, _) => {
                logger.info("======================end================")
                logger.info("Shutting down, finished")
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
