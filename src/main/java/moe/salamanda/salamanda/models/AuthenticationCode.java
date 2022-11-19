package moe.salamanda.salamanda.models;

import lombok.Data;
import moe.salamanda.salamanda.services.AuthCodeService;

import javax.persistence.*;

@Data
@Entity
@Table(name = "authcode")
public class AuthenticationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authcode")
    private String authenticationCode;
    private boolean isUsed;
    private String isUsedBy;

    private void generateCode(){
        authenticationCode = AuthCodeService.codeGenerator();
    }

    public void getUsed(String username){
        isUsed=true;
        isUsedBy=username;
    }

    public AuthenticationCode(){
        generateCode();
        this.isUsed=false;
        this.isUsedBy=null;
    }

    public boolean isOccupied(){
        return isUsed;
    }

}
