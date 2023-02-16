package com.example.controller.system;

import com.example.annotation.LimitRequest;
import com.example.common.utils.LogUtils;
import com.example.common.utils.RedisUtil;
import com.example.model.po.VerifyCode;
import com.example.service.system.IVerifyCodeGen;
import com.example.service.system.impl.SimpleCharVerifyCodeGenImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author 18237
 */

@Api(tags = "系统模块-随机验证码相关接口")
@RestController
@RequestMapping("/system/verify")
public class VerifyCodeApi {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 图片验证码(限制为8秒内最多能请求四次)
     *
     * @param response
     * @return
     */
    @LimitRequest(count = 3, time = 8000)
    @ApiOperation(value = "验证码")
    @GetMapping("/code")
    public void verifyCode(HttpServletResponse response) {
        //删除过时的验证码
        Object pastCode = redisUtil.get("verifyCode");
        if (pastCode != null) {
            redisUtil.delete("verifyCode");
        }
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            // 设置图片长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            LogUtils.LOGGER.debug("随机生成验证码: verifyCode:{}", code);
            // verifyCode存入redis缓存中,并设置过期时间
            redisUtil.set("verifyCode", verifyCode, verifyCode.getExpireDate().getTime());
            // 设置响应头信息
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            // 在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            // 设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            LogUtils.LOGGER.debug("随机生成验证码异常: 验证码:{}", e);
        }
    }
}

