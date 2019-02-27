package cn.edu.jxnu.akka.common.util

/**
 * @author 梦境迷离
 * @time 2019-02-27
 */
object ValidationUrl {


    /**
     * 验证是否页面url合法性
     *
     * @param link
     * @return
     */
    def vaildUrl(link: String): Boolean = {
        link.startsWith("http://") || link.startsWith("https://") || link.endsWith("/")
    }

    /**
     * 验证图片合法
     *
     * @param url
     * @return
     */
    def isUrlUnique(url: String): Boolean = true

}
