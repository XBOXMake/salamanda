package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.salamanda.salamanda.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WithLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    RedisService redisService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        try{
            String cookieCode = WebUtils.getCookie(request,"LOG").getValue();
            Cookie cookie = new Cookie("LOG", cookieCode);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            redisService.delete(redisService.DEFAULT_ATTRIBUTE_PREFIX+cookieCode);
            redisService.delete(redisService.DEFAULT_USERNAME_PREFIX+cookieCode);
            redisService.delete(cookieCode);
            response.sendRedirect("/signout.html");
        }catch (Exception e){
            if(e.getClass().equals(IOException.class)||e.getClass().equals(ServletException.class)) throw e;
            else response.sendRedirect("/login.html");
        }
    }
}
