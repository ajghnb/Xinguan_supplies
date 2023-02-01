package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;


/**
 * @author 18237
 */
@Configuration
@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
public class FdfsConfiguration {

}
