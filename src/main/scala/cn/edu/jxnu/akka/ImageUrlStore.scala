package cn.edu.jxnu.akka

import java.util
import java.util.concurrent.LinkedBlockingDeque

/**
 * 暂不考虑内存
 * 缓存图片链接
 */
object ImageUrlStore {

    //准备下载的图片链接
    private val imagesUrlQueue = new LinkedBlockingDeque[String](128)

    //无效的图片链接，包括下载失败
    private val imageInvalidList: util.List[String] = new util.ArrayList[String]()

    //重复的图片链接
    private val imageDuplicateList: util.List[String] = new util.ArrayList[String]()


    def getImageQueue() = imagesUrlQueue

    def getImageInvalidList() = imageInvalidList

    def getImageDuplicateList() = imageDuplicateList


    def addInvalidImage(img: String) = {
        if (!imageInvalidList.contains(img)) {
            imageInvalidList.add(img)
        }
    }

    def addDuplicateImage(img: String) = {
        if (!imageDuplicateList.contains(img)) {
            imageDuplicateList.add(img)
        }
    }

    /**
     * 待下载图片从右进队
     *
     * @param url
     */
    def rightPush(url: String): Boolean = {

        if (getImageQueue.contains(url)) {
            addDuplicateImage(url)
            return false
        }
        getImageQueue.offerLast(url)
    }

    def rightPushs(urls: String*): Boolean = {

        for (url <- urls) {
            if (!getImageQueue.contains(url)) {
                getImageQueue.offerLast(url)
            } else {
                addDuplicateImage(url)
            }
        }
        true
    }


    /**
     * 开始下载图片从左出队
     *
     * @return
     */
    def leftPoll(): String = {
        if (getImageQueue().isEmpty) {
            null
        }
        else {
            getImageQueue.pollFirst()
        }
    }

    /**
     * 取出前几个
     *
     * @param n
     * @return
     */
    def leftNPoll(n: Int): util.List[String] = {
        import scala.util.control.Breaks._

        val imgs = new util.ArrayList[String]()
        if (getImageQueue().isEmpty) {
            return null
        }
        breakable {

            for (i <- 1 to n) {

                if (getImageQueue.isEmpty) {
                    break()
                }

                imgs.add(getImageQueue.pollFirst())
            }
        }
        imgs
    }

    /**
     * 从左开始取出所有可下载链接
     *
     * @return
     */
    def leftPollAll(): util.List[String] = {
        val list = new util.ArrayList[String]()
        while (!getImageQueue.isEmpty) {
            list.add(getImageQueue.pollFirst())
        }

        list
    }

}
