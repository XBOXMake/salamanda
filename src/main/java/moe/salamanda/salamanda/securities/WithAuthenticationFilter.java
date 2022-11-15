package moe.salamanda.salamanda.securities;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WithAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public WithAuthenticationFilter(){
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        if(!request.getParameter("attribute").equals(null)){
            UsernamePasswordAuthenticationToken authRequest;
            String username = request.getParameter("username").trim();
            System.out.println("username:"+username);
            String password = request.getParameter("password").trim();
            System.out.println("password:"+password);
            String attribute = request.getParameter("attribute").trim();
            System.out.println("attribute:"+attribute);
            authRequest = new WithAuthenticationToken(username,password,Integer.parseInt(attribute));
            super.setDetails(request,authRequest);
            return super.getAuthenticationManager().authenticate(authRequest);
        }
        else return super.attemptAuthentication(request,response);
    }
}
