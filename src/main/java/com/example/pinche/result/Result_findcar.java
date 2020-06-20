package com.example.pinche.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Result_findcar {
    private String pas_id;//乘客id
    private String dri_id;//司机id
    private String id_unique;//用户id
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //时间段
    private Date time_start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time_end;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;//当前时间


    private int path;//路线
    private int site;//上车人数
    private String place;//上车点

    public String getPas_id() {
        return pas_id;
    }

    public void setPas_id(String pas_id) {
        this.pas_id = pas_id;
    }

    public String getDri_id() {
        return dri_id;
    }

    public void setDri_id(String dri_id) {
        this.dri_id = dri_id;
    }

    public String getId_unique() {
        return id_unique;
    }

    public void setId_unique(String id_unique) {
        this.id_unique = id_unique;
    }

    public Date getTime_start() {
        return time_start;
    }

    public void setTime_start(Date time_start) {
        this.time_start = time_start;
    }

    public Date getTime_end() {
        return time_end;
    }

    public void setTime_end(Date time_end) {
        this.time_end = time_end;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
