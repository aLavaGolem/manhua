package com.manhua;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.log4j.*;

public class Http {

    private static Logger logger = Logger.getLogger(Http.class);
    
    public static void main(String[] args) throws Exception {
        String url = "http://www.huhudm.com/huhu2086.html";
        System.out.println(getTimeOut(url));

    }

    public static String get(String urlPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(urlPath);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        try (InputStream input = conn.getInputStream()) {
            byte[] buff = new byte[1024 * 2];
            int len;
            while ((len = input.read(buff)) != -1) {
                sb.append(new String(buff, 0, len));
            }
        }

        return sb.toString();
    }

    public static String getTimeOut(String url) throws Exception {
        int i =0;
        String data = null;
        for(;i<3;i++){
            try {
                data = get(url);
                i=3;
            } catch (Exception e) {
                if(i==2){
                    //e.printStackTrace();
                    throw e;
                }
                
            }
        }        
        return data;
    }
    public static void downImageTimeOut(String url,String path) throws Exception{
        int i =0;
        for(;i<3;i++){
            try {
                downImage(url,path);
                i=3;
            } catch (Exception e) {
                if(i==2){
                    //e.printStackTrace();
                    throw e;
                }
                
            }
        }        
    }
    public static void downImage(String urlString,String path) throws Exception{
        if(new File(path).exists()) {
            return;
        } 
       // 构造URL
       URL url = new URL(urlString);
       // 打开连接
       URLConnection con = url.openConnection();
       // 输入流
       InputStream is = con.getInputStream();
       // 1K的数据缓冲
       byte[] bs = new byte[1024*1024];
       // 读取到的数据长度
       int len;
       // 输出的文件流
       File file = new File(path);
       FileOutputStream os = new FileOutputStream(file);
       // 开始读取
       while ((len = is.read(bs)) != -1) {
           os.write(bs, 0, len);
       }
       os.flush();
       // 完毕，关闭所有链接
       os.close();
       is.close();
       logger.info("下载完成："+path);  
    }
}