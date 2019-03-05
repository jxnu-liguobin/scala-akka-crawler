package cn.edu.jxnu.akka.actor

import java.util.concurrent.CountDownLatch

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{ActorRef, AllForOneStrategy, Props, SupervisorStrategy}
import akka.routing.RoundRobinPool
import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.api.impl.IndexerImpl
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.exception.{IndexingException, RetrievalException}
import org.apache.lucene.index.IndexWriter
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration

/**
 * 并发的主actor执行类
 */
class ParallelActorMaster(latch: CountDownLatch) extends Master(latch) {

    private val logger = LoggerFactory.getLogger(classOf[ParallelActorMaster])

    private var parser: ActorRef = _
    private var indexer: ActorRef = _

    def this(pageRetriever: PageRetriever, indexWriter: IndexWriter, latch: CountDownLatch) = {

        this(latch)
        //使用路由
        parser = getContext().actorOf(Props.create(classOf[PageParsingActor], pageRetriever).
          withRouter(new RoundRobinPool(Constant.round_robin_pool_size)).withDispatcher("worker-dispatcher"))
        indexer = getContext().actorOf(Props.create(classOf[IndexingActor], new IndexerImpl(indexWriter)))
        logger.info("ParallelMaster constructor executed")
    }

    def this(pageRetriever: PageRetriever, indexWriter: IndexWriter) = {

        this(null)
        //使用路由
        parser = getContext().actorOf(Props.create(classOf[PageParsingActor], pageRetriever).
          withRouter(new RoundRobinPool(10)).withDispatcher("worker-dispatcher"))
        indexer = getContext().actorOf(Props.create(classOf[IndexingActor], new IndexerImpl(indexWriter)))
        logger.info("ParallelMaster constructor executed")
    }

    protected override def getIndexer(): ActorRef = indexer

    protected override def getParser(): ActorRef = parser


    //Constant.timeout改小点，就容易连接超时，复现失败策略（Constant有缓存，还是直接改方法入参吧）
    //AllForOneStrategy，影响同级或同层所有actor
    override def supervisorStrategy: SupervisorStrategy = AllForOneStrategy(maxNrOfRetries = 5, Duration.create("1 minute"), true) {

        //索引是IO操作，挂了就停止，还跑个毛。
        case _: IndexingException => Stop
        //重启，不保留状态，重新抓取页面
        case _: RetrievalException => Restart
        //其他异常暂时先终止
        case _: Exception => Stop
    }
}
