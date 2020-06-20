package com.example.pinche.controler;

import com.aliyuncs.exceptions.ClientException;
import com.example.pinche.bean.Pas_dingdan;
import com.example.pinche.initiate.*;
import com.example.pinche.result.Result;
import com.example.pinche.bean.Dri_dingdan;
import com.example.pinche.bean.Identity;

import com.example.pinche.result.Result_findcar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Date;

@RestController
public class Controll {


    /**
     * 注册
     */
//    @PostMapping("/register")
//    public Identity register(@RequestParam("phone") String phone,
//                             @RequestParam("password") String password){
//
////        Identity identity = new Identity();
////        identity.setPhone(phone);
////        identity.setPassword(password);
////        repository.save(identity);
////        return repository.save(identity);
//    }
    @Autowired
    private Reg_Login rl;//注册登录封装类


    @Autowired
    private Dri_initiate di;//司机操作封装类

    @Autowired
    private Pas_initiate pi;//司机操作封装类
    @Autowired
    private Feedback_initiate fi;//司机操作封装类

    @Autowired
    private Findmessage fm;//查看信息封装类

    //注册
    @PostMapping("/register")
    public Result register(Identity identity) {
        return rl.register(identity);
    }

    //登录
    @PostMapping("/login")
    public Result login(Identity identity) {
        return rl.login(identity);
    }

    //查看申请司机身份审核结果
    @PostMapping("/check")
    public Result check(@RequestParam("id_unique") String id_unique) {
        return rl.check(id_unique);
    }

    //完善信息
    @PostMapping("/add")
    public Result add(@RequestParam("id_unique") String id_unique, @RequestParam("identity") int identity,@RequestParam("name") String name,
                      @RequestParam("pas_id") String pas_id, @RequestParam("pas_class") String pas_class,
                      @RequestParam("sex") String sex, @RequestParam("emergency_phone") String emergency_phone,
                     @RequestParam("image_file") MultipartFile file) {
        return rl.add(id_unique,identity,name,pas_id,pas_class,sex,emergency_phone,file);
    }
    //修改头像
    @PostMapping("/touxiang")
    public Result touxiang(@RequestParam("image_file") MultipartFile file, @RequestParam("id_unique") String id_unique) throws FileNotFoundException {
        return rl.touxiang(file,id_unique);
    }
    //查询用户信息
    @PostMapping("/pas_select")
    public Result select(String id_unique) {
        return rl.select(id_unique);
    }
    //添加司机信息(完善)
    @PostMapping("/dri_complete")
    public Result dri_complete(Identity identity) {
        return rl.dri_complete(identity);
    }
    //添加司机信息(审核)
    @PostMapping("/add_driver")
    public Result add_driver(@RequestParam("driver_licence") MultipartFile driver_licence,
                             @RequestParam("idcard_obverse") MultipartFile idcard_obverse,
                             @RequestParam("idcard_reverse") MultipartFile idcard_reverse,
                             @RequestParam("id_unique") String id_unique,@RequestParam("dri_car") String dri_car,
                             @RequestParam("dri_number") String dri_number,@RequestParam("name") String name,
                             @RequestParam("id_card") String id_card) {
        return rl.add_driver(driver_licence,idcard_obverse,idcard_reverse,id_unique,dri_car,dri_number,name,id_card);
    }
    //查看已审核未审核所有司机信息
    @PostMapping("/query_driver_all")
    public Result query_driver_all() {
        return rl.query_driver_all();
    }
    //查看待审核司机信息
    @PostMapping("/query_driver")
    public Result query_driver() {
        return rl.query_driver();
    }
    //审核某一司机信息
    @PostMapping("/check_driver")
    public Result check_driver(@RequestParam("id_unique") String id_unique,@RequestParam("state") int state) {
        return rl.check_driver(id_unique,state);
    }
    //忘记密码
    @PostMapping("/forget")
    public Result forget(Identity identity) {
        return rl.forget(identity);
    }

