package moe.salamanda.salamanda.services;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class CheckcodeService {
    private int defaultHeight =25;
    private int defaultWidth = 95;
    private int defaultLineSize = 40;
    private int defaultFontSize = 18;

    private Font getFont(){
        return new Font("Fixoedsys",Font.CENTER_BASELINE,defaultFontSize);
    }
    private Font getFont(int fontSize){
        return new Font("Fixoedsys",Font.CENTER_BASELINE,fontSize);
    }
    private Color getColor(){
        return RandomService.getRandomColor();
    }

    public String getCheckcode(){
        return RandomService.checkCode(5);
    }

    private void getLine(Graphics graphics){
        int x = RandomService.getRandomInt(defaultWidth);
        int y = RandomService.getRandomInt(defaultHeight);
        int xx = RandomService.getRandomInt(defaultWidth*2+defaultWidth/3);
        int yy = RandomService.getRandomInt(defaultHeight*3+defaultHeight/4);
        graphics.drawLine(x,y,xx,yy);
    }

    private void getLine(Graphics graphics,int width,int height){
        int x = RandomService.getRandomInt(width);
        int y = RandomService.getRandomInt(height);
        int xx = RandomService.getRandomInt(width*2+width/3);
        int yy = RandomService.getRandomInt(height*3+height/4);
        graphics.drawLine(x,y,xx,yy);
    }

    private void getString(Graphics graphics,String checkcode){
        for(int i=0;i<checkcode.length();i++){
            graphics.setFont(getFont());
            graphics.setColor(getColor());
            graphics.translate(RandomService.getRandomInt(defaultFontSize/2),RandomService.getRandomInt(defaultFontSize/5)*RandomService.getRandomPosNeg());
            graphics.drawString(String.valueOf(checkcode.charAt(i)),defaultFontSize*i-defaultFontSize*i/4+defaultFontSize,defaultFontSize);
        }
    }

    private void getString(Graphics graphics,String checkcode,int fontSize){
        for(int i=0;i<checkcode.length();i++){
            graphics.setFont(getFont(fontSize));
            graphics.setColor(getColor());
            graphics.translate(RandomService.getRandomInt(fontSize/2),RandomService.getRandomInt(fontSize/5)*RandomService.getRandomPosNeg());
            graphics.drawString(String.valueOf(checkcode.charAt(i)),fontSize*i-fontSize*i/4+fontSize,fontSize);
        }
    }

    public BufferedImage getCheckcodeImage(String checkcode){
        BufferedImage image = new BufferedImage(defaultWidth,defaultHeight,BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.getGraphics();
        graphics.fillRect(0,0,defaultWidth,defaultHeight);
        graphics.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,defaultFontSize));
        graphics.setColor(getColor());
        for (int i=0;i<=defaultLineSize;i++){
            getLine(graphics);
        }
        getString(graphics,checkcode);
        graphics.dispose();
        return image;
    }

    private int getFontSize(int width,int height){
        return Math.min(height*2-height*2/3-height/2,width*2/5-width*2/3/5-width/2/5);
    }

    public void setDefaultLineSize(int lineSize){
        defaultLineSize=lineSize;
    }

    public BufferedImage getCheckcodeImage(String checkcode,int width,int height){
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.getGraphics();
        graphics.fillRect(0,0,width,height);
        graphics.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,getFontSize(width,height)));
        for (int i=0;i<=defaultLineSize;i++){
            graphics.setColor(getColor());
            getLine(graphics,width,height);
        }
        getString(graphics,checkcode,getFontSize(width,height));
        graphics.dispose();
        return image;
    }

    public void responseIdentifyImg(BufferedImage image, HttpServletResponse response){
        response.setContentType("image/jpeg");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expire",0);
        try{
            ImageIO.write(image,"JPEG",response.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
