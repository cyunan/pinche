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
import com.sun.org.apache.xpath.internal.objects.XNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class Dri_initiate {
    /**
     * 向多个表存数据需要多对多
     */
    @Autowired
    private Identity_repository identity_repository;
    @Autowired
    private Dri_repository dingdan_repository;
    @Autowired
    private Pas_repository pas_repository;

    @Autowired
    private UserMapper userMapper;
    private Result result;
    //todo 考虑要不要给订单一个id
    //司机发布信息
    public Result initiate(Dri_dingdan dd){
        Result result = new Result();
        result.setData(null);
        try{
            //查询数据

            Identity existUser = userMapper.findUserByid_unique(dd.getDri_id());
            if (existUser != null&&existUser.getStatus()==0){//如果有内容且无正在进行的订单

                    start(existUser,dd);
                    result.setStatus("204");
                    result.setMsg("发车成功");
            }else {
                result.setStatus("406");
                result.setMsg("存在未完成的订单");

            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 事务 指数据库事务
     */
    @Transactional//要么都成功，要么都失败
    public void start(Identity existUser,Dri_dingdan dd){
        int scroe;
        //把Identity表中的status值改为1
        existUser.setStatus(1);
        scroe = existUser.getScore();
        identity_repository.save(existUser);

        //将发车数据存进Dri_dingdan中
        dd.setStatus(1);
        dd.setScore(scroe);
        dd.setDri_number(existUser.getDri_number());//司机的车牌号
        dd.setDri_car(existUser.getDri_car());//车辆信息
        dd.setPhone(existUser.getPhone());
        dingdan_repository.save(dd);
    }


    //司机取消订单
    public Result dri_delete(Dri_dingdan dd){
        Result result = new Result();
        result.setData(null);

        try{
            /**
             * （没乘客）
             * identity(司机)和dri_dingdan表的status参数变为0（完成态）
             * （如果有乘客）
             * identity(司机)和dri_dingdan表的status参数变为0（完成态）,信誉积分score减10分,原因reason为取消订单（已有乘客取消发车订单）
             * //todo 以后改为已有乘客且在发车前1小时内取消发车订单
             * identity(乘客)和pas_dingdan表的status参数变为0（完成态）
             */

            int score;
            /**
             * 修改dri_dingdan表中的数据
             */
            Dri_dingdan existUser_dri = userMapper.find_dri_id(dd.getDri_id(),1);//获取司机订单信息
            if (existUser_dri!=null) {
                score = existUser_dri.getScore();
                existUser_dri.setStatus(0);

                //判断该订单是否有乘客
                if (existUser_dri.getPas_numbers() != 0) {//已有乘客，信誉积分-10
                    existUser_dri.setScore(score - 10);//当前信誉积分
                    existUser_dri.setChange_score("-10");
                    existUser_dri.setReason("取消订单（已有乘客）");

                    r = userMapper.findpasdri_id(dd.getDri_id(), 1);  //查询pas_dingdan中司机id为？且未完成的订单
                }
                /**
                 * identity表中司机的数据
                 */
                //得到表中id_unique为?的数据
                Identity identity_dri = userMapper.findUserByid_unique(dd.getDri_id());
                identity_dri.setStatus(0);
                identity_dri.setScore(score);
                //事件处理
                delete(existUser_dri, identity_dri);
                result.setStatus("204");
                result.setMsg("取消成功");
            }else {
                result.setStatus("407");
                result.setMsg("无订单取消");
            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }



    @Transactional//要么都成功，要么都失败
    public void delete(Dri_dingdan dd, Identity identity_dri){
        dingdan_repository.save(dd);
        identity_repository.save(identity_dri);

        if (r!=null){
            for (int i = 0; i < r.size(); i++) {
                Pas_dingdan pd = r.get(i);
                pd.setReason("司机取消订单");
                pd.setStatus(0);
                pd.setChange_score("+0");
                pas_repository.save(pd);

                Identity identity_pas = userMapper.findUserByid_unique(pd.getPas_id());
                identity_pas.setStatus(0);
                identity_repository.save(identity_pas);
            }
        }
    }



    //查询司机未完成订单
    public Result dri_query(Dri_dingdan dd){
        Result result = new Result();
        result.setData(null);
        try{

            Dri_dingdan existUser2 = userMapper.find_dri_id(dd.getDri_id(),1);
            if(existUser2!=null){
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(existUser2);
            }else {
                result.setStatus("408");
                result.setMsg("无未完成的订单");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    //司机查询乘客数据
    public Result query_pas(String dri_id){
        Result result = new Result();
        result.setData(null);
        try{

            List<Pas_dingdan>  pp= userMapper.findpasdri_id(dri_id,1);
            if(pp!=null){
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(pp);
            }else {
                result.setStatus("410");
                result.setMsg("暂无乘客");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //司机完成订单
    public Result finish(Dri_dingdan dd){//todo 目前进度
        Result result = new Result();
        try{
            /**
             * （默认有乘客）
             * identity(司机)和dri_dingdan表的status参数变为0（完成态）,信誉积分score加5分,原因reason为完成订单
             * identity(乘客)和pas_dingdan表的status参数变为0（完成态）,信誉积分score加5分,原因reason为订单已完成
             */

            Dri_dingdan existUser_dri = userMapper.find_dri_id(dd.getDri_id(),1);//获取司机订单信息
            if (existUser_dri!=null) {
                existUser_dri.setStatus(0);

                existUser_dri.setScore(existUser_dri.getScore() + 5);//当前信誉积分
                existUser_dri.setChange_score("+5");
                existUser_dri.setReason("完成订单");

                /**
                 * 查询pas_dingdan中该趟车的所有乘客
                 */
                r = userMapper.findpasdri_id(dd.getDri_id(), 1);  //查询pas_dingdan中司机id为？且未完成的订单

                /**
                 * identity表中司机的数据
                 */
                //得到表中id_unique为?的数据
                Identity identity_dri = userMapper.findUserByid_unique(dd.getDri_id());
                identity_dri.setStatus(0);
                identity_dri.setScore(existUser_dri.getScore());
                //事件处理
                finish_dingdan(existUser_dri, identity_dri);
                result.setStatus("204");
                result.setMsg("取消成功");
            }else {
                result.setStatus("407");
                result.setMsg("无订单取消");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    @Transactional//要么都成功，要么都失败
    public void finish_dingdan(Dri_dingdan dd, Identity identity_dri){
        dingdan_repository.save(dd);
        identity_repository.save(identity_dri);

        if (r!=null){
            for (int i = 0; i < r.size(); i++) {
                Pas_dingdan pd = r.get(i);
                pd.setReason("订单已完成");
                pd.setStatus(0);
                pd.setChange_score("+5");
                pd.setScore(pd.getScore()+5);
                pas_repository.save(pd);

                Identity identity_pas = userMapper.findUserByid_unique(pd.getPas_id());
                identity_pas.setStatus(0);
                identity_pas.setScore(pd.getScore());
                identity_repository.save(identity_pas);
            }
        }
    }

    //查询司机所有订单
    public Result dri_history(Dri_dingdan dri_dingdan){
        Result result = new Result();
        result.setData(null);
        try{
            List<Dri_dingdan> dd = userMapper.finddri_history(dri_dingdan.getDri_id());//乘客订单
            if (dd!=null) {
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(dd);
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
    public Result dri_detail(Result_findcar result_findcar){
        Result result = new Result();
        result.setData(null);

        try{
            Dri_dingdan dd = userMapper.finddri_detail(result_findcar.getDri_id(),result_findcar.getTime());//乘客订单
            if (dd!=null) {
                result.setStatus("204");
                result.setMsg("请求成功");
                result.setData(dd);
            }else {
                result.setStatus("410");
                result.setMsg("无此司机订单");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    List<Pas_dingdan> r = new List<Pas_dingdan>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Pas_dingdan> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Pas_dingdan pas_dingdan) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Pas_dingdan> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Pas_dingdan> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public Pas_dingdan get(int index) {
            return null;
        }

        @Override
        public Pas_dingdan set(int index, Pas_dingdan element) {
            return null;
        }

        @Override
        public void add(int index, Pas_dingdan element) {

        }

        @Override
        public Pas_dingdan remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Pas_dingdan> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Pas_dingdan> listIterator(int index) {
            return null;
        }

        @Override
        public List<Pas_dingdan> subList(int fromIndex, int toIndex) {
            return null;
        }
    };


}
