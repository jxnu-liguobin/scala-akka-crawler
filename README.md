基于Scala Akka生产者-消费者模型的爬虫

[![Build Status](https://travis-ci.org/jxnu-liguobin/scala-akka-crawler.svg?branch=master)](https://travis-ci.org/jxnu-liguobin/scala-akka-crawler)
![GitHub](https://img.shields.io/github/license/jxnu-liguobin/scala-akka-crawler.svg)
![GitHub top language](https://img.shields.io/github/languages/top/jxnu-liguobin/scala-akka-crawler.svg)
![GitHub stars](https://img.shields.io/github/stars/jxnu-liguobin/scala-akka-crawler.svg)
![GitHub forks](https://img.shields.io/github/forks/jxnu-liguobin/scala-akka-crawler.svg)
![GitHub followers](https://img.shields.io/github/followers/jxnu-liguobin.svg)
---

> * 本项目最初参考了Florian Hopf的akka-crawler-example项目的scala-version：

详细说明：

* ```http://blog.florian-hopf.de/2012/08/getting-rid-of-synchronized-using-akka.html```
* ```https://github.com/fhopf/akka-crawler-example```
* 原项目包含3个简单的Web爬虫程序示例，可执行main方法在cn.edu.jxnu.akka.example中
  
* 本项目对引用的组件的版本、语法、过期接口进行了优化处理并解决了已知存在的BUG
* 本项目为二次开发，接口已经全部重构，仅actor的功能划分沿用了原项目
* 本项目使用全新的jsoup替换htmlparser
* 本项目是sbt+SpringBoot项目

#### 本项目代理池参考了fengzhizi715的ProxyPool项目

* ```https://github.com/fengzhizi715/ProxyPool```
* 已使用Scala替代

#### 目前主要组件的版本

* sbt:1.2.8
* scala:2.11.8
* lucene:4.7.2
* akka-actor:2.5.5
* htmlparser:2.1
* ikanalyzer:2012_u6
* jsoup:1.10.3
* httpclient:4.3.5
* springboot:1.5.9
* rxjava2:2.2.3

#### 目前主要功能

- [x] 支持爬取内容索引
- [x] 支持抓取时UserAgent随机选取
- [x] 支持抓取时代理服务器随机选取
- [x] 支持代理服务器定时更新以及缓存
- [x] 支持结合SpringBoot开发业务逻辑
- [x] 支持利用Actor实现分布式抓取、支持重启等策略
- [x] 支持图片异步下载
- [x] 支持图片裁剪、存储
- [x] 支持忽略爬取异常并剔除URL

#### 运行

1. 简单执行：cn.edu.jxnu.akka.run.FetchInParallelExecution中main方法
2. 使用SpringBoot执行：cn.edu.jxnu.akka.StartApp中SpringBoot的启动方法

- 注意

1. 默认不使用代理，在cn.edu.jxnu.akka.common.Constant可修改
2. SpringBoot相关配置在application.properties可修改

#### 修改sbt镜像

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
- 额外（非必须）

1. IDEA 默认设置中搜sbt，设定最大Max堆1024MB（指定项目默认的sbt内存）；
2. 修改sbtopts文件，增加项 ```-mem   1024``` 指定全局sbt的内存；
3. IDEA 默认启动添加下列虚拟机参数，指定构建配置、sbt根路径以及仓库地址（指定项目默认的构建配置参数）；
```
-Dsbt.log.format=true -Dfile.encoding=UTF8 -Dsbt.boot.directory=D:/sbt -Dsbt.global.base=D:/sbt -Dsbt.ivy.home=D:/sbt -Dsbt.repository.config=D:/sbt/conf/repo.properties
```
注意：“额外”这里仓库配置是引用了 repo.properties 文件而不是默认的repositories。


#### 示意图

随便画的，能看懂就行

![](https://github.com/jxnu-liguobin/scala-akka-crawler/blob/master/src/main/resources/actor_img_1.png)










