package cn.edu.jxnu.akka.http.proxy

import java.net.InetSocketAddress
import java.util.concurrent.{Delayed, TimeUnit}

import org.apache.http.HttpHost

import scala.beans.BeanProperty

/**
 * 代理信息
 */
@SerialVersionUID(1L)
case class Proxy() extends Delayed {

    @BeanProperty
    var timeInterval: Long = _
    @BeanProperty
    var ip: String = _
    @BeanProperty
    var port: Int = _
    @BeanProperty
    @BeanProperty
    var id: String = _
    @BeanProperty
    var `type`: String = _ //http、https
    @BeanProperty
    var availableFlag: Boolean = _
    @BeanProperty
    var anonymousFlag: Boolean = _
    @BeanProperty
    var lastSuccessfulTime: Long = _ //最近一次请求成功时间
    @BeanProperty
    var successfulTotalTime: Long = _ //请求成功总耗时
    @BeanProperty
    var failureTimes: Int = _ //请求失败次数
    @BeanProperty
    var successfulTimes: Int = _ //请求成功次数
    @BeanProperty
    var successfulAverageTime: Double = _ //成功请求平均耗时


    def this(ip: String, port: Int, timeInterval: Long) {
        this()
        this.ip = ip
        this.port = port
        this.timeInterval = timeInterval
        this.timeInterval = TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime
    }

    def this(ip: String, port: Int, `type`: String, timeInterval: Long) {
        this(ip, port, timeInterval)
        this.`type` = `type`
        this.timeInterval = TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime
    }

    def getProxyStr: String = `type` + "://" + ip + ":" + port

    /**
     * 将Proxy转换成一个HttpHost对象
     */
    def toHttpHost = new HttpHost(ip, port, `type`)


    /**
     * 转化为Java代理对象
     */
    def toJavaNetProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ip, port))

    override def getDelay(unit: TimeUnit): Long = unit.convert(timeInterval - System.nanoTime, TimeUnit.NANOSECONDS)

    override def compareTo(o: Delayed): Int = {
        val element = o.asInstanceOf[Proxy]

        if ((successfulAverageTime == 0.0d) || (element.successfulAverageTime == 0.0d)) {
            return 0
        }
        return {
            if (successfulAverageTime > element.successfulAverageTime) 1
            else if (successfulAverageTime < element.successfulAverageTime) -1
            else 0
        }
    }

    def canEqual(other: Any): Boolean = other.isInstanceOf[Proxy]


    override def equals(other: Any): Boolean = other match {
        case that: Proxy =>
            (that canEqual this) &&
              timeInterval == that.timeInterval &&
              ip == that.ip &&
              port == that.port &&
              id == that.id &&
              `type` == that.`type` &&
              availableFlag == that.availableFlag &&
              anonymousFlag == that.anonymousFlag &&
              lastSuccessfulTime == that.lastSuccessfulTime &&
              successfulTotalTime == that.successfulTotalTime &&
              failureTimes == that.failureTimes &&
              successfulTimes == that.successfulTimes &&
              successfulAverageTime == that.successfulAverageTime
        case _ => false
    }

    override def hashCode(): Int = {
        val state = Seq(timeInterval, ip, port, id, `type`, availableFlag, anonymousFlag, lastSuccessfulTime, successfulTotalTime, failureTimes, successfulTimes, successfulAverageTime)
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
    }

}
