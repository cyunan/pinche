package com.example.pinche.bean;

import javax.persistence.*;


@Entity

//@Entity表明该类 (Identity) 为一个实体类，它默认对应数据库中的表名是
public class Identity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//设置为自动递增
    private int id;
    private String id_unique;//用户id标识（手机号后四位）
    private String name;//姓名
    private String sex;//性别
    private String phone;//手机号码
    private String emergency_phone;//紧急联系人
    private String password;//登录密码
    private int identity;//身份：1 司机 0乘客
    private String dri_number;//司机的车牌号
    private String idcard;//司机身份证信息
    private String dri_car;//车辆信息
    private String pas_id;//学生学号
    private String pas_class;//班级
    private int status;//订单状态：1有订单未完成 0没有
    private int score;//信誉积分
    private boolean is_add = false;//完善标志（是否已经完善）
    private String url;//用户头像url

    private String pascard_url;//学/教工卡图片
    private int check_driver;//申请司机身份审核状态（0待提交1待审核2通过3拒绝）

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergency_phone() {
        return emergency_phone;
    }

    public void setEmergency_phone(String emergency_phone) {
        this.emergency_phone = emergency_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getDri_number() {
        return dri_number;
    }

    public void setDri_number(String dri_number) {
        this.dri_number = dri_number;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getDri_car() {
        return dri_car;
    }

    public void setDri_car(String dri_car) {
        this.dri_car = dri_car;
    }

    public String getPas_id() {
        return pas_id;
    }

    public void setPas_id(String pas_id) {
        this.pas_id = pas_id;
    }

    public String getPas_class() {
        return pas_class;
    }

    public void setPas_class(String pas_class) {
        this.pas_class = pas_class;
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

    public boolean isIs_add() {
        return is_add;
    }

    public void setIs_add(boolean is_add) {
        this.is_add = is_add;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPascard_url() {
        return pascard_url;
    }

    public void setPascard_url(String pascard_url) {
        this.pascard_url = pascard_url;
    }

    public int getCheck_driver() {
        return check_driver;
    }

    public void setCheck_driver(int check_driver) {
        this.check_driver = check_driver;
    }
}
