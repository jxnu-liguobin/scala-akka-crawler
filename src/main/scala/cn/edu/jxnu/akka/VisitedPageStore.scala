package cn.edu.jxnu.akka

import java.util.List

import scala.collection.JavaConversions._
import scala.collection.mutable._

/**
 * 存储需要访问的页面
 */
object VisitedPageStore {

    //TODO 内存，需要使用队列
    //准备去访问的
    private val pagesToVisit: Set[String] = new HashSet[String]()
    //所有页面
    private val allPages: Set[String] = new HashSet[String]()
    //处理中的页面
    private val inProgress: Set[String] = new HashSet[String]()

    /**
     * 添加页面
     *
     * @param page
     * @return
     */
    def add(page: String) = {
        if (!allPages.contains(page)) {
            pagesToVisit.add(page)
            allPages.add(page)
        }
    }

    /**
     * 存储所有页面
     *
     * @param pages
     */
    def addAll(pages: List[String]) = {
        for (page <- pages) {
            add(page)
        }
    }

    /**
     * 记录已经完成的页面
     *
     * @param page
     * @return
     */
    def finished(page: String) = {
        inProgress.remove(page)
    }

    /**
     * 取得下次可访问的页面
     *
     * @return
     */
    def getNext() = {
        if (pagesToVisit.isEmpty()) {
            null
        } else {
            val next: String = pagesToVisit.iterator().next()
            pagesToVisit.remove(next)
            inProgress.add(next)
            next
        }
    }

    /**
     * 批量取得
     *
     * @return
     */
    def getNextBatch(): Iterable[String] = {
        val pages: Set[String] = new HashSet[String]()
        pages.addAll(pagesToVisit)
        pagesToVisit.clear()
        inProgress.addAll(pages)
        pages
    }

    /**
     * 检验是否已经无可访问页面
     *
     * @return
     */
    def isFinished(): Boolean = pagesToVisit.isEmpty() && inProgress.isEmpty()

}
