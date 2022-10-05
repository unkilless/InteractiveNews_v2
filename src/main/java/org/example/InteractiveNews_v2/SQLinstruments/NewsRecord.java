package org.example.InteractiveNews_v2.SQLinstruments;

import java.sql.Timestamp;

public class NewsRecord {
    private int id;
    private Timestamp dateTime;
    private String url;
    private String newsData;
    private String tag;
    //private SimpleDateFormat formatter;

    public NewsRecord(){
        this.dateTime = Timestamp.valueOf("2022-08-31 22:37:00.0");
        this.url = "interfax.ru";
        this.newsData = "";
        this.tag = "world";
        //this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public void set(int id, Timestamp dateTime, String url, String newsData, String tag){
        this.id = id;
        this.dateTime = dateTime;
        this.url = url;
        this.newsData = newsData;
        this.tag = tag;
    }

    public void set(Timestamp dateTime, String url, String newsData, String tag){
        this.dateTime = dateTime;
        this.url = url;
        this.newsData = newsData;
        this.tag = tag;
    }

    public String toString() {
        return this.id + "\t|\t" + this.dateTime.toString() + "\t|\t" + this.url + "\t|\t" + this.newsData + "\t|\t" + this.tag;
    }

    public Timestamp getDateTime(){
        return this.dateTime;
    }

    public String getURL(){
        return this.url;
    }

    public String getNewsData(){
        return this.newsData;
    }

    public String getTag(){
        return this.tag;
    }
}