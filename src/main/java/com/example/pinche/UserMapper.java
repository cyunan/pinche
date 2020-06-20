package com.example.pinche;


import com.example.pinche.bean.*;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * mapper的具体表达式
 */
@Component
@ConfigurationProperties(prefix = "limit")

@Mapper // 添加了@Mapper注解之后这个接口在编译时会生成相应的实现类
@Repository
public interface UserMapper {

    /**
     * identity
     * 查询用户名是否存在，若存在，不允许注册
     * 注解@Param(value) 若value与可变参数相同，注解可省略
     * 注解@Results  列名和字段名相同，注解可省略
     * @param phone
     * @return
     */
    @Select(value = "select * from identity u where u.phone=#{phone}")
    @Results
            ({@Result(property = "phone",column = "phone"),
                    @Result(property = "password",column = "password")})


    Identity findUserByName(@Param("phone") String phone);

    /**
     * identity
     * 忘记密码
     * 查询Identity表中数据
     * phone
     */
    @Select(value = "select * from identity u where u.phone=#{phone}")

    Identity findphone(@Param("phone") String phone);



    /**
     * identity
     * 登录
     * @param identity
     * @return
     *
     */
    @Select("select * from identity u where u.phone = #{phone} and password = #{password}")
    Identity login(Identity identity);

    /**
     * identity
     * 查询identity表中是否存在一个id_unique为？的数据
     */
    @Select(value = "select * from identity u where u.id_unique=#{id_unique}")
    Identity findUserByid_unique(@Param("id_unique") String id_unique);

    /**
     * identity
     * 查询identity表中所有用户信息
     */
    @Select(value = "select * from identity")
    List<Identity> find_all_identity();
    /**
     * identity
     * 查询identity表中所有乘客或司机信息
     */
    @Select(value = "select * from identity u where u.identity=#{identity}")
    List<Identity> find_all_identity_what(@Param("identity") int identity);


    /**
     * dri_dingdan
     * 查询dri_dingdan表中是否存在一个dri_id为?且status为?的数据
     */
    @Select(value = "select u.id,u.dri_id,u.path,u.time,u.site,u.pas_id,u.place,u.status,u.score,u.change_score,u.reason,u.pas_numbers,u.dri_number,u.dri_car,u.phone from dri_dingdan u " +
            "where u.dri_id=#{dri_id} and u.status=#{status}")

    Dri_dingdan find_dri_id(@Param("dri_id") String dri_id,@Param("status") int status);
//    @Delete(value = "delete from dri_dingdan where u.dri_id=#{dri_id}")
//
//    Dri_dingdan find_dri_id(@Param("dri_id") String dri_id);
    /**
     * dri_dingdan
     * 寻找dri_dingdan表中某一时间段所有已发车未完成的订单
     *
     */
    @Select(value = "select u.id,u.dri_id,u.path,u.time,u.site,u.pas_id,u.place,u.status,u.score,u.change_score,u.reason,u.pas_numbers,u.dri_number,u.dri_car,u.phone from dri_dingdan u " +
            "where u.path=#{path} and u.site>=#{site} and u.status=#{status} and time between #{time_start} AND #{time_end}")
    List<Dri_dingdan> find_car(@Param("path") int path, @Param("status") int status, @Param("time_start") Date time_start,@Param("time_end") Date time_end,@Param("site") int site);

    /**
     * dri_dingdan
     * 选择车辆
     * 相同
     */
    @Select(value = "select u.id,u.dri_id,u.path,u.time,u.site,u.pas_id,u.place,u.status,u.score,u.change_score,u.reason,u.pas_numbers,u.dri_number,u.dri_car,u.phone from dri_dingdan u "+
            "where u.dri_id=#{dri_id} and u.status=#{status}")
    Dri_dingdan select_car(@Param("dri_id") String dri_id,@Param("status") int status);

    /**
     * pas_dingdan
     * 查询pas_dingdan表中是否存在一个pas_id为?且status为?的数据
     */
    @Select(value = "select u.id,u.pas_id,u.path,u.time,u.site,u.dri_id,u.place,u.status,u.score,u.change_score,u.reason,u.dri_number,u.dri_car,u.phone,u.name from pas_dingdan u"+
            " where u.pas_id=#{pas_id} and u.status=#{status}")

