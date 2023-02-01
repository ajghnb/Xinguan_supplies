package com.example;


import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * @author 18237
 */
@SpringBootApplication
@Import(FdfsClientConfig.class)
@MapperScan(basePackages = "com.example.dao")
public class XinguanSuppliesApplication {

    public static void main(String[] args) {
        SpringApplication.run(XinguanSuppliesApplication.class, args);
    }


}
