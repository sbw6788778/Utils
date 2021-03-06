package com.utils.HttpUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class HttpUtils {


    private static HttpClient client = getHttpClient();

    public static HttpClient getHttpClient() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(2);
        manager.setDefaultMaxPerRoute(1);
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(5000)
                .setConnectTimeout(30000).build();
        HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                //无论什么情况都会重试三次
                if (executionCount > 3) {
                    log.error("retry has more than 3 times , give up");
                    return false;
                }
                return true;
            }
        };
        ConnectionKeepAliveStrategy strategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                return 1000;
            }
        };
        return HttpClients.custom().setConnectionManager(manager)
                .setRetryHandler(handler)
                .setDefaultRequestConfig(requestConfig)
                .setKeepAliveStrategy(strategy)
                .build();
    }

    public String getPageHtmlbyPost(String url, String param) {
        HttpPost post = new HttpPost(url);
        String result = null;
        try {
            StringEntity httpEntity = new StringEntity(param);
            post.setEntity(httpEntity);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(post);
            InputStream inputStream = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            result = new String(baf.toByteArray());
            bis.close();
            inputStream.close();
        } catch (Exception e) {
            log.error("创建post参数实体异常", e);
        }
        return result;
    }

    public static String getPageHtmlbyGet(String url) {
        HttpGet get = new HttpGet(url);
        String result = null;
        try {
            get.setHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(get);
            InputStream inputStream = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            result = new String(baf.toByteArray());
            bis.close();
            inputStream.close();
        } catch (Exception e) {
            log.error("get error:", e);
        }
        return result;
    }

    @Test
    public void testPost() {
        System.out.println(getPageHtmlbyPost("http://10.4.56.235/priceapi/query/json/v2/inner/all", "{\"pids\":[25152802],\"units\":[\"1_1\",\"2_1\"]}"));
    }

    @Before
    public void init() {
        ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("classpath:spring.xml");
    }

    @Test
    public void testTimeOut() {
        ExecutorService executorService = new ThreadPoolExecutor(20, 20, 0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100000),
                new ThreadPoolExecutor.AbortPolicy());
        while (true){
            try {
                Thread.sleep(1);
                executorService.execute(()->System.out.println(getPageHtmlbyGet("http://localhost:8080/test/socket_timeout?a="+Thread.currentThread().getName())));
            }catch (Exception e){
                e.getStackTrace();
            }

        }


    }

    public static void main(String[] args) throws Exception {
        List<String> list = readFile();
        String url = "http://10.4.96.36:8500/index/deleteProductInfo?";
        List<List<String>> partitions = Lists.partition(list, 10);
        ExecutorService executorService = new ThreadPoolExecutor(20, 20, 0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100000),
                new ThreadPoolExecutor.AbortPolicy());
        int i = 0;
        log.info("size:{}", partitions.size());
        CountDownLatch countDownLatch = new CountDownLatch(partitions.size());
        for (List<String> partition : partitions) {
            executorService.execute(() -> {
                try {
                    delete(url, partition);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            log.error("countDownLatch.await error", e);
        }
        log.info("done");

    }

    private static void delete(String url, List<String> partition) {
        StringBuffer productIds = new StringBuffer();
        StringBuffer barCodes = new StringBuffer();
        partition.stream().forEach(e -> {
            List<String> list1 = Splitter.on(",").omitEmptyStrings().splitToList(e);
            if (list1.size() != 2) {
                log.info("缺少东西:{}", e);
                return;
            }
            productIds.append(list1.get(0));
            productIds.append(",");
            barCodes.append(list1.get(1));
            barCodes.append(",");
        });
        if (productIds.length() == 0 || barCodes.length() == 0) {
            return;
        }
        productIds.deleteCharAt(productIds.length() - 1);
        barCodes.deleteCharAt(barCodes.length() - 1);
        String get = url + "productIds=" + productIds + "&barCodes=" + barCodes;
        String res = getPageHtmlbyGet(get);
        if (res.equals("ok")) {
            log.info("成功");
        } else {
            log.info(get);
        }
    }

    public static List<String> readFile() {
        List<String> list = Lists.newArrayList();
        try {
            list = Files.readLines(new File("E://20714.txt"), Charset.forName("UTF-8"));
        } catch (Exception e) {

        }
        return list;
    }
}
