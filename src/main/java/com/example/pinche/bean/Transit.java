package com.example.pinche.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * 审核中介表
 */
@Entity
public class Transit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//设置为自动递增
    private int id;
    private String id_unique;
    private String url_driver_licence;//可访问的url
    private String url_idcard_obverse;
    private String url_idcard_reverse;
    private String dri_car;//车辆信息
    private String dri_number;//车牌号
    private String name;//真实姓名
    private String id_card;//身份证号码
    private int state;//审核状态（0待审核1通过2拒绝）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_unique() {
        return id_unique;
    }

    public void setId_unique(String id_unique) {
        this.id_unique = id_unique;
    }

    public String getUrl_driver_licence() {
        return url_driver_licence;
    }

    public void setUrl_driver_licence(String url_driver_licence) {
        this.url_driver_licence = url_driver_licence;
    }

    public String getUrl_idcard_obverse() {
        return url_idcard_obverse;
    }

    public void setUrl_idcard_obverse(String url_idcard_obverse) {
        this.url_idcard_obverse = url_idcard_obverse;
    }

    public String getUrl_idcard_reverse() {
        return url_idcard_reverse;
    }

    public void setUrl_idcard_reverse(String url_idcard_reverse) {
        this.url_idcard_reverse = url_idcard_reverse;
    }

    public String getDri_car() {
        return dri_car;
    }

    public void setDri_car(String dri_car) {
        this.dri_car = dri_car;
    }

    public String getDri_number() {
        return dri_number;
    }

    public void setDri_number(String dri_number) {
        this.dri_number = dri_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
