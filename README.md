## 生产者-消费者 Scala版的Akka例子
  
这个仓库代码只是一个关于如何在Akka中实现生产者-消费者的示例。

> 这是根据Florian Hopf项目完善：

http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html

https://github.com/fhopf/akka-crawler-example

> 此代码库包含3个简单的Web爬虫程序示例，可执行main方法在cn.edu.jxnu.akka.actor.exec中：

* 多actor，执行 FetchInParallelExecution
* 串行，执行 SequentialExecution
* 单actor，执行SimpleExecution

> 技术版本

* sbt:1.2.8
* scala:2.11.8
* lucene:4.7.2
* akka-actor:2.5.5
* htmlparser:2.1
* ikanalyzer:2012_u6
