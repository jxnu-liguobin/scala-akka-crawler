package cn.edu.jxnu.akka

/**
 * 异常
 */
class IndexingException(val message: String) extends RuntimeException(message) {

    var code: Int = IndexingException.INDEX_CODE_ERROR

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }

}

object IndexingException {
    final val INDEX_CODE_ERROR: Int = 1000
    final val INDEX_CODE_SEARCH_ERROR: Int = 1001
}
