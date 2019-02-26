package cn.edu.jxnu.akka

/**
 * 异常常量
 */
object ExceptionConstant {

    final val INDEX_CODE: Int = 1000
    final val INDEX_MESSAGE = "索引错误"


    final val INDEX_CODE_IO: Int = 1001
    final val INDEX_MESSAGE_IO = "索引IO错误"


    final val INDEX_CODE_SEARCH: Int = 1002
    final val INDEX_MESSAGE_SEARCH = "索引查询错误"


    final val ETRIEVAL_CODE: Int = 2000
    final val ETRIEVAL_MESSAGE = "爬页面错误"


    final val ETRIEVAL_CODE_MONKEY: Int = 2001
    final val ETRIEVAL_MESSAGE_MONKEY = "Something went horribly wrong when fetching the page."

}
