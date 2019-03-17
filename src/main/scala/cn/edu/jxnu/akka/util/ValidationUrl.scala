package cn.edu.jxnu.akka.util

import java.util.regex.Pattern

/**
 * 验证URL链接
 */
object ValidationUrl {


    /**
     * 验证子链是否是爬取页面的子站
     *
     * @param basicUrl
     * @param url
     * @return
     */
    def startWithBasicUrl(basicUrl: String, url: String): Boolean = {
        url.startsWith(basicUrl)
    }

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

    def noWWWUrl(url: String): Boolean = {
        val contentRegex = "(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?"
        vaildUrl(url, contentRegex)
    }

    def main(args: Array[String]): Unit = {
        println(noWWWUrl("http://electronics.cnet.com/electronics/0-6342366-8-8994967-1.html"))
        println(noWWWUrl("www.yahoo.com"))
        val url = "https://blog.csdn.net/qq_34446485?orderby=ViewCount"
        println(startWithBasicUrl("https://blog.csdn.net/qq_34446485", "https://blog.csdn.net/qq_34446485?orderby=ViewCount"))
    }

}
