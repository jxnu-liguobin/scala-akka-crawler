package cn.edu.jxnu.akka.actor

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, Props}
import akka.routing.RoundRobinPool
import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.api.impl.IndexerImpl
import cn.edu.jxnu.akka.common.Constant
import org.apache.lucene.index.IndexWriter
import org.slf4j.LoggerFactory

/**
 * 并发的主actor执行类
 *
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

}
