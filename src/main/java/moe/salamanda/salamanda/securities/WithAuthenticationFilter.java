package moe.salamanda.salamanda.securities;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WithAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public WithAuthenticationFilter(){
        super();
    }

    public WithAuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, RememberMeServices rememberMeServices){
        super();
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failureHandler);
        super.setRememberMeServices(rememberMeServices);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        if(!request.getParameter("attribute").equals(null)){
            UsernamePasswordAuthenticationToken authRequest;
            String username = request.getParameter("username").trim();
            String password = request.getParameter("password").trim();
            String attribute = request.getParameter("attribute").trim();
            authRequest = new WithAuthenticationToken(username,password,Integer.parseInt(attribute));
            super.setDetails(request,authRequest);
            return super.getAuthenticationManager().authenticate(authRequest);
        }
        else return super.attemptAuthentication(request,response);
    }
}
