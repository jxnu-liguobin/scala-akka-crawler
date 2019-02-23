package cn.edu.jxnu.akka.api.impl

import java.io.{File, IOException}

import cn.edu.jxnu.akka.RetrievalException
import cn.edu.jxnu.akka.api.Execution
import org.apache.lucene.document.Document
import org.apache.lucene.index._
import org.apache.lucene.search.{IndexSearcher, MatchAllDocsQuery, TopDocs}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.perf4j.{LoggingStopWatch, StopWatch}
import org.slf4j.LoggerFactory
import org.wltea.analyzer.lucene.IKAnalyzer

/**
 * 真正的执行者实体，通过传入指定接口规范的执行器实体，可以以统一的方式执行下载、索引、搜索
 *
 */
class Executor(execution: Execution) {

    final val INDEX_PATH = "D:/git_project/akka-crawler-example/index"

    //传入继承了Execution的类

    private val logger = LoggerFactory.getLogger(classOf[Executor])

    //Lucene索引
    var writer: IndexWriter = _
    var searcher: IndexSearcher = _

    /**
     * 执行调用
     *
     * @param path
     */
    def execute(path: String) = {
        //Lucene索引
        try {
            val indexDir = new File(INDEX_PATH, "index-" + System.currentTimeMillis())
            writer = openWriter(indexDir)

            /**
             * 秒表类用于对Perf4j中的代码块计时。一般的使用模式是创建一个秒表
             * 在要计时的代码段之前，然后在将其传递给日志记录方法之前停止它
             */
            val stopWatch: StopWatch = new LoggingStopWatch()
            //使用传进来的执行器，回调它的下载与索引方法
            execution.downloadAndIndex(path, writer)
            //首次执行不执行commit会报错
            writer.commit()
            stopWatch.stop(execution.getClass().getSimpleName())
            //获取搜索器
            searcher = openSearcher(indexDir)
            //匹配所有文档的查询。前100条数据
            //            val  multiFieldQuery = MultiFieldQueryParser.parse(keyWord, fields, clauses, analyzer)
            val result: TopDocs = searcher.search(new MatchAllDocsQuery(), 100)
            //查询的命中总数。
            logger.info("Found {} results", result.totalHits)
            //遍历命中文件的编号，通过搜索器查询到原文档，并输出id
            for (scoreDoc <- result.scoreDocs) {
                val doc: Document = searcher.doc(scoreDoc.doc)
                logger.debug(doc.get("id"))
            }

        } catch {
            case ex: IOException =>
                logger.error(ex.getMessage(), ex)
                if (writer != null) {
                    try {
                        writer.rollback()
                    } catch {
                        case ex1: IOException =>
                            logger.error(ex1.getMessage(), ex1)

                    }
                }
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch {
                    case ex: CorruptIndexException => {
                        logger.error(ex.getMessage(), ex)
                        throw new RetrievalException(ex.getMessage)
                    }
                    case ex: IOException => logger.error(ex.getMessage(), ex)
                }
            }
        }

    }

    /**
     * 获取索引写入器
     *
     * @param indexDir
     * @return
     */
    private def openWriter(indexDir: File): IndexWriter = {
        val dir = FSDirectory.open(indexDir)
        val config = new IndexWriterConfig(Version.LUCENE_47, new IKAnalyzer(true))
        //        config.setOpenMode(OpenMode.CREATE_OR_APPEND)
        new IndexWriter(dir, config)
    }

    /**
     * 获取索引搜索器
     *
     * @param indexDir
     * @return
     */
    private def openSearcher(indexDir: File): IndexSearcher = {
        val dir = FSDirectory.open(indexDir)
        val directoryReader = DirectoryReader.open(dir)
        new IndexSearcher(directoryReader)
    }
}
