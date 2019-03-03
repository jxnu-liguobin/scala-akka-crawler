package cn.edu.jxnu.akka

import java.util
import java.util.concurrent.LinkedBlockingDeque

/**
 * 暂不考虑内存
 * 缓存图片链接
 */
object ImageUrlStore {

    //全部链接
    private val imagesUrlQueue = new LinkedBlockingDeque[String](128)

    //准备下载的链接
    private val imagesUrlToDownloadQueue = new LinkedBlockingDeque[String](128)

    //无效的图片链接，包括下载失败
    private val imageInvalidList: util.List[String] = new util.ArrayList[String]()

    //重复的图片链接
    private val imageDuplicateList: util.List[String] = new util.ArrayList[String]()


    def getImageQueue() = imagesUrlQueue

    def getImagesUrlToDownloadQueue() = imagesUrlToDownloadQueue

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
     * 从右进队
     *
     * @param url
     */
    def rightPush(url: String) = {

        if (getImageQueue.contains(url)) {
            addDuplicateImage(url)
        } else {
            getImagesUrlToDownloadQueue().offerLast(url)
            getImageQueue().offerLast(url)
        }
    }

    def rightPushAll(urls: String*) = {
        for (url <- urls) {
            rightPush(url)
        }
    }


    /**
     * 开始下载图片从左出队
     *
     * @return
     */
    def leftPoll(): String = {
        if (getImagesUrlToDownloadQueue().isEmpty) {
            null
        }
        else {
            getImagesUrlToDownloadQueue.pollFirst()
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
        if (getImagesUrlToDownloadQueue().isEmpty) {
            return null
        }
        breakable {
            for (i <- 1 to n) {
                if (getImagesUrlToDownloadQueue.isEmpty) {
                    break()
                }
                imgs.add(getImagesUrlToDownloadQueue.pollFirst())
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
        while (!getImagesUrlToDownloadQueue.isEmpty) {
            list.add(getImagesUrlToDownloadQueue.pollFirst())
        }
        list
    }

}
