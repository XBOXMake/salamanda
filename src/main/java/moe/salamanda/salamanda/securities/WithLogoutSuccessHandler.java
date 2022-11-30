package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        try{
            Cookie usernameCookie = new Cookie("username", WebUtils.getCookie(request, "username").getValue());
            usernameCookie.setHttpOnly(false);
            usernameCookie.setSecure(false);
            usernameCookie.setMaxAge(0);
            Cookie attributeCookie = new Cookie("attribute", WebUtils.getCookie(request, "attribute").getValue());
            attributeCookie.setHttpOnly(false);
            attributeCookie.setSecure(false);
            attributeCookie.setMaxAge(0);
            response.addCookie(usernameCookie);
            response.addCookie(attributeCookie);
            response.sendRedirect("/signout.html");
        }catch (Exception e){
            if(e.getClass().equals(IOException.class)||e.getClass().equals(ServletException.class)) throw e;
            else response.sendRedirect("/login.html");
        }
    }
}
