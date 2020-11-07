package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 09:46
 * @Description: 静态资源上传模板类
 */
@Data
@ToString
public class OSSTemplate {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String bucketDomain;

    public String upload(InputStream inputStream, String fileName) {
        try {
            //1.创建上传文件目录名称    yyyy-mm-dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String folderName = sdf.format(new Date());
            //2.修改文件名称
            fileName = UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
            //3.执行上传
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            ossClient.putObject(bucketName, "pic/" + folderName + "/" + fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            inputStream.close();
            //4.返回文件路径
            //https://scw20201023-lhq.oss-cn-beijing.aliyuncs.com/pic/test.jpg
            return "https://" + bucketDomain + "/pic/" + folderName + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
