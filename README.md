## 基于Scala Akka生产者-消费者模型的爬虫
  

> 这是根据Florian Hopf的example项目二次开发的：

http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html

https://github.com/fhopf/akka-crawler-example

> 此代码库包含4个简单的Web爬虫程序示例，可执行main方法在cn.edu.jxnu.akka.actor.exec中：

* 多actor，执行 FetchInParallelExecution（本项目中，下面的actor仅做测试，已废弃）
* 串行，执行 SequentialExecution
* 单actor，执行SimpleExecution
* 失败actor，执行FailingExecution

> 目前技术版本

* sbt:1.2.8
* scala:2.11.8
* lucene:4.7.2
* akka-actor:2.5.5
* htmlparser:2.1
* ikanalyzer:2012_u6

