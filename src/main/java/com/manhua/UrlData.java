package com.manhua;
public class UrlData {
    private String path;
    private String url;

    public UrlData(String path,String url){
        this.url=url;
        this.path=path;
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


}