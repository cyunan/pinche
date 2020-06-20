package com.example.pinche.initiate;

import com.example.pinche.UserMapper;
import com.example.pinche.bean.Dri_dingdan;
import com.example.pinche.bean.Identity;
import com.example.pinche.bean.Pas_dingdan;
import com.example.pinche.mapper.Dri_repository;
import com.example.pinche.mapper.Identity_repository;
import com.example.pinche.mapper.Pas_repository;
import com.example.pinche.result.Result;
import com.example.pinche.result.Result_findcar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class Pas_initiate {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Dri_repository dri_repository;
    @Autowired
    private Pas_repository pas_repository;
    @Autowired
    private Identity_repository identity_repository;


    //查询车辆
    public Result find_car(Result_findcar Rf){
        Result result = new Result();
        try{
            //获取某一时间段某一路线的车辆
            List<Dri_dingdan> dd = userMapper.find_car(Rf.getPath(),1,Rf.getTime_start(),Rf.getTime_end(),Rf.getSite());
            if (dd!=null){
                result.setStatus("204");
                result.setMsg("查询成功");
                result.setData(dd);
            }else {
                result.setStatus("408");
                result.setMsg("暂无车辆");
            }


        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    //乘客选择车辆
    public Result select_car(Result_findcar result_findcar){
        Result result = new Result();
        try{
            //查询乘客是否有未完成订单
            Identity existUser_pas = userMapper.findUserByid_unique(result_findcar.getPas_id());
            Pas_dingdan pd = new Pas_dingdan();
//            Pas_dingdan pd = userMapper.findpas_id(result_findcar.getDri_id(),1);
            //选择车辆
            Dri_dingdan dd = userMapper.select_car(result_findcar.getDri_id(),1);
            if (existUser_pas.getStatus() == 0){//该乘客没有未完成订单
                pd.setPas_id(result_findcar.getPas_id());
                pd.setSite(result_findcar.getSite());
                pd.setPlace(result_findcar.getPlace());
                selectcar(dd,pd,existUser_pas);
                result.setStatus("204");
                result.setMsg("选择车辆成功");
                result.setData(dd);
            }else {
                result.setStatus("409");
                result.setMsg("已有选择车辆");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();;
        }

        return result;
    }
    /**
     * 事务 指数据库事务
     */
    @Transactional//要么都成功，要么都失败
    public void selectcar(Dri_dingdan dd,Pas_dingdan pd,Identity pas){
        String id = pd.getPas_id();
        String place = pd.getPlace();
        int site_pas = pd.getSite();
        /**
         * 将乘客数据存入pas_dingdan
         *
         */
        pd.setPath(dd.getPath());
        pd.setTime(dd.getTime());
        pd.setDri_id(dd.getDri_id());
        pd.setStatus(1);
        pd.setScore(pas.getScore());
        pd.setDri_car(dd.getDri_car());
        pd.setDri_number(dd.getDri_number());
        pd.setPhone(pas.getPhone());
        pd.setName(pas.getName());
        pas_repository.save(pd);



        /**
         * 将乘客id存入该司机发车数据中
         */

        if(dd.getPas_numbers()!=0){//如果已有乘客信息，用","隔开，叠加字符串
            dd.setPas_id(dd.getPas_id()+","+id);
            dd.setPlace(dd.getPlace()+","+place);
        }else {
            dd.setPas_id(id);
            dd.setPlace(place);
        }

        int site_dri = dd.getSite()-site_pas;
        dd.setSite(site_dri);
        dd.setPas_numbers(dd.getPas_numbers()+1);
        dri_repository.save(dd);


        /**
         * 将identity的乘客status改为1（未完成态）
         */
        pas.setStatus(1);
        identity_repository.save(pas);

    }
    //乘客取消订单
    public Result delete(Result_findcar pas_dingdan){
        Result result = new Result();
        try{
            Pas_dingdan pd = userMapper.findpas_id(pas_dingdan.getPas_id(),1);//乘客订单
            if (pd!=null) {
                Dri_dingdan dd = userMapper.find_dri_id(pd.getDri_id(), 1);
                Identity user_pas = userMapper.findUserByid_unique(pd.getPas_id());//identity表中乘客的数据

                Date time = pas_dingdan.getTime();
                pas_delete(pd, user_pas, dd, time);
                result.setStatus("204");
                result.setMsg("取消成功");
            }else {
                result.setStatus("410");
                result.setMsg("乘客没有正在进行的订单");
            }


        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Transactional//要么都成功，要么都失败//Todo 同步锁不起作用
    public void pas_delete(Pas_dingdan pd,Identity user_pas,Dri_dingdan dd,Date time){

        /**
         * pas_dingdan中的乘客订单status值改为0，完成态
         */
        if (pd.getTime().getTime()-time.getTime()<=60){//如果在发车前1小时内取消，扣积分//todo 待实现：如果现在时间大于发车时间，不给取消
            pd.setScore(pd.getScore()-10);
            pd.setChange_score("-10");
        }
        int site = pd.getSite();//乘客预约位置
        int score = pd.getScore();//乘客目前的积分

        String pas_id = pd.getPas_id();
        String place = pd.getPlace();

        pd.setStatus(0);
        pd.setReason("取消订单");
        pas_repository.save(pd);

        /**
         * identity中的乘客订单
         */
        user_pas.setScore(score);//修改为现在的积分
        user_pas.setStatus(0);
        identity_repository.save(user_pas);

        /**
         * dri_dingdan表中的司机订单
         */
        dd.setSite(dd.getSite()+site);//剩余座位加回乘客预留的位置
        String pas_id_end = dd.getPas_id();
        String place_end = dd.getPlace();

        //去除字符串字段（pas_id,place）
//        int i = pas_id_end.indexOf(pas_id);
//        int j = place_end.indexOf(place);
//
//        pas_id_end=pas_id_end.substring(0, i)+pas_id_end.substring(i+1);
//        place_end=place_end.substring(0, j)+place_end.substring(j+1);
        pas_id_end = pas_id_end.replace(pas_id,"");
        place_end = place_end.replace(place,"");
        dd.setPas_numbers(dd.getPas_numbers()-1);

        //重新储存
        dd.setPas_id(pas_id_end);
        dd.setPlace(place_end);
        dri_repository.save(dd);

    }


    //乘客查询
    public Result query(String pas_id){
        Result result = new Result();
        result.setData(null);
        try{
            Pas_dingdan pd = userMapper.findpas_id(pas_id,1);//乘客订单
            if (pd!=null) {
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(pd);
            }else {
                result.setStatus("410");
                result.setMsg("乘客没有正在进行的订单");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //乘客查询司机详细信息
    public Result query_dri(String dri_id){
        Result result = new Result();
        result.setData(null);
        try{
            Identity pd = userMapper.findUserByid_unique(dri_id);//乘客订单
            if (pd!=null) {
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(pd);
            }else {
                result.setStatus("410");
                result.setMsg("无该司机信息");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查询乘客所有订单
    public Result pas_history(Pas_dingdan pas_dingdan){
        Result result = new Result();
        result.setData(null);
        try{
            List<Pas_dingdan> pd = userMapper.findpas_history(pas_dingdan.getPas_id());//乘客订单
            if (pd!=null) {
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(pd);
            }else {
                result.setStatus("411");
                result.setMsg("暂无订单");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    //查询司机某一订单
    public Result pas_detail(Result_findcar result_findcar){
        Result result = new Result();
        result.setData(null);

        try{
            Pas_dingdan dd = userMapper.findpas_detail(result_findcar.getPas_id(),result_findcar.getTime());//乘客订单
            if (dd!=null) {
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(dd);
            }else {
                result.setStatus("410");
                result.setMsg("无此乘客订单");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
