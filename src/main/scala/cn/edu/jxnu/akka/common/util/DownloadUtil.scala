package cn.edu.jxnu.akka.common.util

import java.io._
import java.net.{HttpURLConnection, URL}
import java.util.Date
import java.util.regex.Pattern

import cn.edu.jxnu.akka.{DownloadException, ExceptionConstant, ImageUrlStore}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions
//必须要
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 图片批量下载
 */
object DownloadUtil {

    val target = "D:/git_project/scala-akka-crawler/"
    private val logger = LoggerFactory.getLogger(DownloadUtil.getClass)

    def downloadFuture(images: java.util.List[String]): Future[Boolean] = {

        val downloadImageBatch: Future[Boolean] = {
            Future {
                for (img <- JavaConversions.asScalaIterator(images.iterator())) {

                    val imgType = verifyGet(img)
                    val times = String.valueOf(System.currentTimeMillis()) + "." + imgType
                    val folder = DateUtil.formatDate(new Date())
                    val fileTargetName = getImgPathName(target, folder, times)
                    downloadFile(img, fileTargetName)
                }
                true
            }
        }

        /**
         * 成功回调
         */
        downloadImageBatch onSuccess {
            case true => {
                logger.info("Download Success ")
            }
        }

        /**
         * 失败回调
         */
        downloadImageBatch onFailure {
            case ex: Exception => {
                logger.error("An error has occured and message is {}", ex.getMessage)
                //继续抛出
                throw new DownloadException(ExceptionConstant.DOWNLOAD_CODE_IMAGE, ExceptionConstant.DOWNLOAD_MESSAGE_IMAGE)
            }
        }
        try downloadImageBatch
    }

    /**
     *
     * @param target     目标文件夹
     * @param currentDay 当前日期
     * @param fileName   重命名的图片名称，已经带后缀，没有则用默认
     * @return
     */
    def getImgPathName(target: String, currentDay: String, fileName: String): String = {

        val targetPath = target.replace("\\\\", "/")

        var realPath: String = null
        if (targetPath.endsWith("/")) {
            realPath = targetPath + currentDay
        } else {
            realPath = targetPath + "/" + currentDay
        }

        val testFile = new java.io.File(realPath)
        if (!testFile.exists()) {
            testFile.mkdir()
        }
        //先写死

        if (realPath.endsWith("/")) {

            realPath + fileName

        } else {
            realPath + "/" + fileName
        }
    }

    /**
     *
     * @param fileUrl 下载链接
     * @param target  保存地址且重命名后的文件
     */
    def downloadFile(fileUrl: String, target: String) {
        var in: InputStream = null
        var connection: HttpURLConnection = null
        var out: OutputStream = null
        try {

            val url = new URL(fileUrl)
            connection = url.openConnection().asInstanceOf[HttpURLConnection]
            connection.setRequestMethod("GET")
            in = connection.getInputStream
            val fileToDownloadAs = new java.io.File(target)
            out = new BufferedOutputStream(new FileOutputStream(fileToDownloadAs))
            val byteArray = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
            out.write(byteArray)
        } catch {
            case ex: Exception => {
                ImageUrlStore.getImageInvalidList().add(fileUrl)
                throw new DownloadException(ExceptionConstant.DOWNLOAD_CODE_IMAGE, ExceptionConstant.DOWNLOAD_MESSAGE_IMAGE)
            }
        }
        finally {

            if (in != null) {
                in.close()
            }

            if (out != null) {
                out.close()
            }
        }
    }

    def verifyGet(imageUrl: String): String = {
        val suffixes = "jpeg|gif|jpg|png|bmp"
        val pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")")
        //正则判断
        val mc = pat.matcher(imageUrl) //条件匹配
        while (mc.find()) {
            val substring = mc.group() //截取文件名后缀名
            val fileType = substring.split("\\.")
            if (fileType != null && fileType.size > 1) {
                return fileType(1)
            }
        }
        //返回默认是后缀
        "jpg"
    }

}
