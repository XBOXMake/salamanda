package moe.salamanda.salamanda.securities;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class WithAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Integer attribute;
    public WithAuthenticationToken(Object a, Object b, Integer attribute){
        super(a,b);
        this.attribute = attribute;
    }

    public Integer getAttribute() {
        return attribute;
    }
}
