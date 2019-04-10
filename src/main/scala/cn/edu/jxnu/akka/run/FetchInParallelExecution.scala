package cn.edu.jxnu.akka.run

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorSystem, Props}
import cn.edu.jxnu.akka.actor.CrawlerActor
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
        val master = actorSystem.actorOf(Props.create(classOf[CrawlerActor], new HtmlParserPageRetriever(path),
            writer, countDownLatch))
        master ! (path)
        try {
            //计数，控制主进程不能提取结束
            //这样会出现一个问题。如果允许的actor比这个值还小，会导致程序一直阻塞不结束
            countDownLatch.await()
            actorSystem.terminate(); //旧版本是shutdown()
        } catch {
            case ex: InterruptedException => throw new IllegalStateException(ex)
        }
    }

}

/**
 * 使用爬虫，爬虫通过定时器获取，无法再使用第二个main
 */
object FetchInParallelExecution extends App {

    //测试时不要用页面太多的网站，或者需要登录。目前没有考虑内存qaq
    override def main(args: Array[String]) {
        val execution = new FetchInParallelExecution()
        val exec = new Executor(execution)
        exec.execute("https://blog.csdn.net/qq_34446485")
    }
}
