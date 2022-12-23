package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.salamanda.salamanda.services.RandomService;
import moe.salamanda.salamanda.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WithAuthentcationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        String cookieCode = redisService.getCrypt();
        redisService.add(redisService.DEFAULT_USERNAME_PREFIX+cookieCode,request.getParameter("username").trim());
        redisService.add(redisService.DEFAULT_ATTRIBUTE_PREFIX+cookieCode,request.getParameter("attribute").trim());
        redisService.add(cookieCode,"true");
        Cookie cookie = new Cookie("LOG",cookieCode);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        if(!ObjectUtils.isEmpty(request.getParameter("remember-me"))) {
            redisService.setExpire(redisService.DEFAULT_USERNAME_PREFIX+cookieCode, redisService.EXPIRE_TIME,redisService.EXPIRE_TIME_TYPE);
            redisService.setExpire(redisService.DEFAULT_ATTRIBUTE_PREFIX+cookieCode, redisService.EXPIRE_TIME,redisService.EXPIRE_TIME_TYPE);
            redisService.setExpire(cookieCode, redisService.EXPIRE_TIME,redisService.EXPIRE_TIME_TYPE);
            cookie.setMaxAge(60 * 60 * 24);
        }
        response.addCookie(cookie);
        response.setContentType("application/json;charset-UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("message","登陆成功");
        result.put("status",200);
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
}
