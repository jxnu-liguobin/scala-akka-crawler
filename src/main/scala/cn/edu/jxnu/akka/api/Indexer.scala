package cn.edu.jxnu.akka.api

import java.io.File

import cn.edu.jxnu.akka.entity.PageContent

/**
 * 索引器
 *
 */
abstract class Indexer {

    //提交
    def commit()

    //索引
    def index(pageContent: PageContent)

    //关闭
    def close()

    //查询
    def searchAll(file: File)
}
