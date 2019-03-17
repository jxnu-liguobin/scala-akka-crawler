package cn.edu.jxnu.akka.exception

import cn.edu.jxnu.akka.common.ExceptionConstant

/**
 * 爬取异常
 */
class RetrievalException(val message: String) extends RuntimeException(message) {

    var code: Int = ExceptionConstant.ETRIEVAL_CODE

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }

    var url: String = _

    def this(code: Int, message: String, url: String) = {
        this(message)
        this.code = code
        this.url = url
    }

    def this(message: String, url: String) = {
        this(message)
        this.url = url
    }
}