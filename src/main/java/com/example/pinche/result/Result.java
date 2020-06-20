package com.example.pinche.result;

/**
 * 返回的Json数组
 * @param <T>
 */
public class Result<T> {
    //返回状态码
    private String status;
    //返回信息
    private String msg;
    //具体返回的数据
    private T data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
