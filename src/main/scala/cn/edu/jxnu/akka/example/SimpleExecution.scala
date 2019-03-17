package cn.edu.jxnu.akka.example

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, ActorSystem, Props}
import cn.edu.jxnu.akka.api.Execution
import cn.edu.jxnu.akka.api.impl.{Executor, HtmlParserPageRetriever}
import org.apache.lucene.index.IndexWriter

/**
 * 单actor执行
 * 仅测试用
 */
@deprecated
class SimpleExecution extends Execution {

    override def downloadAndIndex(path: String, writer: IndexWriter): Unit = {

        //TODO BUG
        val actorSystem: ActorSystem = ActorSystem.create
        val countDownLatch = new CountDownLatch(1)
        val master: ActorRef = actorSystem.actorOf(Props.create(classOf[SimpleActorMaster],
            new HtmlParserPageRetriever(path), writer, countDownLatch))
        master ! path
        try {
            countDownLatch.await()
            actorSystem.terminate();
        } catch {
            case ex: InterruptedException => throw new IllegalStateException(ex)
        }
    }

}

object SimpleExecution extends App {

    override def main(args: Array[String]): Unit = {
        val execution = new SimpleExecution
        val exec = new Executor(execution)
        exec.execute("http://www.baidu.com/")
    }

}