    Pas_dingdan findpas_id(@Param("pas_id") String pas_id, @Param("status") int status);
    /**
     * pas_dingdan
     * 查询pas_dingdan表中是否存在一个dri_id为?的数据
     * 查询所有搭本趟车的乘客
     */
    @Select(value = "select u.id,u.pas_id,u.path,u.time,u.site,u.dri_id,u.place,u.status,u.score,u.change_score,u.reason,u.dri_number,u.dri_car,u.phone,u.name from pas_dingdan u"+
            " where u.dri_id=#{dri_id} and u.status=#{status}")
    List<Pas_dingdan> findpasdri_id(@Param("dri_id") String dri_id, @Param("status") int status);
    /**
     * pas_dingdan
     * 查询pas_dingdan表中是否存在一个pas_id为?的数据
     * 查询所有搭本趟车的乘客
     */
    @Select(value = "select u.id,u.pas_id,u.path,u.time,u.site,u.dri_id,u.place,u.status,u.score,u.change_score,u.reason,u.dri_number,u.dri_car,u.phone,u.name from pas_dingdan u"+
            " where u.pas_id=#{pas_id}")
    List<Pas_dingdan> findpas_history(@Param("pas_id") String pas_id);
    /**
     * dri_dingdan
     * 查询dri_dingdan表中是否存在一个dri_id为?的数据
     */
    @Select(value = "select u.id,u.dri_id,u.path,u.time,u.site,u.pas_id,u.place,u.status,u.score,u.change_score,u.reason,u.pas_numbers,u.dri_number,u.dri_car,u.phone from dri_dingdan u " +
            "where u.dri_id=#{dri_id}")

    List<Dri_dingdan> finddri_history(@Param("dri_id") String dri_id);
    /**
     * dri_dingdan
     * 查询dri_dingdan表中是否存在一个dri_id为?且time为？的数据
     */
    @Select(value = "select u.id,u.dri_id,u.path,u.time,u.site,u.pas_id,u.place,u.status,u.score,u.change_score,u.reason,u.pas_numbers,u.dri_number,u.dri_car,u.phone from dri_dingdan u " +
            "where u.dri_id=#{dri_id} and u.time=#{time}")

    Dri_dingdan finddri_detail(@Param("dri_id") String dri_id,@Param("time") Date time);
    /**
     * dri_dingdan
     * 查询dri_dingdan表中所有订单
     */
    @Select(value = "select * from dri_dingdan")
    List<Dri_dingdan> find_all_dri_dingdan();

    /**
     * pas_dingdan
     * 查询pas_dingdan表中是否存在一个pas_id为?且time为？的数据
     */
    @Select(value = "select * from pas_dingdan u where u.pas_id=#{pas_id} and u.time=#{time}")

    Pas_dingdan findpas_detail(@Param("pas_id") String pas_id,@Param("time") Date time);
    /**
     * Pas_dingdan
     * 查询Pas_dingdan表中所有订单
     */
    @Select(value = "select * from pas_dingdan")

    List<Pas_dingdan> find_all_pas_dingdan();
    /**
     * Feedback
     * 查询Feedback表中是否存在一个id_unique为?的数据
     */
    @Select("select * from feedback u where u.id_unique=#{id_unique}")
    List<Feedback> findfeedback(@Param("id_unique") String id_unique);
    /**
     * Feedback
     * 查询Feedback表中是否存在一个id_unique为?的数据
     */
    @Select("select * from feedback u where u.id_unique=#{id_unique} and u.time=#{time}")
    Feedback findfeedback_detail(@Param("id_unique") String id_unique,@Param("time") Date time);

    /**
     * Transit
     * 查询Transit表中所有待审核的数据
     */
    @Select("select * from transit u where u.state=#{state}")
    List<Transit> find_checkdriver(@Param("state") int state);

    /**
     * Transit
     * 查询Transit表中是否存在一个id_unique为?的数据
     */
    @Select("select * from transit u where u.id_unique=#{id_unique}")
    Transit checkdriver(@Param("id_unique") String id_unique);

    /**
     * Transit
     * 查询Transit表中是否存在一个id_unique为?的数据
     */
    @Select("select * from transit u")
    List<Transit> find_checkdriver_all();
}
