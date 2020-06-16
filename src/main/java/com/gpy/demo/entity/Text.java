package com.gpy.demo.entity;

import java.util.Date;

public class Text {
    private int txtid;
    private String txtcontent;
    private int txtstatus;
    private int id;
    private  String title;
    private  String author;
    private int handle;
    private  int views;
    private String speciality;
    private int type;

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    private Date date;


    public int getTxtid() {
        return txtid;
    }

    public void setTxtid(int txtid) {
        this.txtid = txtid;
    }

    public String getTxtcontent() {
        return txtcontent;
    }

    public void setTxtcontent(String txtcontent) {
        this.txtcontent = txtcontent;
    }

    public int getTxtstatus() {
        return txtstatus;
    }

    public void setTxtstatus(int txtstatus) {
        this.txtstatus = txtstatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
