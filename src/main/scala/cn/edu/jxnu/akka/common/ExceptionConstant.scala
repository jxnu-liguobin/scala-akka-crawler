package cn.edu.jxnu.akka.common

/**
 * 异常常量
 */
object ExceptionConstant {

    final val INDEX_CODE = 1000
    final val INDEX_MESSAGE = "索引错误"


    final val INDEX_CODE_IO = 1001
    final val INDEX_MESSAGE_IO = "索引IO错误"


    final val INDEX_CODE_SEARCH = 1002
    final val INDEX_MESSAGE_SEARCH = "索引查询错误"


    final val ETRIEVAL_CODE = 2000
    final val ETRIEVAL_CODE_MONKEY = 2001
    final val ETRIEVAL_MESSAGE = "爬页面错误"
    final val ETRIEVAL_MESSAGE_TIMEOUT = "爬页面超时"
    final val ETRIEVAL_MESSAGE_JSOUP = "Jsoup爬页面错误"
    final val ETRIEVAL_MESSAGE_JSOUP_CONNECT = "Jsoup爬页面错误-链接参数无效"
    final val ETRIEVAL_MESSAGE_MONKEY = "Something went horribly wrong when fetching the page."
    final val ETRIEVAL_MESSAGE_URL = "url验证失败"
    final val ETRIEVAL_MESSAGE_OTHER = "其他失败"

    final val PROXY_MESSAGE_NNP = "暂时未找到可用代理"
    final val PROXY_MESSAGE_TIMEOUT = "暂时未找到可用代理"
    final val PROXY_CODE_NNP = 5000


    final val DOWNLOAD_CODE_IMAGE = 3000
    final val DOWNLOAD_MESSAGE_IMAGE = "下载图片时出错"


    final val CONNECTION_CODE = 4000
    final val CONNECTION_MESSAGE: String = "请求url无效"
    final val CONNECTION_MESSAGE_NNP: String = "请求参数为空"


}
