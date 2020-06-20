package com.example.pinche.initiate;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.example.pinche.File_init;
import com.example.pinche.UserMapper;
import com.example.pinche.bean.Identity;
import com.example.pinche.bean.Transit;
import com.example.pinche.mapper.Identity_repository;
import com.example.pinche.mapper.Transit_repository;
import com.example.pinche.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service

public class Reg_Login {
    /**
     * 注册-登录-完善信息封装
     */
    @Autowired
    private File_init fi;
    @Autowired
    private Identity_repository identity_repository;
    @Autowired
    private Transit_repository transit_repository;

    @Autowired
    private UserMapper userMapper;

    private Result result;

    //注册
    public Result<Object> register(Identity identity) {//todo 可能造成id_unique重名
        Result<Object> result = new Result<>();
        result.setData(null);
        try {
            Identity existUser = userMapper.findUserByName(identity.getPhone());
            if (existUser != null) {
                //如果用户手机号存在
                result.setStatus("401");
                result.setMsg("用户已存在");
                result.setData(existUser);
            } else {
//                userMapper.regist(identity);
                identity.setScore(650);//信誉积分
                String a = identity.getPhone();//读取手机号
                String id = 1 + a.substring(a.length() - 6);//1+截取手机号后六位
                identity.setId_unique(id);
                identity.setUrl("http://114.55.249.135:8888/touxiang/a.png");
                identity_repository.save(identity);
                result.setStatus("201");
                result.setMsg("注册成功");
                result.setData(identity);
            }
        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    //登录
    public Result<Identity> login(Identity identity) {
        Result<Identity> result = new Result<Identity>();
        result.setData(null);
        try {
            Identity existUser = userMapper.login(identity);
            if (existUser == null) {
                result.setStatus("405");
                result.setMsg("用户名或密码错误");
            } else {
                result.setStatus("203");
                result.setMsg("登录成功");
                result.setData(existUser);
            }
        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //完善数据
    public Result add(@RequestParam("id_unique") String id_unique, @RequestParam("identity") int identity, @RequestParam("name") String name,
                      @RequestParam("pas_id") String pas_id, @RequestParam("pas_class") String pas_class,
                      @RequestParam("sex") String sex, @RequestParam("emergency_phone") String emergency_phone,
                      @RequestParam("image_file") MultipartFile file) {
        Result result = new Result();
        result.setData(null);
        try {
            //查询数据
            Identity existUser = userMapper.findUserByid_unique(id_unique);
//        identity_repository.flush();
            if (existUser != null) {//如果有内容
                if (existUser.getIdentity() == 1) {//如果本来已进行完善司机信息，identity就一直为1（即司机态）
                    identity = 1;
                }


                // 获取文件名
                String fileName = file.getOriginalFilename();
                //加个时间戳，尽量避免文件名称重复，保存的文件名为: ""+fileName
                fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
                String path = fi.getUploadFolder_pascard() + fileName;
                String url = fi.getStaticAccessPath_pascard() + fileName;
                //创建文件路径
                File dest = new File(path);
                //判断文件父目录是否存在
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();
                }
                //上传文件
                file.transferTo(dest); //保存文件
                //保存数据

//            Identity identity1 = new Identity();
//            identity1.setIs_add(true);
//            identity1.setId(existUser.getId());
//            identity1.setPassword(existUser.getPassword());
//            identity1.setPhone(existUser.getPhone());
//            identity1.setScore(existUser.getScore());
//            identity1.setStatus(existUser.getStatus());
//            identity1.setDri_number(existUser.getDri_number());
//            identity1.setIdcard(existUser.getIdcard());
//            identity1.setDri_car(existUser.getDri_car());
//            identity1.setUrl(existUser.getUrl());
                existUser.setIdentity(identity);
                existUser.setName(name);
                existUser.setPas_id(pas_id);
                existUser.setPas_class(pas_class);
                existUser.setSex(sex);
                existUser.setEmergency_phone(emergency_phone);
                existUser.setPascard_url(url);
                identity_repository.save(existUser);
                result.setStatus("202");
                result.setMsg("完善成功");

            } else {
                result.setStatus("403");
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //添加司机信息
    public Result dri_complete(Identity identity) {
        Result result = new Result();
        result.setData(null);
        try {
            //查询数据
            Identity existUser = userMapper.findUserByid_unique(identity.getId_unique());
//        identity_repository.flush();
            if (existUser != null) {//如果有内容
                identity.setId(existUser.getId());
                identity.setIdentity(1);
                identity.setEmergency_phone(existUser.getEmergency_phone());
                identity.setSex(existUser.getSex());
                identity.setIs_add(existUser.isIs_add());
                identity.setPas_id(existUser.getPas_id());
                identity.setPas_class(existUser.getPas_class());
                identity.setPassword(existUser.getPassword());
                identity.setPhone(existUser.getPhone());
                identity.setScore(existUser.getScore());
                identity.setStatus(existUser.getStatus());
                identity_repository.save(identity);//todo identifier of an instance of com.example.pinche.bean.Identity was altered from 1 to 2
                result.setStatus("204");
                result.setMsg("司机完善成功");
            } else {
                result.setStatus("403");
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查询用户信息
    public Result<Identity> select(String id_unique) {
        Result<Identity> result = new Result<Identity>();
        result.setData(null);
        try {
            Identity existUser = userMapper.findUserByid_unique(id_unique);
            if (existUser != null) {//如果有内容
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(existUser);
            } else {
                result.setStatus("420");
                result.setMsg("请求失败");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //修改密码(忘记密码)
    public Result<Identity> forget(Identity identity) {
        Result result = new Result();
        result.setData(null);
        try {
            Identity existUser = userMapper.findphone(identity.getPhone());
            if (existUser != null) {//如果有内容
                existUser.setPassword(identity.getPassword());
                identity_repository.save(existUser);
                result.setStatus("204");
                result.setMsg("请求成功");

            } else {
                result.setStatus("420");
                result.setMsg("该用户尚未注册");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //修改用户头像
    public Result<Identity> touxiang(MultipartFile file, String id_unique) {
        Result result = new Result();
        result.setData(null);

        try {
            Identity existUser = userMapper.findUserByid_unique(id_unique);
            if (existUser != null) {//如果有内容
                // 获取文件名
                String fileName = file.getOriginalFilename();
                //加个时间戳，尽量避免文件名称重复，保存的文件名为: ""+fileName
                fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;

                /**
                 *
                 */
                String path = fi.getUploadFolder_touxiang() + fileName;
                String url = fi.getStaticAccessPath_touxiang() + fileName;
                //创建文件路径
                File dest = new File(path);
                //判断文件父目录是否存在
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();
                }
                //上传文件
                file.transferTo(dest); //保存文件
                //保存数据
                existUser.setUrl(url);
                identity_repository.save(existUser);
                result.setStatus("204");
                result.setMsg("上传成功");
                result.setData(existUser);

            } else {
                result.setStatus("420");
                result.setMsg("该用户尚未注册");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //添加用户司机审核数据

    /**
     * 将数据存入一个中介表中
     * @param driver_licence 驾驶证
     * @param idcard_obverse 身份证正面
     * @param idcard_reverse 身份证反面
     * @param id_unique 用户id
     * @param dri_car 车辆信息
     * @param dri_number 车牌号
     * @param name 姓名
     * @param id_card 身份证号码
     * @return
     */
    public Result add_driver(MultipartFile driver_licence, MultipartFile idcard_obverse, MultipartFile idcard_reverse,
                             String id_unique, String dri_car, String dri_number, String name, String id_card) {
        Result result = new Result();
        result.setData(null);

        try {
            /**
             * 获取文件名
             */
            String fileName1 = driver_licence.getOriginalFilename();//
            //加个时间戳，尽量避免文件名称重复，保存的文件名为: ""+fileName
            fileName1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName1;
            String fileName2 = driver_licence.getOriginalFilename();
            //加个时间戳，尽量避免文件名称重复，保存的文件名为: ""+fileName
            fileName2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName2;
            String fileName3 = driver_licence.getOriginalFilename();
            //加个时间戳，尽量避免文件名称重复，保存的文件名为: ""+fileName
            fileName3 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName3;

            /**
             *获取目录和可直接访问的url
             */
            String path1 = fi.getUploadFolder_driver() + fileName1;
            String url1 = fi.getStaticAccessPath_driver() + fileName1;

            String path2 = fi.getUploadFolder_driver() + fileName2;
            String url2 = fi.getStaticAccessPath_driver() + fileName2;

            String path3 = fi.getUploadFolder_driver() + fileName3;
            String url3 = fi.getStaticAccessPath_driver() + fileName3;
            /**
             * 创建文件路径
             */

            File dest1 = new File(path1);
            File dest2 = new File(path2);
            File dest3 = new File(path3);
            //判断文件父目录是否存在
            if (!dest1.getParentFile().exists()) {
                dest1.getParentFile().mkdir();
            }
            if (!dest2.getParentFile().exists()) {
                dest2.getParentFile().mkdir();
            }
            if (!dest3.getParentFile().exists()) {
                dest3.getParentFile().mkdir();
            }
            //上传文件
            driver_licence.transferTo(dest1); //保存文件
            idcard_obverse.transferTo(dest2);
            idcard_reverse.transferTo(dest3);
            //保存数据
            Transit transit = new Transit();
            transit.setId_unique(id_unique);//用户id
            transit.setDri_car(dri_car);//车辆信息
            transit.setDri_number(dri_number);//车牌号
            transit.setName(name);//真实姓名
            transit.setId_card(id_card);//身份证号码
            transit.setUrl_driver_licence(url1);//驾驶证
            transit.setUrl_idcard_obverse(url2);//身份证正反面
            transit.setUrl_idcard_reverse(url3);
            Identity identity = userMapper.findUserByid_unique(id_unique);
            if (identity!=null){
                savexinxi(transit,identity);
                result.setStatus("204");
                result.setMsg("上传成功");
            }else {
                result.setStatus("420");
                result.setMsg("该用户id已过期（id_unique不存在）");
            }


        } catch (IOException e) {
            result.setStatus("410");
            result.setMsg("上传失败");
            e.printStackTrace();
        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    @Transactional//要么都成功，要么都失败
    public void savexinxi(Transit transit, Identity identity) {
        transit_repository.save(transit);
        identity.setCheck_driver(1);//待审核
        identity_repository.save(identity);
    }

    //查找所有待审核的司机数据
    public Result<Transit> query_driver() {
        Result result = new Result();
        result.setData(null);
        try {
            List<Transit> existUser = userMapper.find_checkdriver(0);
            if (existUser != null) {//如果有内容
                result.setStatus("204");
                result.setMsg("查询成功");
                result.setData(existUser);

            } else {
                result.setStatus("422");
                result.setMsg("暂无待审核的数据");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    //审核司机信息
    public Result<Transit> check_driver(String id_unique,int state) {
        Result result = new Result();
        result.setData(null);
        try {
            Transit existUser = userMapper.checkdriver(id_unique);
            Identity identity = userMapper.findUserByid_unique(id_unique);
            if (existUser != null) {//如果有内容
                existUser.setState(state);
                savemessage(existUser,identity,state);
                result.setStatus("204");
                result.setMsg("审核操作成功");

            } else {
                result.setStatus("423");
                result.setMsg("该审核信息已过期");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    @Transactional//要么都成功，要么都失败
    public void savemessage(Transit transit, Identity identity, int state) {
        transit_repository.save(transit);
        if (state == 1){//审核通过
            identity.setDri_car(transit.getDri_car());
            identity.setDri_number(transit.getDri_number());
            identity.setName(transit.getName());
            identity.setIdcard(transit.getId_card());
            identity.setCheck_driver(2);//2是通过标志
            identity.setIdentity(1);//获得司机身份
        }else if (state == 2){//审核失败
            identity.setCheck_driver(3);//3是拒绝标志
        }
        identity_repository.save(identity);
    }
    //查找所有已审核未审核的司机数据
    public Result<Transit> query_driver_all() {
        Result result = new Result();
        result.setData(null);
        try {
            List<Transit> existUser = userMapper.find_checkdriver_all();
            if (existUser != null) {//如果有内容
                result.setStatus("204");
                result.setMsg("查询成功");
                result.setData(existUser);

            } else {
                result.setStatus("422");
                result.setMsg("暂无数据");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //检查用户身份
    public Result<Identity> check(String id_unique) {
        Result result = new Result();
        result.setData(null);
        try {
            Identity existUser = userMapper.findUserByid_unique(id_unique);
            if (existUser != null) {//如果有内容
                result.setStatus("204");
                result.setMsg("查询成功");
                result.setData(existUser.getCheck_driver());

            } else {
                result.setStatus("420");
                result.setMsg("该用户信息已过期");
            }

        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Result checkSucces() {
        Result result = new Result();
        result.setData(null);
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4Fy2afHXho9ZZomgkVtx", "R8lOYCdN4myCUkmrzOpZilOFfG9ngZ");
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushRequest pushRequest = new PushRequest();
        // 推送目标
        pushRequest.setAppKey((long) 29672318);
//        pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
//        pushRequest.setTargetValue(deviceIds); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setTarget("ALL"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALL: 推送给全部
        pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
        pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
        // 推送配置
        pushRequest.setTitle("新的审核信息"); // 消息的标题
        pushRequest.setBody("202"); // 消息的内容
        // 推送配置: Android
        pushRequest.setAndroidNotifyType("NONE");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        pushRequest.setAndroidOpenType("URL"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
        pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
        pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
        pushRequest.setAndroidMusic("default"); // Android通知音乐
        pushRequest.setAndroidPopupActivity("com.ali.demo.PopupActivity");//设置该参数后启动辅助弹窗功能, 此处指定通知点击后跳转的Activity（辅助弹窗的前提条件：1. 集成第三方辅助通道；2. StoreOffline参数设为true）
        pushRequest.setAndroidPopupTitle("Popup Title");
        pushRequest.setAndroidPopupBody("Popup Body");
        pushRequest.setAndroidExtParameters("{\"k1\":\"android\",\"k2\":\"v2\"}"); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
        // 推送控制
        Date pushDate = new Date(System.currentTimeMillis()) ; // 30秒之间的时间点, 也可以设置成你指定固定时间
        String pushTime = ParameterHelper.getISO8601Time(pushDate);
        pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
        pushRequest.setExpireTime(expireTime);
        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
        PushResponse pushResponse = null;
        try {
            pushResponse = client.getAcsResponse(pushRequest);
            System.out.printf("RequestId: %s, MessageID: %s\n",
                    pushResponse.getRequestId(), pushResponse.getMessageId());
            result.setStatus("202");
            result.setMsg("审核成功");
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return result;

    }

}
