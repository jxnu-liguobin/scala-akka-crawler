package cn.edu.jxnu.akka.common.util

import java.util.regex.Pattern

/**
 * 验证URL链接
 */
object ValidationUrl {


    /**
     * 验证是否页面url合法性
     *
     * @param url
     * @return
     */
    def vaildUrl(url: String, regex: String): Boolean = {
        val pattern = Pattern.compile(regex)
        if (pattern.matcher(url).find()) {
            true
        } else {
            false
        }
    }

    def imgUrl(url: String): Boolean = {
        val imgRegex = "(http|https):\\/\\/([\\w.]+\\/?)\\S*"
        vaildUrl(url, imgRegex)
    }

    def contentUrl(url: String): Boolean = {
        val contentRegex = "^(?:https?://)?[\\w]{1,}(?:\\.?[\\w]{1,})+[\\w-_/?&=#%:]*$"
        vaildUrl(url, contentRegex)
    }
}
