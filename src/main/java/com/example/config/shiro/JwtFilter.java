package com.example.config.shiro;


import com.example.common.utils.LogUtils;
import com.example.common.utils.MD5Utils;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.R;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author 18237
 */
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {


    /**
     * 认证之前执行该方法
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = SecurityUtils.getSubject();
        return null != subject && subject.isAuthenticated();
    }

    /**
     * 认证未通过执行该方法
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //完成token登入
        //1.检查请求头中是否含有token
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String tokenStr = httpServletRequest.getHeader("Authorization");
        LogUtils.LOGGER.info("[{校验客户端请求是否携带token}]");
        //2. 如果客户端没有携带token，拦下请求
        if (null == tokenStr || "".equals(tokenStr)) {
            responseTokenError(response, "Token无效,您无权访问该接口");
            return false;
        }
        LogUtils.LOGGER.info("[{校验用户身份验证是否合法}]");
        //3.完成用户密钥和盐值加密校验
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        String password = activeUser.getUser().getPassword();
        String salt = activeUser.getUser().getSalt();
        String target = MD5Utils.md5Encryption(password + salt);
        String signKey = (String) httpServletRequest.getSession().getAttribute("signKey");
        if (!target.equals(signKey)) {
            return false;
        }
        //4.验证token
        Token token = new Token(tokenStr);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            LogUtils.LOGGER.info(e.getMessage());
            responseTokenError(response, e.getMessage());
            return false;
        }
        return true;


    }

    /**
     * 无需转发，直接返回Response信息 Token认证错误
     */
    private void responseTokenError(ServletResponse response, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter printOut = httpServletResponse.getWriter()) {
            HashMap<String, Object> errorData = new HashMap<>();
            errorData.put("errorCode", Assert.TOKEN.getCode());
            errorData.put("errorMsg", Assert.TOKEN.getMessage());
            R<HashMap<String, Object>> result = R.of(Assert.TOKEN, errorData);
            String data = new Gson().toJson(result);
            printOut.append(data);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.LOGGER.info(e.getMessage());
        }
    }
}
