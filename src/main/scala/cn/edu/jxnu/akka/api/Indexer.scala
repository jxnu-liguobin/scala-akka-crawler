package cn.edu.jxnu.akka.api

import cn.edu.jxnu.akka.PageContent

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
}
