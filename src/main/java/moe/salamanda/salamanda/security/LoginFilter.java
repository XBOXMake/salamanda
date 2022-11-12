package moe.salamanda.salamanda.security;
/*
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String defaultFilterProcessesUrl){
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        String method = request.getMethod();
        if("post".equalsIgnoreCase(request.getMethod())){
            try {
                throw new AuthenticationException("What the fuck you do with: "+request.getMethod());
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String attribute = request.getParameter("attribute");
        username = username==null?"":username.trim();
        password = password==null?"":password.trim();

    }

}
*/
