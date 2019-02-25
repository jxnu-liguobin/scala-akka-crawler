package cn.edu.jxnu.akka.actor

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, Props}
import cn.edu.jxnu.akka.api.PageRetriever
import cn.edu.jxnu.akka.api.impl.IndexerImpl
import org.apache.lucene.index.IndexWriter
import org.slf4j.LoggerFactory

/**
 * 简单的主actor执行类
 *
 * @param latch Java并发门阀，用于限定并行数
 */
class SimpleActorMaster(latch: CountDownLatch) extends Master(latch) {

    private val logger = LoggerFactory.getLogger(classOf[SimpleActorMaster])


    private var indexer: ActorRef = _
    private var parser: ActorRef = _

    def this() {
        this(null)
    }

    def this(pageRetriever: PageRetriever, indexWriter: IndexWriter) = {

        this(null)
        this.indexer = getContext().actorOf(Props.create(classOf[IndexingActor], new IndexerImpl(indexWriter)))
        //使用无类型角色工厂建造解析器
        this.parser = getContext().actorOf(Props.create(classOf[PageParsingActor], pageRetriever))
        logger.info("SimpleActorMaster constructor executed")
    }

    protected def getIndexer(): ActorRef = this.indexer

    protected def getParser(): ActorRef = this.parser

}