package com.example.classup.navneet122CS0958.notice;

public class NoticeData {
    private String title;
    private String downloadUrl;
    private String date;
    private String time;
    private String uniqueKey;
    private String image;

    public NoticeData() {
        // Empty constructor for Firebase
    }

    public NoticeData(String title, String downloadUrl, String date, String time, String uniqueKey) {
        this.title = title;
        this.downloadUrl = downloadUrl;
        this.date = date;
        this.time = time;
        this.uniqueKey = uniqueKey;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }



    public String getTitle() {
        return title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }
}
