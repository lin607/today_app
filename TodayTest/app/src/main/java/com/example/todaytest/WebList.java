package com.example.todaytest;

public class WebList {  // recyclerview里面的数据列表里每一项显示啥
    private String newstitle;
    private String url;
    private byte[] bit_image;
    public WebList(String newstitle, String url, byte[] bit_image){
        this.newstitle = newstitle;
        this.url = url;
        this.bit_image = bit_image;
    }
    public String getUrl(){
        return url;
    }
    public String getNewstitle(){
        return newstitle;
    }
    public byte[] getBit_image(){
        return bit_image;
    }
//    public int getImageId(){
//        return imageId;
//    }
    public void setNewstitle(String newstitle){
        this.newstitle = newstitle;
    }
    public void setUrl(String url){
        this.url = url;
    }
}