package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WithAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        System.out.println("succeeded");
        Map<String,Object> result = new HashMap<>();
        result.put("message","success");
        result.put("status",200);
        response.setContentType("application/json;charset=UTF-8");
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
}
