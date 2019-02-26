package cn.edu.jxnu.akka.actor.exec

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorSystem, Props}
import cn.edu.jxnu.akka.actor.ParallelActorMaster
import cn.edu.jxnu.akka.api.Execution
import cn.edu.jxnu.akka.api.impl.{Executor, HtmlParserPageRetriever}
import cn.edu.jxnu.akka.common.Constant
import org.apache.lucene.index.IndexWriter

/**
 * 并行actor
 */
class FetchInParallelExecution extends Execution {

    override def downloadAndIndex(path: String, writer: IndexWriter) = {
        val actorSystem = ActorSystem.create()
        val countDownLatch = new CountDownLatch(Constant.count_latch_size)
        val master = actorSystem.actorOf(Props.create(classOf[ParallelActorMaster], new HtmlParserPageRetriever(path),
            writer, countDownLatch))
        master ! (path)
        try {
            countDownLatch.await()
            actorSystem.terminate(); //旧版本是shutdown()
        } catch {
            case ex: InterruptedException => throw new IllegalStateException(ex)
        }
    }

}

object FetchInParallelExecution extends App {

    override def main(args: Array[String]) {
        val execution = new FetchInParallelExecution()
        val exec = new Executor(execution)
        exec.execute("http://www.baidu.com/")
    }
}
