package cn.edu.jxnu.akka.exception

import cn.edu.jxnu.akka.common.ExceptionConstant

/**
 * 下载异常
 */
class DownloadException(val message: String) extends RuntimeException(message) {

    var code: Int = ExceptionConstant.DOWNLOAD_CODE_IMAGE

    def this(code: Int, message: String) = {
        this(message)
        this.code = code
    }


}
