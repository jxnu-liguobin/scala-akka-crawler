package cn.edu.jxnu.akka.common.util

import java.text.SimpleDateFormat
import java.util.Date

/**
 * 时间
 */
object DateUtil {

    private val monthDayFormat = new SimpleDateFormat("yyyy-MM-dd")

    def formatDate(date: Date): String = {
        monthDayFormat.format(date)
    }
}
