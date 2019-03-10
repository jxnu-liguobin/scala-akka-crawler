package cn.edu.jxnu.akka.common.util

import java.io._
import java.net.{HttpURLConnection, URL}
import java.util.Date
import java.util.regex.Pattern

import cn.edu.jxnu.akka.common.{Constant, ExceptionConstant}
import cn.edu.jxnu.akka.exception.DownloadException
import cn.edu.jxnu.akka.store.ImageUrlStore
import com.alibaba.simpleimage.render._
import com.alibaba.simpleimage.{ImageFormat, SimpleImageException}
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions
//必须要
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 图片批量下载
 */
object DownloadUtil {

    private val logger = LoggerFactory.getLogger(DownloadUtil.getClass)

    /**
     * 图片裁剪
     *
     * 裁剪成功返回true
     *
     * 本方法依赖的api太老旧，会出现各种问题，有的jar还需要手动引入
     *
     * @param source  原图
     * @param target  裁剪后
     * @param imgType 图片类型
     */
    def tailoringIamge(source: String, target: String, imgType: ImageFormat): Boolean = {
        var inStream: FileInputStream = null
        var outStream: FileOutputStream = null
        var wr: WriteRender = null
        //是否需要裁剪
        try {
            //得到原图
            val img = new File(source)
            //经过裁剪处理的图片
            val targetImg = new File(target)
            inStream = new FileInputStream(img)
            outStream = new FileOutputStream(targetImg)
            val rr = new ReadRender(inStream)
            val scaleParam = new ScaleParameter(Constant.img_width, Constant.img_height)
            val sr = new ScaleRender(rr, scaleParam)
            wr = new WriteRender(sr, outStream, imgType)
            wr.render() //触发图像处理
            //千万别用ImageWriteHelper处理，坑爹
            //这里删除原图会失败
            true
        } catch {
            case ex: Exception => {
                logger.error(ex.getMessage)
                false
            }
        }
        finally {
            if (inStream != null) {
                inStream.close()
            }

            if (outStream != null) {
                outStream.close()
            }

            if (wr != null) {
                try {
                    wr.dispose() //释放simpleImage的内部资源
                } catch {
                    case e: SimpleImageException => {
                        logger.error(e.getMessage)
                    }
                }
            }
        }
    }

    /**
     * 图片下载
     *
     * @param images    图片下载链接列表
     * @param tailoring 是否裁剪图片
     * @param deleted   是否删除原图
     * @return
     */
    def downloadFuture(images: java.util.List[String], tailoring: Boolean, deleted: Boolean): Future[Boolean] = {

        val downloadImageBatch: Future[Boolean] = {
            Future {
                var folder: String = null
                for (img <- JavaConversions.asScalaIterator(images.iterator())) {

                    val imgType = verifyGet(img)
                    val times = String.valueOf(System.currentTimeMillis()) + "." + imgType
                    folder = DateUtil.formatDate(new Date())
                    val fileTargetName = getImgPathName(Constant.img_target, folder + Constant.img_original, times)
                    downloadFile(img, fileTargetName)
                    if (tailoring) {
                        //需要裁剪
                        val tailoringResult = tailoringIamge(fileTargetName, getImgPathName(Constant.img_target, folder + Constant.img_modify, times),
                            ImageFormat.getImageFormat(imgType))
                        if (tailoringResult) {
                            logger.info("Successful image clipping")
                        } else {
                            logger.info("Picture clipping failed")
                        }
                    }
                }
                //需要删除原图片
                if (deleted) {
                    logger.info("Successful image clipping and delete the original image")
                    FileUtils.deleteQuietly(new File(Constant.img_target + folder + Constant.img_original))
                } else {
                    logger.info("No need to delete")
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
                logger.info(ex.getMessage)
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

    /**
     * 验证图片后缀，无法验证返回jpg
     *
     * @param imageUrl
     * @return
     */
    def verifyGet(imageUrl: String): String = {
        val suffixes = Constant.suffixes
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
