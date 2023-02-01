package com.example.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author 18237
 */
@Data
public class VerifyCode {

    private String code;

    private byte[] imgBytes;

    private Date expireDate;
}