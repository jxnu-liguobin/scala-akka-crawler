package cn.edu.jxnu.akka.exception

import cn.edu.jxnu.akka.common.ExceptionConstant

/**
 * 链接异常
 */
class ConnectionException(val message: String) extends RuntimeException(message) {

    var code: Int = ExceptionConstant.CONNECTION_CODE

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }
}
