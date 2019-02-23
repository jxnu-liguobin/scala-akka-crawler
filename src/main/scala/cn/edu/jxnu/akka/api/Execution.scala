package cn.edu.jxnu.akka.api

import org.apache.lucene.index.IndexWriter

/**
 * 顶级执行抽象类
 */
abstract class Execution {

    /**
     * 定义通用的下载并索引的接口
     *
     * @param path
     * @param writer
     */
    def downloadAndIndex(path: String, writer: IndexWriter)

}