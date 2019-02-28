## 基于Scala Akka生产者-消费者模型的爬虫
  

> 本项目最初参考了Florian Hopf的akka-crawler-example项目的scala-version：

详细说明：

* http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html
* https://github.com/fhopf/akka-crawler-example
* 原项目包含4个简单的Web爬虫程序示例，可执行main方法在cn.edu.jxnu.akka.actor.exec中
        
* 本项目对引用的组件的版本、语法、过期接口进行了优化处理并解决了已知存在的BUG
* 本项目中只对FetchInParallelExecution做拓展，继续开发，其他废弃。
* 本项目接口已经全部重构

> 目前技术版本

* sbt:1.2.8
* scala:2.11.8
* lucene:4.7.2
* akka-actor:2.5.5
* htmlparser:2.1 准备废弃
* ikanalyzer:2012_u6 可选
* jsoup:1.10.3 定义新的接口

> 运行

FetchInParallelExecution中main方法即可




