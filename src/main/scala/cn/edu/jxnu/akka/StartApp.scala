package cn.edu.jxnu.akka

import cn.edu.jxnu.akka.api.impl.Executor
import cn.edu.jxnu.akka.common.Constant
import cn.edu.jxnu.akka.run.FetchInParallelExecution
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * SpringBoot启动
 */
object StartApp extends App {

    SpringApplication.run(classOf[AppConfig])
    val execution = new FetchInParallelExecution
    val exec = new Executor(execution)
    exec.execute(Constant.url_test)
}

@EnableCaching
@SpringBootApplication
@EnableScheduling
class AppConfig