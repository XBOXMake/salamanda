package moe.salamanda.salamanda.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WithAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        System.out.println("failed");
        Map<String,Object> result = new HashMap<>();
        result.put("message","failure: "+exception.getMessage());
        result.put("status",500);
        response.setContentType("application/json;charset=UTF-8");
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
}
