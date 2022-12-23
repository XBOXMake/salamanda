package moe.salamanda.salamanda.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

@Service
public class CryptService {
    //DES
    private final static String THE_FUCKING_KEY = "TWO_YOUNG_GIRLS_EXPLORE_A_SHATTERED_WORLD_FILL_WITH_SOUND_A_PAST_TO_BE_ANSWERED__";
    public static String encrypt(final String content,final String key){
        try{
            String realKey = THE_FUCKING_KEY + key;
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(realKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,secretKey,random);
            return new String (cipher.doFinal());
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public static String decrypt(final String content,final String key){
        try{
            String realKey = THE_FUCKING_KEY + key;
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(realKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,secretKey,random);
            return new String(cipher.doFinal(content.getBytes()));
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
}
