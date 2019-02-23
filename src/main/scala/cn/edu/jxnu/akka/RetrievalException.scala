package cn.edu.jxnu.akka

/**
 * 异常
 */
class RetrievalException(val message: String) extends RuntimeException(message) {

    final val ETRIEVAL_CODE_ERROR: Int = 2000

    var code: Int = ETRIEVAL_CODE_ERROR

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }

}