package cn.edu.jxnu.akka.actor.exec

import akka.actor.{ActorRef, ActorSystem, Props}
import cn.edu.jxnu.akka.actor.SimpleActorMaster
import cn.edu.jxnu.akka.api.Execution
import cn.edu.jxnu.akka.api.impl.{Executor, HtmlParserPageRetriever}
import org.apache.lucene.index.IndexWriter

/**
 * 单actor执行
 */
class SimpleExecution extends Execution {

    override def downloadAndIndex(path: String, writer: IndexWriter): Unit = {

        //TODO BUG
        val actorSystem: ActorSystem = ActorSystem.create
        val master: ActorRef = actorSystem.actorOf(Props.create(classOf[SimpleActorMaster],
            new HtmlParserPageRetriever(path), writer))
        master ! path
        //        actorSystem.terminate(); //旧版本是shutdown()
    }

}

object SimpleExecution extends App {

    override def main(args: Array[String]): Unit = {
        val execution = new SimpleExecution
        val exec = new Executor(execution)
        exec.execute("http://www.synyx.de/")
    }

}

