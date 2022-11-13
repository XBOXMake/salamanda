package moe.salamanda.salamanda.services;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomService {
    private static Random random = new Random();
    private static char getRandomChar(){
        int number = random.nextInt(36);
        char character = (char) (number<10?'0'+number:'A'+number-10);
        return character;
    }
    public static String checkCode(int bound){
        String str = new String();
        for(int i=1;i<=bound;i++){
            str+=getRandomChar();
        }
        return str;
    }
    public static String checkCode(){
        String str = new String();
        for(int i=1;i<=6;i++){
            str+=getRandomChar();
        }
        return str;
    }
}
