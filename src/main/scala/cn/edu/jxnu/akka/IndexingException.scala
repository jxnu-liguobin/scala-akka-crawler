package cn.edu.jxnu.akka

/**
 * 异常
 */
class IndexingException(val message: String) extends RuntimeException(message) {

    var code: Int = ExceptionConstant.INDEX_CODE

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }

}
