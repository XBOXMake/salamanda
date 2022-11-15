package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WithAuthentcationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        System.out.println("succeeded");
        response.setContentType("application/json;charset-UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("message","登陆成功");
        result.put("status",200);
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
}
