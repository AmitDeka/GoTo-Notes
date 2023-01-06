package com.android.gotonotes.utils;

import com.google.firebase.database.Exclude;

public class AdapterModel {
    private String title;
    private String content;
    private String date;
    @Exclude
    public String documentId;

    public AdapterModel() {
    }

    public AdapterModel(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Exclude
    public String getDocumentId(){
        return documentId;
    }
    public void setDocumentId(String documentId){
        this.documentId = documentId;
    }
}