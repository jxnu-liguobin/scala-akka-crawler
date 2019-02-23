package cn.edu.jxnu.akka

/**
 * 异常
 */
class IndexingException(val message: String) extends RuntimeException(message) {

    final val INDEX_CODE_ERROR: Int = 1000

    var code: Int = INDEX_CODE_ERROR

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }

}
