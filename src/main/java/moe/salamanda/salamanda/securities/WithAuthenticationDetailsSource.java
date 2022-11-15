package moe.salamanda.salamanda.securities;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//unused
@Component
public class WithAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest,WithWebAuthenticationDetails>{
    @Override
    public WithWebAuthenticationDetails buildDetails(HttpServletRequest request){
        return new WithWebAuthenticationDetails(request);
    }
}
