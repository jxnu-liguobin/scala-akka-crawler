package cn.edu.jxnu.akka.api.impl

import java.io.IOException

import cn.edu.jxnu.akka.PageContent
import cn.edu.jxnu.akka.api.Indexer
import org.apache.lucene.document.{Document, Field}
import org.apache.lucene.index.{CorruptIndexException, IndexWriter}
import org.slf4j.LoggerFactory

/**
 * 索引器实现
 *
 */
class IndexerImpl(indexWriter: IndexWriter) extends Indexer {

    private val logger = LoggerFactory.getLogger(classOf[IndexerImpl])

    override def index(pageContent: PageContent) = {
        try {
            indexWriter.addDocument(toDocument(pageContent))
        } catch {
            case ex: CorruptIndexException => throw new IllegalStateException(ex)
            case ex: IOException => throw new IllegalStateException(ex)
        }
    }

    //将页面转化为文档
    private def toDocument(content: PageContent): Document = {
        val doc = new Document()
        //索引三个字段
        try {
            doc.add(new Field("id", content.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED))
            doc.add(new Field("title", content.getTitle(), Field.Store.YES, Field.Index.ANALYZED))
            //不存
            doc.add(new Field("content", content.getContent(), Field.Store.NO, Field.Index.ANALYZED))
        }catch {
            case  ex:Exception=> {
                logger.error(ex.getMessage)
                throw new IllegalStateException(ex)
            }
        }
        doc
    }

    override def commit() {
        try {
            indexWriter.commit()
        } catch {
            case ex: CorruptIndexException => throw new IllegalStateException(ex)
            case ex: IOException => throw new IllegalStateException(ex)
        }
    }

    override def close() {
        try {
            indexWriter.close()
        } catch {
            case ex: CorruptIndexException => throw new IllegalStateException(ex)
            case ex: IOException => throw new IllegalStateException(ex)
        }
    }

}
