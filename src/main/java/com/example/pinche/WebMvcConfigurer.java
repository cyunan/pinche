package com.example.pinche;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfigurer  extends WebMvcConfigurerAdapter {
//    @Value("${file.staticAccessPath}")
//    private String imagepath;//图片虚拟路径
//    @Value("${file.uploadFolder}")
//    private String ture_imagepath;//图片真实路径
//    @Value("${file.linux_imagepath}")
//    private String linux_imagepath;//图片真实路径
    @Autowired
    private File_init fileInit;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8888/image_file/20200311102230_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20190607124441.png
        //其中image_file表示访问的前缀。"file:D:\\pinche\\target\\classes\\image_file\\"是文件真实的存储路径
//        registry.addResourceHandler("/image_file/**").addResourceLocations("file:D:\\pinche\\target\\classes\\image_file\\");
        //Windows下
        registry.addResourceHandler("/image_file/**").addResourceLocations("file:"+ fileInit.getUploadFolder());

        registry.addResourceHandler("/touxiang/**").addResourceLocations("file:"+ fileInit.getUploadFolder_touxiang());

        registry.addResourceHandler("/pas_card/**").addResourceLocations("file:"+ fileInit.getUploadFolder_pascard());

        registry.addResourceHandler("/driver/**").addResourceLocations("file:"+ fileInit.getUploadFolder_driver());

        super.addResourceHandlers(registry);
    }
}
