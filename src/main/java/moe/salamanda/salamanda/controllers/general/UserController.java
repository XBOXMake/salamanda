package moe.salamanda.salamanda.controllers.general;

import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Teacher;
import moe.salamanda.salamanda.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.lang.String;


@Controller
public class UserController {
    private final static String IDENTITY_CODE_KEY="LOLOLOLOLOLOLOLOLOLOLOLOLOLOLOL";

    @Autowired
    private UserService userService;
    @Autowired
    private CheckcodeService checkcodeService;
    @Autowired
    private AuthCodeService authCodeService;
    @Autowired
    private FileService fileService;


    private boolean checkcodeCheck(HttpSession session,String checkcode,String email){
        String code = session.getAttribute("checkcode").toString();
        String mail = session.getAttribute("email").toString();
        String realMail = CryptService.decrypt(code,email);
        String realCode = CryptService.decrypt(mail,checkcode);
        if(realCode.equals(checkcode)) {
            if(email.equals(realMail)){
                return true;
            }
            throw new RuntimeException("注册邮箱与先前申请验证码的邮箱不一致");
        }
        throw new RuntimeException("验证码不一致");
    }

    private boolean noRepeatCheck(String username,String email){
        WithUser user = userService.getUserByUsername(username);
        if(ObjectUtils.isEmpty(user)) {
            user = userService.getUserByEmail(email);
            if(ObjectUtils.isEmpty(user)){
                return true;
            }
            throw new RuntimeException("该邮箱有绑定的账号");
        }
        throw new RuntimeException("已存在该用户名");
    }

    private boolean authCheck(String authcode,String atrribute){
        if(atrribute == "3") return authCodeService.checkAuthCode(authcode);
        else return true;
    }

    @RequestMapping("/register")
    @ResponseBody
    public void register(HttpServletRequest request, HttpServletResponse response, @RequestParam("authcode")String authcode, @RequestParam("selection")String attribute, @RequestParam("firstname")String firstname, @RequestParam("lastname")String lastname, @RequestParam("name")String name, @RequestParam("password")String password, @RequestParam("email")String email, @RequestParam("checkcode")String checkcode) throws Exception{
        try {
            name=name.toLowerCase();
            email=email.toLowerCase();
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (checkcodeCheck(session, checkcode, email)) {
                    if (noRepeatCheck(name,email)) {
                        if(authCheck(authcode,attribute)){
                            WithUser user = new WithUser();
                            user.setAttribute(Integer.parseInt(attribute));
                            user.setFirstName(firstname);
                            user.setLastName(lastname);
                            user.setUsername(name);
                            user.setEmail(email);
                            user.setPassword(password);
                            if (attribute.equals("1")) {
                                userService.insertStudent(new Student(user));
                                System.out.println(new Student(user));
                            }
                            else if(attribute.equals("2")) userService.insertTeacher(new Teacher(user));
                            else userService.insertUser(user);
                            if(attribute == "3")authCodeService.useAuthCode(authcode,name);
                            ResponseService.successResponse(request, response, "注册成功");
                            return;
                        }
                    }
                }
            }
            throw new RuntimeException("无法接触到当前的SESSION信息");
        }catch (Exception e) {
            ResponseService.failureResponse(request, response, e);
        }
    }

    @RequestMapping("/check-code")
    @ResponseBody
    public void checkCode(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "address") String address) throws Exception{
        try{
            String code = RandomService.checkCode();
            MailService.codeSender(address, code);
            System.out.println(code);
            System.out.println(address);
            session.setAttribute("checkcode", CryptService.encrypt(address,code));
            session.setAttribute("email",CryptService.encrypt(code,address));
            ResponseService.successResponse(request, response, "成功发送验证码");
        }catch (Exception e){
            ResponseService.failureResponse(request,response,e);
        }
    }

    @RequestMapping("/forget")
    @ResponseBody
    public void forget(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "address") String address) throws Exception{
        try{
            address=address.toLowerCase();
            WithUser user = userService.getUserByEmail(address);
            if(!ObjectUtils.isEmpty(user)){
                String password = RandomService.password();
                userService.resetPassword(address,password);
                MailService.passwordSender(address,password);
                ResponseService.successResponse(request, response, "成功发送");
            }
            else throw new RuntimeException("该邮箱无绑定的账号");
        }catch (Exception e){
            ResponseService.failureResponse(request,response,e);
        }
    }

    @RequestMapping("/student-checkcode")
    @ResponseBody
    public void studentCheckcodeImage(HttpServletResponse response,HttpSession session){
        String checkcode = checkcodeService.getCheckcode();
        session.setAttribute("identityCode-student",CryptService.encrypt(checkcode,IDENTITY_CODE_KEY));
        BufferedImage image=checkcodeService.getCheckcodeImage(checkcode,150,50);
        fileService.responseIdentifyImg(image,response);
    }
    @RequestMapping("/teacher-checkcode")
    @ResponseBody
    public void teacherCheckcodeImage(HttpServletResponse response,HttpSession session){
        String checkcode = checkcodeService.getCheckcode();
        session.setAttribute("identityCode-teacher",CryptService.encrypt(checkcode,IDENTITY_CODE_KEY));
        BufferedImage image=checkcodeService.getCheckcodeImage(checkcode,150,50);
        fileService.responseIdentifyImg(image,response);
    }

    private boolean identitycodeCheckMethod(HttpSession session,String identitycode,String attribute){
        String code = CryptService.decrypt(session.getAttribute("identityCode-"+attribute).toString(),IDENTITY_CODE_KEY);
        if(code.toUpperCase().equals(identitycode.toUpperCase())) {
            return true;
        }
        else throw new RuntimeException("验证码不一致");
    }


    @RequestMapping("/identitycode-check")
    @ResponseBody
    public void identitycodeCheck(@RequestParam("identitycode") String identitycode,@RequestParam("attr") String attribute, HttpServletRequest request,HttpServletResponse response) throws Exception{
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (identitycodeCheckMethod(session,identitycode,attribute)) {
                    ResponseService.successResponse(request,response,"验证码一致");
                    return;
                }
            }
            throw new RuntimeException("无法接触到当前的SESSION信息");
        }catch (Exception e) {
            ResponseService.failureResponse(request, response, e);
        }
    }

    @RequestMapping("/auth-code")
    @ResponseBody
    public void authcodeCheck(@RequestParam("authcode") String authcode, HttpServletRequest request,HttpServletResponse response) throws Exception{
        try {
                if(authCheck(authcode,"3")){
                    ResponseService.successResponse(request,response,"授权码可使用");
                }
        } catch (Exception e) {
            ResponseService.failureResponse(request, response, e);
        }
    }
}
