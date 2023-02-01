package com.example.common.utils;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 18237
 */
public class MD5Utils {

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};


    /**
     * 密码加密
     *
     * @return
     */
    public static String md5Encryption(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.limitedStackTrace(e);
        }
        return str;
    }


    /**
     * 文件获取MD5大写
     *
     * @param file
     * @return
     */
    public static String getMD5MakeLower(File file) {
        String value = null;

        try (FileInputStream in = new FileInputStream(file)) {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);

        } catch (Exception e) {
            LogUtils.limitedStackTrace(e);
        }
        return value;
    }


    public static String getMD5(byte[] source) {
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            // MD5 的计算结果是一个 128 位的长整数
            byte[] tmp = md.digest();
            // 用字节表示就是 16 个字节,每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
            char[] str = new char[16 * 2];
            // 表示转换结果中对应的字符位置
            int k = 0;
            // 从第一个字节开始，对 MD5 的每一个字节
            for (int i = 0; i < 16; i++) {
                // 转换成 16 进制字符的转换, 取第 i 个字节
                byte byte0 = tmp[i];
                // 取字节中高 4 位的数字转换,
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                // >>> 为逻辑右移，将符号位一起右移,取字节中低 4 位的数字转换
                str[k++] = hexDigits[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.limitedStackTrace(e);
        }
        return s;
    }


    /**
     * 文件获取MD5小写
     *
     * @param upload
     * @return
     * @throws Exception
     */
    public static String getMD5LowerStr(MultipartFile upload) throws Exception {
        byte[] uploadBytes = upload.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(uploadBytes);
        String hashString = new BigInteger(1, digest).toString(16);
        return hashString.toUpperCase();
    }
}
