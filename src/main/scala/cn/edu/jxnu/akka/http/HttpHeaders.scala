package cn.edu.jxnu.akka.http

import java.util.Random

import scala.collection.mutable.{HashMap, Map}

/**
 * @author 梦境迷离
 * @version 1.0, 2019-03-17
 */
object HttpHeaders {

    final val userAgentArray = Array("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
        "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36")
    final val ACCEPT_ENCODING: String = "gzip, deflate"
    final val CACHE_CONTROL = "max-age=0"
    final val CONNECTION = "keep-alive"
    final val ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2,de;q=0.2"
    final val USER_AGENT = org.apache.http.HttpHeaders.USER_AGENT

    /**
     * 随机选取浏览器标识
     */
    def getUserAgents(): String = {
        val index = new Random().nextInt(HttpHeaders.userAgentArray.length)
        userAgentArray(index)
    }

    /**
     * 设置其他请求头
     *
     * @return
     */
    def getOtherHeads(): Map[String, String] = {
        val heads: HashMap[String, String] = new HashMap[String, String]
        heads.+=(org.apache.http.HttpHeaders.ACCEPT_LANGUAGE -> ACCEPT_LANGUAGE)
        heads.+=(org.apache.http.HttpHeaders.ACCEPT_ENCODING -> ACCEPT_ENCODING)
        heads.+=(org.apache.http.HttpHeaders.CACHE_CONTROL -> CACHE_CONTROL)
        heads.+=(org.apache.http.HttpHeaders.CONNECTION -> CONNECTION)
    }
}
