package cn.edu.jxnu.akka.util

import com.gio.bloomfilter.ScalaBloomFilter

/**
 * 测试布隆过滤器jar包
 *
 * @author 梦境迷离
 * @version 1.0, 2019-04-10
 */
object TestBloomFilter extends App {


    def test(): Unit = {
        val elementCount: Int = 50000 // 测试元素个数
        val falsePositiveProbability: Double = 0.00001 //假阳性概率
        val bf: ScalaBloomFilter[String] = new ScalaBloomFilter[String](falsePositiveProbability, elementCount)
        bf.add("test")
        if (bf.contains("test")) {
            println("存在元素: test")
            println("根据公式计算假阳性的预期概率: " + bf.expectedFalsePositiveProbability)
        }
        if (bf.contains("test1")) {
            println("There was a test1.")
        }
    }

    //因为本项目是2.11.8所以使用JavaConversions.asScalaIterator
    test

}
