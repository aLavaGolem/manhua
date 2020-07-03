package com.manhua;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import com.rabbitmq.client.DeliverCallback;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;

public class Main {
    // cpu核心数
    private static final int nThreads = Runtime.getRuntime().availableProcessors();
    // 线程池
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws Exception {
        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {

            fixedThreadPool.execute(() -> {
                try {
                    String url = new String(delivery.getBody(), "UTF-8");
                    System.out.println("Received:" + url);
                    try {
                        String body = Http.getTimeOut(url);
                        ArrayList<UrlData> list = Html.pareHtml(body);
                        System.out.println(list);
                        String pathQ = "/home/manhua/";
                        // String pathQ = "/";
                        for (UrlData data : list) {
                            new File(pathQ + data.getPath()).mkdirs();
                            Mq.sendMessage(data.getUrl() + "#" + pathQ + "/" + data.getPath(), "manhua2");
                        }

                    } catch (Exception e) {
                        // try(FileWriter fw = new FileWriter("fail.log",true))
                        // {
                        // fw.write(url+" "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
                        // Date()));
                        // } catch (Exception e2) {

                        // }
                        Mq.sendMessage(url, "manhua1-fail");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            });

        };
        Mq.getMessage(deliverCallback1, "manhua1");

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {

            fixedThreadPool.execute(() -> {
                try {
                    String strData = new String(delivery.getBody(), "UTF-8");
                    String url = strData.split("#")[0];
                    String path = strData.split("#")[1];

                    System.out.println("Received:" + url);
                    try {
                        String body = Http.getTimeOut(url);
                        ArrayList<String> list = Html.findUrl(body, url);
                        for (String str : list) {
                            Mq.sendMessage(str + "#" + path, "manhua3");
                        }

                    } catch (Exception e) {
                        Mq.sendMessage(strData, "manhua2-fail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        };
        Mq.getMessage(deliverCallback2, "manhua2");

        DeliverCallback deliverCallback3 = (consumerTag, delivery) -> {
            fixedThreadPool.execute(() -> {
                try {
                    String strData = new String(delivery.getBody(), "UTF-8");
                    String url = strData.split("#")[0];
                    String path = strData.split("#")[1];
                    System.out.println("Received:" + url);
                    try {
                        String body = Http.getTimeOut(url);
                        UrlData data = Html.getUrl(body, path);
                        if (data != null) {
                            try {
                                Http.downImageTimeOut(data.getUrl(), data.getPath());
                            } catch (Exception e) {
                                try (FileWriter fw = new FileWriter("fail.log", true)) {
                                    fw.write(strData + " "
                                            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                } catch (Exception e2) {
                                    Mq.sendMessage(strData, "manhua3-fail");
                                }
                            }

                        }

                    } catch (Exception e) {
                        Mq.sendMessage(strData, "manhua3-fail");
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            });

        };
        Mq.getMessage(deliverCallback3, "manhua3");
    }
}