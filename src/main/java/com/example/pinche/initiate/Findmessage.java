package com.example.pinche.initiate;

import com.example.pinche.UserMapper;
import com.example.pinche.bean.Dri_dingdan;
import com.example.pinche.bean.Identity;
import com.example.pinche.bean.Pas_dingdan;
import com.example.pinche.mapper.Dri_repository;
import com.example.pinche.mapper.Identity_repository;
import com.example.pinche.mapper.Pas_repository;
import com.example.pinche.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Findmessage {
    @Autowired
    private Identity_repository identity_repository;
    @Autowired
    private Dri_repository dingdan_repository;
    @Autowired
    private Pas_repository pas_repository;

    @Autowired
    private UserMapper userMapper;



    //查看所有用户数据
    public Result find_all_identity(){
        Result result = new Result();
        result.setData(null);
        try{
            //查询数据

            List<Identity> existUser = userMapper.find_all_identity();
            if (existUser != null){//如果有内容且无正在进行的订单
                result.setStatus("202");
                result.setMsg("查询成功");
                result.setData(existUser);
            }else {
                result.setStatus("406");
                result.setMsg("暂无用户数据");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查看所有司机数据
    public Result find_all_dri(){
        Result result = new Result();
        result.setData(null);
        try{
            //查询数据

            List<Identity> existUser = userMapper.find_all_identity_what(1);
            if (existUser != null){//如果有内容且无正在进行的订单
                result.setStatus("202");
                result.setMsg("查询成功");
                result.setData(existUser);
            }else {
                result.setStatus("406");
                result.setMsg("暂无订单数据");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查看所有司机数据
    public Result find_all_pas(){
        Result result = new Result();
        result.setData(null);
        try{
            //查询数据

            List<Identity> existUser = userMapper.find_all_identity_what(0);
            if (existUser != null){//如果有内容且无正在进行的订单
                result.setStatus("202");
                result.setMsg("查询成功");
                result.setData(existUser);
            }else {
                result.setStatus("406");
                result.setMsg("暂无订单数据");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查看所有司机订单数据
    public Result find_all_dri_dingdan(){
        Result result = new Result();
        result.setData(null);
        try{
            //查询数据

            List<Dri_dingdan> existUser = userMapper.find_all_dri_dingdan();
            if (existUser != null){//如果有内容且无正在进行的订单
                result.setStatus("202");
                result.setMsg("查询成功");
                result.setData(existUser);
            }else {
                result.setStatus("406");
                result.setMsg("暂无订单数据");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查看所有乘客订单数据
    public Result find_all_pas_dingdan(){
        Result result = new Result();
        result.setData(null);
        try{
            //查询数据

            List<Pas_dingdan> existUser = userMapper.find_all_pas_dingdan();
            if (existUser != null){//如果有内容且无正在进行的订单
                result.setStatus("202");
                result.setMsg("查询成功");
                result.setData(existUser);
            }else {
                result.setStatus("406");
                result.setMsg("暂无订单数据");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
