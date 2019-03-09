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

    /**
     * 单个ip请求间隔，单位ms
     */
    final val time_interval: Long = 1000

    /**
     * 代理socket验证超时
     */
    final val proxy_timeout: Int = 3000

    final val userProxy=false

    final val charset = "UTF-8"

    final val url_test = "https://www.baidu.com/s?wd=%E6%B5%8B%E8%AF%95&rsv_spt=1&rsv_iqid=0xaa8f265a0002da5a&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=0&rsv_sug3=2&rsv_sug1=1&rsv_sug7=100&inputT=2178&rsv_sug4=3864"

}
