package com.gpy.demo.entity;

import org.junit.experimental.theories.DataPoint;

import java.util.Date;

public class Abutment {

    private int id;
    private  String party_A;
    private  String party_B;
    private Date date;
    private  int status;
    private  String title;
    private  int txtid;
    private  int type;
    private  String speciality;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTxtid() {
        return txtid;
    }

    public void setTxtid(int txtid) {
        this.txtid = txtid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParty_A() {
        return party_A;
    }

    public void setParty_A(String party_A) {
        this.party_A = party_A;
    }

    public String getParty_B() {
        return party_B;
    }

    public void setParty_B(String party_B) {
        this.party_B = party_B;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
