package com.example.pinche.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Entity
////@Entity表明该类 为一个实体类
public class Pas_dingdan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//设置为自动递增
    private int id;
    private  String pas_id;//乘客id

    private int path;//路线
    /**复查日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;//时间

    private int site;//乘客预留空位
    private String dri_id;//司机id
    private String place;//上车点
    private int status;//订单状态：1有订单未完成 0没有
    /**
     * change定义出错，不知道为什么
     */
    private int score;//信誉积分
    private String change_score;//信誉积分变化
    private String reason;//积分变化原因
    private String dri_number;//司机的车牌号
    private String dri_car;//车辆信息
    private String phone;//乘客手机号
    private String name;//乘客姓名


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPas_id() {
        return pas_id;
    }

    public void setPas_id(String pas_id) {
        this.pas_id = pas_id;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getDri_id() {
        return dri_id;
    }

    public void setDri_id(String dri_id) {
        this.dri_id = dri_id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getChange_score() {
        return change_score;
    }

    public void setChange_score(String change_score) {
        this.change_score = change_score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDri_number() {
        return dri_number;
    }

    public void setDri_number(String dri_number) {
        this.dri_number = dri_number;
    }

    public String getDri_car() {
        return dri_car;
    }

    public void setDri_car(String dri_car) {
        this.dri_car = dri_car;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
