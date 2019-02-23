package cn.edu.jxnu.akka.actor

import java.util.List
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

        message match {
            //启动
            case start: String => {
                logger.info("=======================start================")
                visitedPageStore.add(start)
                getParser().tell(visitedPageStore.getNext(), getSelf())
            }
            //页面
            case PageContent(path: String, links: List[String], title: String, content: String) => {
                logger.info("========================page==============")
                getIndexer().tell(PageContent(path, links, title, content), getSelf())
                //存储待访问页面链接
                visitedPageStore.addAll(links)
                if (visitedPageStore.isFinished()) {
                    //完成了则提交
                    getIndexer().tell(new COMMIT_MESSAGE(), getSelf())
                } else {
                    //继续获取下一个页面
                    for (page <- visitedPageStore.getNextBatch()) {
                        getParser().tell(page, getSelf())
                    }
                }

            }
            //索引
            case indexedMessage: IndexedMessage => {
                logger.info("====================index=================")
                visitedPageStore.finished(indexedMessage.getPath)
                if (visitedPageStore.isFinished())
                    getIndexer().tell(new COMMIT_MESSAGE(), getSelf())

            }
            //提交
            case _: COMMITTED_MESSAGE => {
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
