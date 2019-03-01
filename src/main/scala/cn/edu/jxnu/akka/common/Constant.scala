package cn.edu.jxnu.akka.common

/**
 * 通用公共常量
 */
object Constant {

    final val message_commit = "完成索引"
    final val message_committed = "完成提交"
    final val message_search_all = "查询所有"

    //actor池大小
    final val round_robin_pool_size = 100

    //最大并发
    final val count_latch_size = 50

    //TODO 可配置，待改
    final val index_path = "D:/git_project/scala-akka-crawler"

}
