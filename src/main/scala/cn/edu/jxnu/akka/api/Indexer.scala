package cn.edu.jxnu.akka.api

import java.io.File

import cn.edu.jxnu.akka.entity.PageContent

/**
 * 索引器接口
 *
 */
abstract class Indexer {

    /**
     * 提交
     */
    def commit()

    /**
     * 索引
     *
     * @param pageContent
     */
    def index(pageContent: PageContent)

    /**
     * 关闭
     */
    def close()

    /**
     * 查询
     *
     * @param file
     */
    def searchAll(file: File)
}
