package moe.salamanda.salamanda.services;

import moe.salamanda.salamanda.models.general.AuthenticationCode;
import moe.salamanda.salamanda.repositories.general.AuthenticationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class AuthCodeService {
    @Autowired
    private AuthenticationCodeRepository repository;
    public static String codeGenerator(){
        StringBuilder code = new StringBuilder("$114514:");
        for(int i=1;i<12;i++) code.append(RandomService.getRandomChar());
        return code.toString();
    }

    public boolean checkAuthCode(String authCode){
        AuthenticationCode authenticationCode = repository.findByAuthCode(authCode);
        if(ObjectUtils.isEmpty(authenticationCode)) throw new RuntimeException("没有该授权码");
        if(authenticationCode.isOccupied()) throw new RuntimeException("授权码已被使用");
        return true;
    }

    public void useAuthCode(String authcode,String username){
        AuthenticationCode authenticationCode = repository.findByAuthCode(authcode);
        if(ObjectUtils.isEmpty(authenticationCode)) throw new RuntimeException("没有该授权码");
        if(authenticationCode.isOccupied()) throw new RuntimeException("授权码已被使用");
        authenticationCode.getUsed(username);
        repository.save(authenticationCode);
    }

    public AuthenticationCode createAuthCode(){
        AuthenticationCode authenticationCode = new AuthenticationCode();
        repository.save(authenticationCode);
        return authenticationCode;
    }
}
