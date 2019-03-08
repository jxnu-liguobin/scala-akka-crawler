package cn.edu.jxnu.akka.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.WordSpecLike


/**
 * 使用testkit测试图片下载
 */
class ImageDownloadActorTest extends TestKit(ActorSystem("imageDownload")) with ImplicitSender with WordSpecLike {


    "Test download image" must {
        "response" in {
            val downloadImage = system.actorOf(Props.create(classOf[ImageDownloadActor]))
            downloadImage ! "hello world"
            //日志输出Unmatched message types
        }
    }

}
