## 基于Scala Akka生产者-消费者模型的爬虫
  

> 本项目最初参考了Florian Hopf的akka-crawler-example项目的scala-version：

详细说明：

* ```http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html```
* ```https://github.com/fhopf/akka-crawler-example```
* 原项目包含3个简单的Web爬虫程序示例，可执行main方法在cn.edu.jxnu.akka.example中
  
* 本项目对引用的组件的版本、语法、过期接口进行了优化处理并解决了已知存在的BUG
* 本项目为二次开发，接口已经全部重构，仅actor的功能划分沿用了原项目
* 本项目使用全新的jsoup替换htmlparser

> 目前技术版本

* sbt:1.2.8
* scala:2.11.8
* lucene:4.7.2
* akka-actor:2.5.5
* htmlparser:2.1
* ikanalyzer:2012_u6
* jsoup:1.10.3

> 运行

cn.edu.jxnu.akka.run.FetchInParallelExecution中main方法即可

> 目前的核心接口与核心Actor

- 接口

* Execution 执行器
* Indexer 索引器
* PageRetriever 爬页器
* PageVisitor 页面解析访问器

- 核心Actor

* ImageDownloadActor 图片下载Actor
* IndexingActor 信息索引Actor
* PageParsingActor 页面解析Actor
* SearchActor 信息查询Actor
* ParallelActorMaster 主Actor

- 示意图

随便画的，能看懂就行

![](https://github.com/jxnu-liguobin/scala-akka-crawler/blob/master/src/main/resources/actor_img_1.png)








