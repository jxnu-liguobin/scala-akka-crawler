package cn.edu.jxnu.akka.api.impl

import java.io.{File, IOException}
import java.util.Date

import cn.edu.jxnu.akka.api.Execution
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.exception.{IndexingException, RetrievalException}
import cn.edu.jxnu.akka.util.DateUtil
import org.apache.lucene.index._
import org.apache.lucene.search.IndexSearcher
import org.perf4j.{LoggingStopWatch, StopWatch}
import org.slf4j.LoggerFactory

/**
 * 真正的执行者实体，通过传入指定接口规范的执行器实体，可以以统一的方式执行下载、索引、搜索
 *
 */
class Executor(execution: Execution) {

    //传入继承了Execution的类
    private val logger = LoggerFactory.getLogger(classOf[Executor])

    //Lucene索引
    var writer: IndexWriter = _
    var searcher: IndexSearcher = _

    def execute(path: String) = {

        //Lucene索引
        try {
            val folder = DateUtil.formatDate(new Date())
            Constant.index_path + folder
            Executor.indexDir = new File(Constant.index_path + folder, "index-" + System.currentTimeMillis())
            writer = IndexerImpl.openWriter(Executor.indexDir)

            /**
             * 秒表类用于对Perf4j中的代码块计时。一般的使用模式是创建一个秒表
             * 在要计时的代码段之前，然后在将其传递给日志记录方法之前停止它
             */
            val stopWatch: StopWatch = new LoggingStopWatch()
            //使用传进来的执行器，回调它的下载与索引方法
            execution.downloadAndIndex(path, writer)
            //首次执行不执行commit会报错
//            writer.commit()
            stopWatch.stop(execution.getClass().getSimpleName())
        } catch {
            case ie: IndexingException => {
                logger.error(ie.getMessage())
                if (writer != null) {
                    try {
                        writer.rollback()
                    } catch {
                        case ex1: IOException =>
                            logger.error(ex1.getMessage(), ex1)
                    }
                }
            }
            case re: RetrievalException => {
                logger.error(re.getMessage())
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch {
                    case ex: CorruptIndexException => {
                        logger.error(ex.getMessage())
                    }
                    case ex: IOException => logger.error(ex.getMessage)
                }
            }
        }

    }

}

object Executor {

    var indexDir: File = _
}
