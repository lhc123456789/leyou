package com.leyou.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.config.UploadProperties;
import com.leyou.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private UploadProperties properties;

    //private static final List<String> allowTypes= Arrays.asList("image/jpeg","image/png","image/bmp");

//    public String uploadImage(MultipartFile file) {
//        try {
//            //校验文件类型
//            String contentType = file.getContentType();
//            if(!properties.getAllowTypes().contains(contentType)){
//                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
//            }
//            //校验文件内容
//            BufferedImage image = ImageIO.read(file.getInputStream());
//            if(image==null){
//                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
//            }
//
//            //上传到OSS
//            String extension= StringUtils.substringAfter(file.getOriginalFilename(),".");//获取文件后缀名
//            String yourObjectName=properties.getBaseUrl()+file.getOriginalFilename()+extension;//全路径
//            // 创建OSSClient实例
//            OSSClient ossClient=new OSSClient(properties.getEndpoint(), properties.getAccessKeyId(), properties.getAccessKeySecret());
//            //OSS ossClient= new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKeyId(), properties.getAccessKeySecret());
//            // 创建PutObjectRequest对象。
//            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(),file.getOriginalFilename(), (File) file);
//            ossClient.putObject(putObjectRequest);
//            ossClient.shutdown();
//            return yourObjectName;
//
//            //准备目标路径
//            //File dest = new File("D:\\workspace-ideal\\yun\\code\\leyou\\upload", file.getOriginalFilename());
//            //保存文件到本地
//            //file.transferTo(dest);
//            //返回路径
//            //return "http://image.leyou.com"+file.getOriginalFilename();
//        } catch (IOException e) {
//            //上传文件失败
//            log.error("上传文件失败", e);
//            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
//        }
//    }
    public String uploadImage(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();
            if(!properties.getAllowTypes().contains(contentType)){
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image==null){
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //上传到OSS
            String url = OssUtil.upload(file, OssUtil.FileDirType.ARTICLE_IMAGES);
            System.out.println("返回地址:" + url);
            return url;
        } catch (IOException e) {
            //上传文件失败
           log.error("上传文件失败", e);
           throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}
