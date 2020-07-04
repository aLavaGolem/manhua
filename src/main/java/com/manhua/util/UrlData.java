package com.manhua.util;
public class UrlData {
    private String path;
    private String url;
    private String name;
    private String parentName;
    private String title;

    public UrlData(String path,String url,String name,String parentName){
        this.url=url;
        this.path=path;
        this.name=name;
        this.parentName=parentName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
    @Override
    public String toString() {
        return "UrlData [path=" + path + ", url=" + url + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}