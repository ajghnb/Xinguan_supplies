package com.example.config.shiro;


import com.example.common.utils.LogUtils;
import com.example.common.utils.MD5Utils;
import com.example.common.utils.RedisUtil;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.R;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

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
        //3.验证token,执行登录
        Token token = new Token(tokenStr);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            LogUtils.LOGGER.info(e.getMessage());
            responseTokenError(response, e.getMessage());
            return false;
        }
        //校验用户身份信息
        LogUtils.LOGGER.info("[{校验用户身份验证是否合法}]");
        //4.完成用户鉴权信息校验
        if (httpServletRequest.getRequestURI().equals("login")) {
            ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
            String password = activeUser.getUser().getPassword();
            String salt = activeUser.getUser().getSalt();
            String activeKey = MD5Utils.md5Encryption(password + salt);
            String signKey = (String) httpServletRequest.getSession()
                    .getAttribute("signKey");
            //若身份信息不匹配返回false
            if (!activeKey.equals(signKey)) {
                return false;
            }
        }
        //匹配则返回true
        return true;
    }

    /**
     * 对spring-boot整合shiro出现的跨域提供支持,此时全局配置跨域不生效
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //响应头的设置
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        //httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 跨域时会首先发送一个option请求,这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
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
