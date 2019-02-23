package cn.edu.jxnu.akka.actor.exec

import akka.actor.{ActorSystem, Props}
import cn.edu.jxnu.akka.actor.ParallelActorMaster
import cn.edu.jxnu.akka.api.Execution
import cn.edu.jxnu.akka.api.impl.{ChaosMonkeyPageRetriever, Executor}
import org.apache.lucene.index.IndexWriter

/**
 * 失败
 */
class FailingExecution extends Execution {

    def downloadAndIndex(path: String, writer: IndexWriter): Unit = {
        val actorSystem = ActorSystem.create
        val master = actorSystem.actorOf(Props.create(classOf[ParallelActorMaster], new ChaosMonkeyPageRetriever(path), writer))
        master ! path
        //        actorSystem.terminate()
    }
}

object FailingExecution extends App {

    override def main(args: Array[String]): Unit = {
        val execution = new FailingExecution
        val executor = new Executor(execution)
        executor.execute("http://www.baidu.com")
    }
}