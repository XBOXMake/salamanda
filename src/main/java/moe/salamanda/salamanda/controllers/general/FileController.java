package moe.salamanda.salamanda.controllers.general;

import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Teacher;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import moe.salamanda.salamanda.repositories.teacher.TeacherRepository;
import moe.salamanda.salamanda.services.FileService;
import moe.salamanda.salamanda.services.RedisService;
import moe.salamanda.salamanda.services.ResponseService;
import moe.salamanda.salamanda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;


    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/util/upload")//头像
    public void upload(@RequestBody MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(file == null){
            ResponseService.failureResponse(request,response,"failed");
            return;
        }
        String path = fileService.getSavePath()+"/resources/";
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        File folder = new File(path+"users/"+username+"/");
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

    @PostMapping("/util/upload-image")//照片
    public void uploadImage(@RequestBody MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception{
        if(file == null){
            ResponseService.failureResponse(request,response,"failed");
            return;
        }
        String path = fileService.getSavePath()+"/resources/";
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        String attribute = redisService.get(redisService.DEFAULT_ATTRIBUTE_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        File folder = new File(path+"users/"+username+"/");
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = "image"+oldName.substring(oldName.lastIndexOf('.'));
        try{
            file.transferTo(new File(folder,newName));
            if(WithUser.getRoleName(attribute).equals("student")){
                Student user = studentRepository.findByUsername(username);
                user.setImage(new File(folder,newName));
                studentRepository.save(user);
            }
            else if(WithUser.getRoleName(attribute).equals("teacher")){
                Teacher user = teacherRepository.findByUsername(username);
                user.setImage(new File(folder,newName));
                teacherRepository.save(user);
            }
            else {
                ResponseService.response(response,request,1);
                return;
            }
            ResponseService.response(response,request,114);
        }
        catch (IOException e){
            e.printStackTrace();
            ResponseService.response(response,request,1);
        }
    }

    @GetMapping("/util/getThumbnail")
    public void getThumbnail(HttpServletRequest request, HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        String path = fileService.getSavePath()+"/resources/"+"users/"+username+"/";
        System.out.println(path);
        String fileName = "thumbnail.png";
        File file = new File(path,fileName);
        if(!file.exists()){
            String defaultPath = fileService.getSavePath()+"/resources/"+"default/";
            file = new File(defaultPath,fileName);
        }
        fileService.responseIdentifyImg(file,response);
    }

    @GetMapping("/util/getImage")
    public void getImage(HttpServletRequest request, HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        String attribute = redisService.get(redisService.DEFAULT_ATTRIBUTE_PREFIX+WebUtils.getCookie(request,"LOG").getValue());
        File file = null;
        if(WithUser.getRoleName(attribute).equals("student")){
            System.out.println("student");
            Student user = studentRepository.findByUsername(username);
            file = user.getImage();
        }
        else if(WithUser.getRoleName(attribute).equals("teacher")){
            Teacher user = teacherRepository.findByUsername(username);
            file = user.getImage();
        }
        if (file == null){
            String defaultPath = fileService.getSavePath()+"/resources/"+"default/";
            file = new File(defaultPath,"image.png");
        }
        fileService.responseIdentifyImg(file,response);
    }

    @GetMapping("/util/getThumbnail-chose")
    public void getThumbnail(@RequestParam("username") String username,HttpServletRequest request,HttpServletResponse response){
        String path = fileService.getSavePath()+"/resources/"+"users/"+username+"/";
        String fileName = "thumbnail.png";
        File file = new File(path,fileName);
        if(!file.exists()){
            String defaultPath = fileService.getSavePath()+"/resources/"+"default/";
            file = new File(defaultPath,fileName);
        }
        fileService.responseIdentifyImg(file,response);
    }

    @GetMapping("/util/getImage-chose")
    public void getImage(@RequestParam("username") String username,HttpServletRequest request,HttpServletResponse response){
        String path = fileService.getSavePath()+"/resources/"+"users/"+username+"/";
        String fileName = "image.png";
        File file = new File(path,fileName);
        if(!file.exists()){
            String defaultPath = fileService.getSavePath()+"/resources/"+"default/";
            file = new File(defaultPath,fileName);
        }
        fileService.responseIdentifyImg(file,response);
    }

}
