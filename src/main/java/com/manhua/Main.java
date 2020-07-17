package com.manhua;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.manhua.entity.Link;
import com.manhua.util.*;
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
                    
                    //查询数据库
                    if(DB.urlExists(url)){
                        return;
                    }
                   Link link= new Link(url,null,new Date(),10);
                    DB.save(link);



                    System.out.println("Received:" + url);
                    try {
                        String body = Http.getTimeOut(url);
                        StringBuilder sb = new StringBuilder();
                        ArrayList<UrlData> list = Html.pareHtml(body,sb);
                        System.out.println(list);
                        String pathQ = "/home/manhua/";
                        //String pathQ = "";

                        //ArrayList<Link> array = new ArrayList<Link>();

                        for (UrlData data : list) {
                            new File(pathQ + data.getPath()).mkdirs();
                            Link link_temp = new Link(data.getUrl(),data.getPath(),new Date(),data.getName(),data.getParentName(),url,11);
                            link_temp.setParentId(link.getId());
                            //array.add(link_temp);
                            DB.save(link_temp);

                            Mq.sendMessage(data.getUrl() + "#" + pathQ + data.getPath()+"#"+link_temp.getId(), "manhua2");
                            
                        }
//                        if(array.size()>0){
//                            DB.save(array);
//                        }
                        link.setName(sb.toString());
                        link.setPath(sb.toString());
                        DB.update(link);
                        
                    } catch (Exception e) {
                        // try(FileWriter fw = new FileWriter("fail.log",true))
                        // {
                        // fw.write(url+" "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
                        // Date()));
                        // } catch (Exception e2) {

                        // }
                    	e.printStackTrace();
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
                    String id = strData.split("#")[2];
                    
                    System.out.println("Received:" + url);

                    ArrayList<Link> array = new ArrayList<Link>();
                    try {
                        String body = Http.getTimeOut(url);
                        ArrayList<String> list = Html.findUrl(body, url);
                        for (String str : list) {
                            Mq.sendMessage(str + "#" + path+"#"+id, "manhua3");

                            if(!DB.urlExists(str)){
                                Link link_temp = new Link(str,path,new Date(),null,null,url,12);
                                link_temp.setParentId(Long.valueOf(id));
								array.add( link_temp);
                            }
                            
                        }
                        if(array.size()>0){
                            DB.save(array);
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
                    String id = strData.split("#")[2];
                    System.out.println("Received:" + url);
                    try {
                        String body = Http.getTimeOut(url);
                        UrlData data = Html.getUrl(body, path);
                        

                        if(!DB.urlExists(data.getUrl())){
                            Link link_temp = new Link(data.getUrl(),data.getPath(),new Date(),null,null,url,13);
                            link_temp.setParentId(Long.valueOf(id));
							DB.save( link_temp);
                        }

                        if (data != null) {
                            try {
                                Http.downImageTimeOut(data.getUrl(), data.getPath());
                            } catch (Exception e) {
                                // try (FileWriter fw = new FileWriter("fail.log", true)) {
                                //     fw.write(strData + " "
                                //             + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                // } catch (Exception e2) {
                                //     Mq.sendMessage(strData, "manhua3-fail");
                                // }
                                Mq.sendMessage(strData, "manhua3-fail");
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