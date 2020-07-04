package com.manhua.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Html {
    public static ArrayList<UrlData> pareHtml(String body,StringBuilder sb){
        Pattern pattern = Pattern.compile("<h1>\\s*(.*?)\\s*<div");
        
        Matcher matcher = pattern.matcher(body);
        String fileName = "";
		
		if(matcher.find()) {
			fileName = matcher.group(1);
			sb.append(fileName);
		}

        ArrayList<UrlData> list = new ArrayList<UrlData>();

        pattern = Pattern.compile("<div class='cVolTag'>[\\s\\S]*?</ul>");
        Pattern tp = Pattern.compile("<div class='cVolTag'>(.*?)</div>");
		Pattern cp = Pattern.compile("<a.*?href='(.*?)'.*?>(.*?)</a>");

		matcher = pattern.matcher(body);
		while(matcher.find()) {
            String cont = matcher.group();
            Matcher tp_m = tp.matcher(cont);
            String minTitel = "";
            if(tp_m.find()){
                minTitel=tp_m.group(1);
            }
            tp_m = cp.matcher(cont);
            while(tp_m.find()){
                String href = tp_m.group(1);
                String name = tp_m.group(2);
                
                list.add(new UrlData(fileName+"/"+minTitel+"/"+name ,"http://www.huhudm.com"+href,name,minTitel));
                
            }
        }
        return list;
    }

    public static ArrayList<String> findUrl(String body,String url) {
		
		String hdVolID ="";
		String hdS ="";
		String hdDomain ="";
		String hdPageIndex ="";
		String hdPageCount ="";
		
		
		Pattern pattern = Pattern.compile("<input name=\"hdVolID\".*?value=\"(.*?)\".*?/>");
		Matcher matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdVolID = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdS\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdS = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdDomain\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdDomain = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdPageIndex\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdPageIndex = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdPageCount\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdPageCount = matcher.group(1);
        }
        int i = Integer.valueOf(hdPageIndex);
        int end = Integer.valueOf(hdPageCount);
        ArrayList<String> arry = new ArrayList<String>();
        for(;i<=end;i++){
            String url2 = url.replace(hdPageIndex+".html",i+".html");
            arry.add(url2);
        }
		return arry;
    }
    public static UrlData getUrl(String body,String path) {
        String hdVolID ="";
		String hdS ="";
		String hdDomain ="";
		String hdPageIndex ="";
		String hdPageCount ="";
        
        UrlData data = null;		
		Pattern pattern = Pattern.compile("<input name=\"hdVolID\".*?value=\"(.*?)\".*?/>");
		Matcher matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdVolID = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdS\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdS = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdDomain\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdDomain = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdPageIndex\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdPageIndex = matcher.group(1);
		}
		pattern = Pattern.compile("<input name=\"hdPageCount\".*?value=\"(.*?)\".*?/>");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			hdPageCount = matcher.group(1);
        }
        String domain = "http://www.huhudm.com/";
        pattern = Pattern.compile("<img.*?name=\"(.*?)\"");
		matcher = pattern.matcher(body);
		if(matcher.find()) {
			String code = matcher.group(1);
			String parseCode = parseCode(domain,code);
			String imageUrl =hdDomain.split("\\|")[0]+parseCode;
			String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));

             data = new UrlData(path+fileName, imageUrl,code,null);

        }
        return data;
    }
    public static String parseCode(String domain,String code) {
		String url ="";
		ScriptEngineManager m = new ScriptEngineManager();
		//获取JavaScript执行引擎
		ScriptEngine engine = m.getEngineByName("JavaScript");
		//执行JavaScript代码
		try {
			String script = "function unsuan(s) \r\n" + 
					"{\r\n" + 
					"	sw=\"huhudm.com|huhumh.com\";\r\n" + 
					"	su = \""+domain+"\";\r\n" + 
					"	b=false;\r\n" + 
					"	for(i=0;i<sw.split(\"|\").length;i++) {\r\n" + 
					"	    if(su.indexOf(sw.split(\"|\")[i])>-1) {\r\n" + 
					"	        b=true;\r\n" + 
					"	        break;\r\n" + 
					"        }\r\n" + 
					"    }\r\n" + 
					"    if(!b)return \"\";\r\n" + 
					"    \r\n" + 
					"    x = s.substring(s.length-1);\r\n" + 
					"    w=\"abcdefghijklmnopqrstuvwxyz\";\r\n" + 
					"    xi=w.indexOf(x)+1;\r\n" + 
					"    sk = s.substring(s.length-xi-12,s.length-xi-1);\r\n" + 
					"    s=s.substring(0,s.length-xi-12);    \r\n" + 
					"	k=sk.substring(0,sk.length-1);\r\n" + 
					"	f=sk.substring(sk.length-1);\r\n" + 
					"	for(i=0;i<k.length;i++) {\r\n" + 
					"	    eval(\"s=s.replace(/\"+ k.substring(i,i+1) +\"/g,'\"+ i +\"')\");\r\n" + 
					"	}\r\n" + 
					"    ss = s.split(f);\r\n" + 
					"	s=\"\";\r\n" + 
					"	for(i=0;i<ss.length;i++) {\r\n" + 
					"	    s+=String.fromCharCode(ss[i]);\r\n" + 
					"    }\r\n" + 
					"    return s;\r\n" + 
					"}";
			engine.eval(script);
			
			if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                url = in.invokeFunction("unsuan",code).toString();
            }
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;

	}


}