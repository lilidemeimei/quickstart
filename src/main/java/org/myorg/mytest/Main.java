package org.myorg.mytest;


import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CloseableHttpAsyncClient httpclient = null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000) //设置HttpClient连接池超时时间
                .build();

        httpclient = HttpAsyncClients.custom()
                .setMaxConnTotal(20) //连接池最大连接数
                .setDefaultRequestConfig(requestConfig)
                .build();

        httpclient.start();

//        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        HttpGet httpGet = new HttpGet("http://qc.api.ba.qihoo.net/k?k=号码定位找人");

        Future<HttpResponse> future = httpclient.execute(httpGet, null);//callback是回调函数（也可通过回调函数拿结果）

        CompletableFuture<String> s =
                CompletableFuture.supplyAsync(new Supplier<String>() {

                    @Override
                    public String get() {

                        // 用try包住，处理get不到值时的报错程序
                        try {
                            HttpResponse response = future.get();
                            HttpEntity entity = response.getEntity();
                            String result = EntityUtils.toString(entity);
                            return result;
                        } catch (Exception e) {
                            // 拿不到的返回null(还没有查询到结果，就从future取了)
                            return "";
                        }
                    }
                });

        s.thenAccept(new Consumer<String>() {
            @Override
            public void accept(String integer) {
                System.out.println(integer);

            }
        });

        try {
            Thread.sleep(1000);
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        OkHttpClient okHttpClient = new OkHttpClient();//1.定义一个client
//        Request request = new Request.Builder().url("http://www.baidu.com").build();//2.定义一个request
//        Call call = okHttpClient.newCall(request);//3.使用client去请求
////        Future<HttpResponse> future = httpclient.execute(httpGet, null);
//        QueryInfo queryInfo = new QueryInfo();
//        Map<String, String> m = new HashMap<>();
//        call.enqueue(new Callback() {//4.回调方法
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();//5.获得网络数据
//
//                System.out.println(result);
//                m.put("result", result);
//                queryInfo.setStr(result);
//            }
//        });
    }
}
