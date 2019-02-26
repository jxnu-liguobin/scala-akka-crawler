package cn.edu.jxnu.akka

import java.util

import cn.edu.jxnu.akka.api.impl.IndexerImpl
import cn.edu.jxnu.akka.entity.PageContent
import org.apache.lucene.index._
import org.apache.lucene.search.{IndexSearcher, TermQuery, TopDocs}
import org.apache.lucene.store.{Directory, RAMDirectory}
import org.apache.lucene.util.Version
import org.junit.Assert.assertEquals
import org.junit.Test
import org.wltea.analyzer.lucene.IKAnalyzer


class IndexerImplTest {

    @Test
    def pageContentIsFoundAfterCommit() = {

        val index: Directory = new RAMDirectory()
        val config: IndexWriterConfig = new IndexWriterConfig(Version.LUCENE_47, new IKAnalyzer(true))
        val writer: IndexWriter = new IndexWriter(index, config)
        val indexerImpl: IndexerImpl = new IndexerImpl(writer)
        val content: PageContent = new PageContent("http://path", new util.ArrayList[String](), "This is the title", "This is the content")
        indexerImpl.index(content)
        indexerImpl.commit()

        val directoryReader = DirectoryReader.open(index)
        val searcher: IndexSearcher = new IndexSearcher(directoryReader)
        val query: TermQuery = new TermQuery(new Term("content", "content"))
        val result: TopDocs = searcher.search(query, 10)
        assertEquals(1, result.totalHits)

    }

}
