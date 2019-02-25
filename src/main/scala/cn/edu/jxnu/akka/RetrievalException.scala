package cn.edu.jxnu.akka

/**
 * 异常
 */
class RetrievalException(val message: String) extends RuntimeException(message) {

    var code: Int = RetrievalException.ETRIEVAL_CODE_ERROR

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }

}

object RetrievalException {
    final val ETRIEVAL_CODE_ERROR: Int = 2000
}