package moe.salamanda.salamanda.securities;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class WithWebAuthenticationDetails extends WebAuthenticationDetails {
    private final Integer attribute;
    public WithWebAuthenticationDetails(HttpServletRequest request){
        super(request);
        attribute = Integer.parseInt(request.getParameter("attribute"));
    }
    public Integer getAttribute(){return attribute;}

    @Override
    public String toString(){
        return super.toString() + "; attribute: " + this.getAttribute();
    }
}
