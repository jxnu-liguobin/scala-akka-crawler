package cn.edu.jxnu.akka.common

/**
 * 通用公共常量
 */
object Constant {

    final val message_commit = "完成索引"
    final val message_committed = "完成提交"
    final val message_search_all = "查询所有"

    final val message_download_image = "图片下载"
    final val message_downloaded_image = "确认图片下载"

    //actor池大小
    final val round_robin_pool_size = 100

    //最大并发
    final val count_latch_size = 50

    final val depth_dom = 150

    //默认超时，毫秒
    //测试需要频繁修改这个参数，可能有缓存导致改了没生效，强制刷新内存
    final val timeout = 10000

    //TODO 可配置，待改
    final val index_path = "D:/git_project/scala-akka-crawler/"

}
