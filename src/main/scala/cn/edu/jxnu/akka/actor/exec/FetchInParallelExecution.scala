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

    //测试时不要用页面太多的网站，或者需要登录。目前没有考虑内存qaq
    override def main(args: Array[String]) {
        val execution = new FetchInParallelExecution()
        val exec = new Executor(execution)
        exec.execute("https://www.baidu.com/s?wd=%E6%B5%8B%E8%AF%95&rsv_spt=1&rsv_iqid=0xaa8f265a0002da5a&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=0&rsv_sug3=2&rsv_sug1=1&rsv_sug7=100&inputT=2178&rsv_sug4=3864")
    }
}
