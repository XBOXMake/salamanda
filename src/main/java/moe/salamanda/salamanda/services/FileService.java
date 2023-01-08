package moe.salamanda.salamanda.services;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    public String getSavePath(){
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        return applicationHome.getDir().getAbsolutePath();
    }

    public void responseIdentifyImg(BufferedImage image, HttpServletResponse response){
        response.setContentType("image/png");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expire",0);
        try{
            ImageIO.write(image,"png",response.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void responseIdentifyImg(File imageFile, HttpServletResponse response){
        try{
            BufferedImage image = ImageIO.read(imageFile);
            response.setContentType("image/png");
            response.setHeader("Pragma","No-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setDateHeader("Expire",0);
            ImageIO.write(image,"png",response.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
