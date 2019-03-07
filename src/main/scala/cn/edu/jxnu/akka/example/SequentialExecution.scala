package cn.edu.jxnu.akka.example

import cn.edu.jxnu.akka.VisitedPageStore
import cn.edu.jxnu.akka.api.Execution
import cn.edu.jxnu.akka.api.impl.{Executor, HtmlParserPageRetriever, IndexerImpl}
import org.apache.lucene.index.IndexWriter

/**
 * 顺序执行器，无actor
 * 仅测试用
 */
@deprecated
class SequentialExecution extends Execution {

    override def downloadAndIndex(path: String, writer: IndexWriter) = {

        val pageStore = VisitedPageStore
        //存储待访问页面链接
        pageStore.add(path)
        //创建索引器
        val indexer: IndexerImpl = new IndexerImpl(writer)
        //创建解析器
        val retriever: HtmlParserPageRetriever = new HtmlParserPageRetriever(path)
        var page = pageStore.getNext()
        // 在这里检查正确的值
        while (page != null && page != "") {
            val pageContent = retriever.fetchPageContent(page)
            pageStore.addAll(pageContent.getLinksToFollow())
            indexer.index(pageContent)
            pageStore.finished(page)
            page = pageStore.getNext()
        }
        //完成索引
        indexer.commit()
    }

}

object SequentialExecution extends App {
    override def main(args: Array[String]) {
        val execution = new SequentialExecution()
        val exec = new Executor(execution)
        exec.execute("https://blog.csdn.net/qq_34446485")
    }

}

