package cn.edu.jxnu.akka.actor

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{ActorRef, AllForOneStrategy, Props, SupervisorStrategy, UntypedAbstractActor}
import cn.edu.jxnu.akka.actor.message._
import cn.edu.jxnu.akka.api.Indexer
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.entity.PageContent
import cn.edu.jxnu.akka.exception.IndexingException
import cn.edu.jxnu.akka.store.VisitedPageStore
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration

/**
 * 执行索引的actor
 * 单线程IO
 */
class IndexingActor(indexer: Indexer) extends UntypedAbstractActor {

    private val logger = LoggerFactory.getLogger(classOf[IndexingActor])

    private val search = getContext().actorOf(Props.create(classOf[SearchActor]))

    //无类型化actor，与之相对的TypedActor
    /**
     * 无类型化具体实现方法
     * actor
     * 该方法其实是actor的receive方法的实现，源码：final def receive = { case msg ⇒ onReceive(msg) }
     *
     * @param message
     */
    override def onReceive(message: Any) = {

        logger.info("Indexing actor is " + self)
        logger.info("IndexingActor type is " + message.getClass.getSimpleName)
        message match {
            //页面消息
            case content: PageContent => {
                try {
                    indexer.index(content)
                } catch {
                    //吃掉异常
                    case ie: IndexingException => {
                        logger.error(ie.getMessage)
                        logger.warn("url {} has an exception", ie.url)
                        VisitedPageStore.finished(content.getPath())
                    }
                }
                //报告给主线程
                this.getSender() ! (IndexedMessage(content.getPath()), self)
            }
            //索引提交消息
            case _: CommitMessage => {
                //报告索引完成
                indexer.commit()
                //getSelf可以获取actor的ActorRef引用
                this.getSender() ! (CommittedMessage(Constant.message_committed), self)
                //查出来
                search ! SearchMessage(Constant.message_search_all)
            }
            //其他消息
            case _ => {
                this.unhandled(message)
            }
        }
    }

    protected def getSearch(): ActorRef = search

    //AllForOneStrategy
    override def supervisorStrategy: SupervisorStrategy = AllForOneStrategy(maxNrOfRetries = 5, Duration.create("1 minute"), true) {

        //查询挂了，保留状态，继续开始
        //只有父actor能控制失败策略，而不是与它通信的actor
        case _: Exception => Resume
    }

}
