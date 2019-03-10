package cn.edu.jxnu.akka.http.proxy;

import cn.edu.jxnu.akka.entity.Proxy;
import cn.edu.jxnu.akka.http.HttpManager;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.apache.http.HttpHost;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代理管理器暂时使用java
 */
public class ProxyManager {

    private Logger log = LoggerFactory.getLogger(ProxyManager.class);


    private ProxyManager() {
    }

    public static ProxyManager get() {
        return ProxyManager.Holder.MANAGER;
    }

    private static class Holder {
        private static final ProxyManager MANAGER = new ProxyManager();
    }

    /**
     * 抓取代理，成功的代理存放到ProxyPool中
     */
    public void start() {

        Flowable.fromIterable(ProxyPool.proxyMap().keySet())
                .parallel(ProxyPool.proxyMap().size())
                .map(s -> {
                    try {
                        return new ProxyPageCallable(s).call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<cn.edu.jxnu.akka.entity.Proxy>();
                })
                .flatMap((Function<List<Proxy>, Publisher<Proxy>>) proxies -> {
                    if (proxies != null) {
                        List<Proxy> result = proxies
                                .stream()
                                .parallel()
                                .filter(proxy -> {
                                    HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort(), proxy.getType());
                                    boolean result1 = HttpManager.get().checkProxy(httpHost);
                                    if (result1) log.info("Check Proxy " + proxy.getProxyStr() + ", " + result1);
                                    return result1;
                                }).collect(Collectors.toList());

                        return Flowable.fromIterable(result);
                    }

                    return Flowable.empty();
                })
                .runOn(Schedulers.io())
                .sequential()
                .subscribe(proxy -> {
                    if (proxy != null) {
                        log.info("Accept " + proxy.getProxyStr());
                        proxy.setLastSuccessfulTime(new Date().getTime());
                        ProxyPool.proxyList().add(proxy);
                    }
                }, throwable -> log.error("ProxyManager is error: " + throwable.getMessage()));
    }
}
