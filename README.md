## 基于Scala Akka生产者-消费者模型的爬虫
  

> 本项目最初参考了Florian Hopf的akka-crawler-example项目的scala-version：

详细说明：

* ```http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html```
* ```https://github.com/fhopf/akka-crawler-example```
* 原项目包含3个简单的Web爬虫程序示例，可执行main方法在cn.edu.jxnu.akka.example中
  
* 本项目对引用的组件的版本、语法、过期接口进行了优化处理并解决了已知存在的BUG
* 本项目为二次开发，接口已经全部重构，仅actor的功能划分沿用了原项目
* 本项目使用全新的jsoup替换htmlparser

> 目前主要组件的版本

* sbt:1.2.8
* scala:2.11.8
* lucene:4.7.2
* akka-actor:2.5.5
* htmlparser:2.1
* ikanalyzer:2012_u6
* jsoup:1.10.3
* httpclient:4.3.5
* SpringBoot:1.5.9
* rxjava2:2.2.3

> 运行

1. 简单执行：cn.edu.jxnu.akka.run.FetchInParallelExecution中main方法
2. 使用SpringBoot执行：cn.edu.jxnu.akka.StartApp中springboot的启动方法

- 注意

1. 默认不使用代理，在cn.edu.jxnu.akka.common.Constant可修改
2. springboot相关配置在application.properties可修改

> 修改sbt镜像

1. 找到sbt安装目录下的conf文件夹
2. 在conf下创建repositories文件
3. repositories中加入以下内容

```
[repositories]
local
osc: http://maven.oschina.net/content/groups/public/
typesafe: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sonatype-oss-releases
maven-centra
```

- 示意图

随便画的，能看懂就行

![](https://github.com/jxnu-liguobin/scala-akka-crawler/blob/master/src/main/resources/actor_img_1.png)










