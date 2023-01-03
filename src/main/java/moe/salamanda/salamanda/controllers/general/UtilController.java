package moe.salamanda.salamanda.controllers.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.salamanda.salamanda.models.general.AuthenticationCode;
import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.general.WithSubject;
import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Teacher;
import moe.salamanda.salamanda.repositories.general.WithSubjectRepository;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import moe.salamanda.salamanda.repositories.teacher.TeacherRepository;
import moe.salamanda.salamanda.repositories.general.UserRepository;
import moe.salamanda.salamanda.repositories.general.WithClassRepository;
import moe.salamanda.salamanda.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UtilController {

    private final String INDEX_ERROR_PREFIX = "SOMETHING_GOING_WRONG__INDEX_CONTROLLER__";

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthCodeService authCodeService;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WithClassRepository withClassRepository;
    @Autowired
    private WithSubjectRepository withSubjectRepository;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private boolean checkcodeCheck(HttpSession session, String checkcode, String email){
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

    @GetMapping("/util/getEmail")
    @ResponseBody
    public String getEmail(HttpServletResponse response, HttpServletRequest request){
        try{
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            WithUser user = userService.getUserByUsername(username);
            return user.getEmail();
        }
        catch (Exception e) {
            System.out.println(INDEX_ERROR_PREFIX + e.getMessage());
            return "NULL";
        }
    }

    @PostMapping("/util/changeEmail")
    public void changeEmail(@RequestParam("email") String email,@RequestParam("checkcode") String checkcode,HttpServletResponse response,HttpServletRequest request) throws Exception{
        try{
            email = email.toLowerCase();
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (checkcodeCheck(session,checkcode,email)){
                    String cookie = WebUtils.getCookie(request, "LOG").getValue();
                    String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
                    WithUser user = userService.getUserByUsername(username);
                    String oldEmail = user.getEmail();
                    if(oldEmail.equals(email)) ResponseService.response(response,request,1);
                    WithUser possible = userService.getUserByEmail(email);
                    if(ObjectUtils.isEmpty(possible)){
                        user.setEmail(email);
                        userService.insertUser(user);
                        ResponseService.response(response,request,114);
                    }
                    else ResponseService.response(response,request,1);
                }
                else ResponseService.response(response,request,2);
            }
        }
        catch (Exception e){
            ResponseService.failureResponse(request, response, e);
            e.printStackTrace();
        }
    }

    @GetMapping("/util/getIndex")
    @ResponseBody
    public String getIndex(HttpServletResponse response, HttpServletRequest request){
        try{
            String cookieCode = WebUtils.getCookie(request, "LOG").getValue();
            String attribute = redisService.get(redisService.DEFAULT_ATTRIBUTE_PREFIX + cookieCode);
            return "/" + WithUser.getRoleName(attribute) + "/index.html" ;
        }
        catch (Exception e) {
            System.out.println(INDEX_ERROR_PREFIX + e.getMessage());
            return "NULL";
        }
    }

    @GetMapping("/util/getUsername")
    @ResponseBody
    public String getUsername(HttpServletResponse response,HttpServletRequest request){
        try{
            String cookieCode = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookieCode);
            return username;
        }
        catch (Exception e){
            System.out.println(INDEX_ERROR_PREFIX+e.getMessage());
            return "NULL";
        }
    }

    @PostMapping("/util/changePassword")
    public void changePassword(@RequestParam("passwordRetro") String retro,@RequestParam("password") String password,@RequestParam("passwordRe") String re, HttpServletResponse response, HttpServletRequest request){
        try{
            String cookieCode = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookieCode);
            String attribute =  redisService.get(redisService.DEFAULT_ATTRIBUTE_PREFIX + cookieCode);
            WithUser user = userService.getUserByUsername(username);
            if (!user.getPassword().equals(retro)) {
                ResponseService.response(response, request, 1);
                return;
            }
            if(!passwordCheck(password)){
                ResponseService.response(response,request,2);
                return;
            }
            if(!password.equals(re)){
                ResponseService.response(response,request,3);
                return;
            }
            user.setPassword(password);
            if(attribute.equals("1")) studentRepository.save((Student)user);
            if(attribute.equals("2")) teacherRepository.save((Teacher)user);
            if(attribute.equals("3")) userRepository.save(user);
            ResponseService.response(response,request,114);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/util/getWith")
    @ResponseBody
    public List getWith(HttpServletRequest request,HttpServletResponse response){
        List<Map> list = new ArrayList<>();
        List<WithClass> withClassList = withClassRepository.findAll();
        for (WithClass withClass:withClassList) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",withClass.getId());
            map.put("key",WithClass.total(withClass));
            list.add(map);
        }
        return list;
    }

    @GetMapping("/util/getInitInformationB-Teacher")
    @ResponseBody
    public Map getInitInformationBTeacher(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            Teacher user = teacherRepository.findByUsername(username);
            map.put("research",user.getResearchDirect());
            map.put("paper",user.getPapers());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @PostMapping("/util/changeInformationB-Teacher")
    public void changeInformationBTeacher(HttpServletRequest request, HttpServletResponse response, @RequestParam("paper") @Nullable String paper, @RequestParam("research") @Nullable String research){
        try{
            if(paper.length()>300){
                ResponseService.response(response,request,2);
                return;
            }
            if(research.length()>100){
                ResponseService.response(response,request,1);
                return;
            }
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            Teacher user = teacherRepository.findByUsername(username);
            user.setPapers(paper);
            user.setResearchDirect(research);
            teacherRepository.save(user);
            ResponseService.response(response,request,114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/util/getInitInformationB-Student")
    @ResponseBody
    public Map getInitInformationBStudent(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            Student user = studentRepository.findByUsername(username);
            if(user.getWithClass()!=null) map.put("with",user.getWithClass().getId());
            else map.put("with",null);
            if(user.getBirthday()!=null) map.put("birthday",format.format(user.getBirthday()));
            else map.put("birthday",null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @PostMapping("/util/changeInformationB-Student")
    public void changeInformationBStudent(HttpServletRequest request, HttpServletResponse response, @RequestParam("with") @Nullable Integer with, @RequestParam("birthday") @Nullable String birthday){
        try{
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            Student user = studentRepository.findByUsername(username);
            if(with != null){
                user.setWithClass(withClassRepository.findById(with));
            }
            else{
                user.setWithClass(null);
            }
            if(!birthday.equals("")) {
                user.setBirthday(format.parse(birthday));
            }
            else{
                user.setBirthday(null);
            }
            studentRepository.save(user);
            ResponseService.response(response,request,114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/util/getInitInformationA")
    @ResponseBody
    public Map getInitInformationA(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        try{
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            WithUser user = userService.getUserByUsername(username);
            map.put("firstname",user.getFirstName());
            map.put("lastname",user.getLastName());
            map.put("phone",user.getPhone());
            map.put("sex",user.getSex());
            map.put("introduction",user.getIntroductions());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @PostMapping("/util/changeInformationA")
    public void changeInformationA(@RequestParam("firstname")String firstname,@RequestParam("lastname")String lastname,@RequestParam("withphone") @Nullable String phone,@RequestParam("sex") @Nullable Integer sex,@RequestParam("introduction") @Nullable  String introduction,HttpServletResponse response,HttpServletRequest request){
        try{
            if (firstname.equals("")) {
                ResponseService.response(response, request, 1);return;
            }
            if (lastname.equals("")) {
                ResponseService.response(response, request, 2);return;
            }
            if (phone.length()>0&&!phoneCheck(phone)) {
                ResponseService.response(response, request, 3);return;
            }
            if (introduction.length()>300) {
                ResponseService.response(response, request, 4);return;
            }
            System.out.println(phone);
            String cookie = WebUtils.getCookie(request, "LOG").getValue();
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + cookie);
            String attribute =  redisService.get(redisService.DEFAULT_ATTRIBUTE_PREFIX + cookie);
            WithUser user = userService.getUserByUsername(username);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setPhone(phone);
            user.setSex(sex);
            user.setIntroductions(introduction);
            if(attribute.equals("1")) studentRepository.save((Student)user);
            if(attribute.equals("2")) teacherRepository.save((Teacher)user);
            if(attribute.equals("3")) userRepository.save(user);
            ResponseService.response(response,request,114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean phoneCheck(String phone){
        for(int i = 0;i < phone.length();i++)
            if(!Character.isDigit(phone.charAt(i))) return false;
        return true;
    }

    private boolean passwordCheck(String password){
        if(password.length()<10) return false;
        for(int i = 0;i < password.length();i++)
            if(!Character.isLetterOrDigit(password.charAt(i))) return false;
        return true;
    }

    @PostMapping("/util/addAuthcode")
    public void addAuthcode(HttpServletResponse response,HttpServletRequest request){
        try{
            AuthenticationCode code = authCodeService.createAuthCode();
            response.setContentType("application/json;charset-UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> result = new HashMap<>();
            result.put("victory", 114);
            result.put("id",code.getId());
            result.put("authcode", code.getAuthenticationCode());
            result.put("usestatus", code.isUsed() == false ? "未使用" : "已使用");
            result.put("usedby", code.getIsUsedBy());
            result.put("status", 200);
            String str = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(str);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/util/getSubject")
    @ResponseBody
    public List getSubject(HttpServletRequest request,HttpServletResponse response){
        List<Map> list = new ArrayList<>();
        List<WithSubject> withSubjectList = withSubjectRepository.findAll();
        for (WithSubject withSubject : withSubjectList) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", withSubject.getId());
            map.put("value", withSubject.getName());
            list.add(map);
        }
        return list;
    }
}
