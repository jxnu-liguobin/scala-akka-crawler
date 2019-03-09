package cn.edu.jxnu.akka

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

import scala.beans.BeanProperty

/**
 * 测试
 */
@Controller
class TestSpringBoot {
    @Value("${hello.message}")
    @BeanProperty var helloMessage: String = _

    @RequestMapping(value = Array("/hello"), method = Array(RequestMethod.GET))
    @ResponseBody
    def hello(): Message = {
        val message = new Message
        message.value = helloMessage
        message
    }
}

class Message {
    @BeanProperty var value: String = _
}