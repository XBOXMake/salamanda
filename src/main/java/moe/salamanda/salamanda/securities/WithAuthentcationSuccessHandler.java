package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        Cookie usernameCookie = new Cookie("username",request.getParameter("username").trim());
        usernameCookie.setHttpOnly(false);
        usernameCookie.setSecure(false);
        if(!ObjectUtils.isEmpty(request.getParameter("remember-me"))) usernameCookie.setMaxAge(60*60*24);
        Cookie attributeCookie = new Cookie("attribute",request.getParameter("attribute").trim());
        attributeCookie.setHttpOnly(false);
        attributeCookie.setSecure(false);
        if(!ObjectUtils.isEmpty(request.getParameter("remember-me"))) attributeCookie.setMaxAge(60*60*24);
        response.addCookie(usernameCookie);
        response.addCookie(attributeCookie);
        response.setContentType("application/json;charset-UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("message","登陆成功");
        result.put("status",200);
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
}
