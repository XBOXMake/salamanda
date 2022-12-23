package moe.salamanda.salamanda.services;


import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Random;

@Service
public class RandomService {
    private static Random random = new Random();
    protected static char getRandomChar(){
        int number = random.nextInt(36);
        char character = (char) (number<10?'0'+number:'A'+number-10);
        return character;
    }
    protected static char getRandomChar(boolean DigitOrCharater){
        return (char) (DigitOrCharater==true?'0'+random.nextInt(10):'A'+random.nextInt(26));
    }

    public static String getCyrptedCookie(){
        String str = new String();
        for(int i=1;i<=32;i++){
            str += getRandomChar(false);
        }
        return str;
    }

    public static String password(){
        String password = "";
        for(int i=1;i<=16;i++){
            password += getRandomChar();
        }
        return password;
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

    public static int getRandomInt(int bound){
        return random.nextInt(bound+1);
    }

    public static int getRandomPosNeg(){
        return random.nextInt(114)>57?1:-1;
    }

    public static Color getRandomColor(){
        return new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }
    public static Color getRandomColor(int bound){
        bound = bound>256?256:bound<0?1:bound;
        return new Color(random.nextInt(bound),random.nextInt(bound),random.nextInt(bound));
    }
    public static Color getRandomColor(int boundA,int boundB,int boundC){
        boundA = boundA>256?256:boundA<0?1:boundA;
        boundB = boundB>256?256:boundB<0?1:boundB;
        boundC = boundC>256?256:boundC<0?1:boundC;
        return new Color(random.nextInt(boundA),random.nextInt(boundB),random.nextInt(boundC));
    }
}
