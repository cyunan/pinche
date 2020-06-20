package com.example.pinche.initiate;

import com.example.pinche.File_init;
import com.example.pinche.UserMapper;
import com.example.pinche.bean.Feedback;
import com.example.pinche.mapper.Feedback_repository;
import com.example.pinche.result.Result;
import com.example.pinche.result.Result_findcar;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Feedback_initiate {
    @Autowired
    private Feedback_repository fr;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private File_init fi;
    private String  url;


    //用户反馈
//    public Result feedback(MultipartFile file, String detail, HttpServletRequest request,String id_unique) throws FileNotFoundException {
    public Result feedback(MultipartFile file,String detail,String id_unique) throws FileNotFoundException {
        Result result = new Result();
        //判断文件是否为空
        if (file.isEmpty()) {//android判断
            result.setStatus("413");
            result.setMsg("文件为空");
            return result;
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
//        System.out.print("上传的文件名为: "+fileName+"\n");
        //加个时间戳，尽量避免文件名称重复，保存的文件名为: ""+fileName
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        /**
         * 存jar包内部
         * 存在开发时target下的目录
         * String path = ResourceUtils.getURL("classpath:").getPath()+"image_file" +System.getProperty("file.separator")+fileName;
         * url = "image_file"+System.getProperty("file.separator")+fileName;
         */
//        File.separator获取默认文件系统的分隔符



        /**
         * 存项目在容器中实际发布运行的根路径
         * // 项目在容器中实际发布运行的根路径
         *         String realPath = request.getSession().getServletContext().getRealPath("/");
         *         // 设置存放图片文件的路径
         *         String path = realPath+"image_file\\"+fileName;
         */
        String path = fi.getUploadFolder()+fileName;
        url  = fi.getStaticAccessPath()+fileName;

        System.out.println("绝对路径: "+path);
        System.out.println("url: "+url);
        //创建文件路径
        File dest = new File(path);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            //上传文件
            file.transferTo(dest); //保存文件
            Feedback fb = new Feedback();
            fb.setId_unique(id_unique);//用户id
            fb.setDetail(detail);//反馈信息
            fb.setUrl(url);//可直接访问
            fb.setPath(path);//真实路径
            fb.setTime(new Date());//上传时间

            fr.save(fb);
            result.setStatus("204");
            result.setMsg("上传成功");

        } catch (IOException e) {
            result.setStatus("412");
            result.setMsg(e.toString());
//            result.setMsg("上传失败");
        } catch (Exception e) {
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

//用户查询自己的反馈信息
    public Result<List<Feedback>> feedback_history(String id_unique){
        Result<List<Feedback>> result = new Result<>();
        result.setData(null);

        try{
            List<Feedback> fd = userMapper.findfeedback(id_unique);
            if (fd!=null){
                result.setStatus("204");
                result.setMsg("查询成功");
                result.setData(fd);
            }else {
                result.setStatus("410");
                result.setMsg("暂无反馈");
            }
        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    //某一反馈查询
    public Result<List<Feedback>> feedback_detail(String id_unique,String time){
        Result result = new Result();
        result.setData(null);
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(time);
            Feedback fd = userMapper.findfeedback_detail(id_unique,date);
            if (fd!=null){
                result.setStatus("204");
                result.setMsg("查询成功");
                result.setData(fd);
            }else {
                result.setStatus("411");
                result.setMsg("该反馈不存在");
            }

        }catch (Exception e){
            result.setStatus("404");
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }



}
