package moe.salamanda.salamanda.controllers;

import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.services.FileService;
import moe.salamanda.salamanda.services.RedisService;
import moe.salamanda.salamanda.services.ResponseService;
import moe.salamanda.salamanda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    FileService fileService;
    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;

    @PostMapping("/util/upload")
    public void upload(@RequestBody MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(file == null){
            ResponseService.failureResponse(request,response,"failed");
            return;
        }
        String path = fileService.getSavePath()+"\\resources\\";
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        File folder = new File(path+"users\\"+username+"\\");
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = "thumbnail"+oldName.substring(oldName.lastIndexOf('.'));
        try{
            file.transferTo(new File(folder,newName));
            WithUser user = userService.getUserByUsername(username);
            user.setThumbnail(new File(folder,newName));
            userService.insertUser(user);
            ResponseService.successResponse(request,response,"success");
        }
        catch (IOException e){
            e.printStackTrace();
            ResponseService.failureResponse(request,response,"failed");
        }
    }

    @GetMapping("/util/getThumbnail")
    public void getThumbnail(HttpServletRequest request, HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        String path = fileService.getSavePath()+"\\resources\\"+"users\\"+username+"\\";
        String fileName = "thumbnail.png";
        File file = new File(path,fileName);
        if(!file.exists()){
            String defaultPath = fileService.getSavePath()+"\\resources\\"+"default\\";
            file = new File(defaultPath,fileName);
        }
        fileService.responseIdentifyImg(file,response);
    }

}