    //司机发布订单信息
    @PostMapping("/initiate")
    public Result initiate(Dri_dingdan driDingdan) {
        return di.initiate(driDingdan);
    }

    //司机取消订单
    @PostMapping("/dri_delete")
    public Result dri_delete(Dri_dingdan driDingdan) {
        return di.dri_delete(driDingdan);
    }

    //查询司机未完成订单
    @PostMapping("/dri_query")
    public Result dri_query(Dri_dingdan driDingdan) {
        return di.dri_query(driDingdan);
    }

    //查询车辆
    @PostMapping("/find_car")
    public Result find_car(Result_findcar result_findcar) {
        return pi.find_car(result_findcar);
    }

    //选择车辆
    @PostMapping("/select_car")
    public Result select_car(Result_findcar result_findcar) {
        return pi.select_car(result_findcar);
    }

    //乘客取消订单
    @PostMapping("/pas_delete")//todo 超出发车时间无法取消实现
    public Result pas_delete(Result_findcar rf) {
        return pi.delete(rf);
    }
    //查询乘客未完成订单
    @PostMapping("/pas_query")
    public Result pas_query(String pas_id) {
        return pi.query(pas_id);
    }
    //司机完成订单
    @PostMapping("/dri_finish")
    public Result finish(Dri_dingdan dri_dingdan) {
        return di.finish(dri_dingdan);
    }
    //乘客查询司机详细信息
    @PostMapping("/query_dri")
    public Result query_dri(String dri_id) {
        return pi.query_dri(dri_id);
    }
    //司机查询乘客详细信息
    @PostMapping("/query_pas")
    public Result query_pas(String dri_id) {
        return di.query_pas(dri_id);
    }
    //查询乘客所有订单
    @PostMapping("/pas_history")
    public Result pas_history(Pas_dingdan pas_dingdan) {
        return pi.pas_history(pas_dingdan);
    }
    //查询司机所有订单
    @PostMapping("/dri_history")
    public Result dri_history(Dri_dingdan dri_dingdan) {
        return di.dri_history(dri_dingdan);
    }
    //查询司机某一订单
    @PostMapping("/dri_detail")
    public Result dri_detail(Result_findcar result_findcar) {
        return di.dri_detail(result_findcar);
    }
    //查询乘客某一订单
    @PostMapping("/pas_detail")
    public Result pas_detail(Result_findcar result_findcar) {
        return pi.pas_detail(result_findcar);
    }
    //意见反馈
    @PostMapping("/feedback")
    public Result feedback(@RequestParam("image_file") MultipartFile file, @RequestParam("detail") String detail, @RequestParam("id_unique") String id_unique) throws FileNotFoundException {
        return fi.feedback(file,detail,id_unique);
    }
    //历史反馈
    @PostMapping("/feedback_history")
    public Result feedback_history(@RequestParam("id_unique") String id_unique)  {
        return fi.feedback_history(id_unique);
    }
    //单个意见反馈查询
    @PostMapping("/feedback_detail")
    public Result feedback_detail(@RequestParam("id_unique") String id_unique,@RequestParam("time") String time)  {
        return fi.feedback_detail(id_unique,time);
    }
    //查询所有用户信息
    @PostMapping("/find_all_identity")
    public Result find_all_identity()  {
        return fm.find_all_identity();
    }
    //查询所有司机信息
    @PostMapping("/find_all_dri")
    public Result find_all_dri()  {
        return fm.find_all_dri();
    }
    //查询所有乘客信息
    @PostMapping("/find_all_pas")
    public Result find_all_pas()  {
        return fm.find_all_pas();
    }
    //查询所有乘客订单信息
    @PostMapping("/find_all_dri_dingdan")
    public Result find_all_dri_dingdan()  {
        return fm.find_all_dri_dingdan();
    }
    //查询所有乘客订单信息
    @PostMapping("/find_all_pas_dingdan")
    public Result find_all_pas_dingdan()  {
        return fm.find_all_pas_dingdan();
    }

    //测试
    @PostMapping("/test")
    public Result test() throws ClientException {
        return rl.checkSucces();
    }
}
