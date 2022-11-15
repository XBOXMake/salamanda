package moe.salamanda.salamanda.securities;

import moe.salamanda.salamanda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WithAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private UserService userService;
    public void setUserService(UserService userService){this.userService=userService;}
    public UserService getUserService(){return this.userService;}

    PasswordEncoder passwordEncoder;
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){this.passwordEncoder=passwordEncoder;}
    public PasswordEncoder getPasswordEncoder(){return passwordEncoder;}

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,UsernamePasswordAuthenticationToken token) throws AuthenticationException{
        if(token.getCredentials().equals(null)){
            throw new BadCredentialsException("There is none");
        }
        else{
            String password=token.getCredentials().toString();
            //decrypt
            //there is none
            if(!getPasswordEncoder().matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("What the fuck is going on with that?");
            }
        }
    }

    public WithAuthenticationProvider(UserService userService){
        super();
        setUserService(userService);
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser;
        if(authentication instanceof WithAuthenticationToken){
            WithAuthenticationToken authenticationToken = (WithAuthenticationToken) authentication;
            loadedUser = getUserService().loadUserByUsernameAndAttribute(username, authenticationToken.getAttribute());
        }
        else {
            loadedUser = getUserService().loadUserByUsername(username);
        }
        if(loadedUser.equals(null)){
            throw new AuthenticationServiceException("WHat the fuck?It is null!!!");
        }
        return loadedUser;
    }
}
