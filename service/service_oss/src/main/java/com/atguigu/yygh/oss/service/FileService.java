package com.atguigu.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile; /**
 * @author wang
 * @create 2022-05-30
 */
public interface FileService {
    String upload(MultipartFile file);
}
