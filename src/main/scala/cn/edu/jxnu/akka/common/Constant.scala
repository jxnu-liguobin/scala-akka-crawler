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
    final val round_robin_pool_size = 20

    //main方法等待的最大actor
    final val count_latch_size = 10

    //DOM深度遍历的最大深度
    final val depth_dom = 15

    //下载支持的图片后缀格式
    final val suffixes = "jpeg|gif|jpg|png|bmp"

    //默认超时，毫秒
    //测试需要频繁修改这个参数，可能有缓存导致改了没生效，强制刷新内存
    final val timeout = 10000

    //TODO 可配置，待改
    final val index_path = "D:/git_project/scala-akka-crawler/"

    //图片存放根目录，默认与本项目目录相同目录
    final val img_target = "D:/git_project/scala-akka-crawler/"

    //裁剪后的图片存放的子目录
    final val img_modify = "/image/"

    //图片裁剪之前的原图片
    final val img_original = "/original/"


    //是否删除原图
    final val delete_the_original_image = false

    //是否需要裁剪图片
    final val is_tailoring_img = false

    //图片默认裁剪大小，小于此比例不进行处理
    final val img_width = 100
    final val img_height = 100


    //单个ip请求间隔，单位ms
    final val time_interval: Long = 1000

    //代理socket验证超时
    final val proxy_timeout: Int = 3000

    final val userProxy = false

    final val charset = "UTF-8"

    final val url_test = "http://image.baidu.com/search/index?ct=201326592&tn=baiduimage&word=%E5%9C%A8%E7%BA%BF%E7%BF%BB%E8%AF%91&pn=0&ie=utf-8&oe=utf-8&cl=2&lm=-1&fr=&se=&sme=&hd=1&latest=0&copyright=0"

}
