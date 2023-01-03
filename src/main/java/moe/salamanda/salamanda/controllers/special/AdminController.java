package moe.salamanda.salamanda.controllers.special;

import moe.salamanda.salamanda.models.general.AuthenticationCode;
import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.general.WithSubject;
import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Course;
import moe.salamanda.salamanda.repositories.general.AuthenticationCodeRepository;
import moe.salamanda.salamanda.repositories.general.UserRepository;
import moe.salamanda.salamanda.repositories.general.WithClassRepository;
import moe.salamanda.salamanda.repositories.general.WithSubjectRepository;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import moe.salamanda.salamanda.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    AuthenticationCodeRepository codeRepository;
    @Autowired
    WithClassRepository classRepository;
    @Autowired
    WithSubjectRepository subjectRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    UserRepository userRepository;

    //
    ///admin/manage/authcode.html
    //

    @GetMapping(value = "/admin/manage/getAuthcode",produces = "application/json")
    @ResponseBody
    public List<Map> getAuthcode(HttpServletRequest request, HttpServletResponse response){
        List<AuthenticationCode> data = codeRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(AuthenticationCode code:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",code.getId());
            map.put("authcode", code.getAuthenticationCode());
            map.put("usestatus",!code.isUsed() ? "未使用" : "已使用");
            map.put("usedby", code.getIsUsedBy());
            list.add(map);
        }
        return list;
    }

    //
    ///admin/manage/class.html
    //

    @GetMapping(value = "/admin/manage/getClass",produces = "application/json")
    @ResponseBody
    public List<Map> getClass(HttpServletRequest request, HttpServletResponse response){
        List<WithClass> data = classRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(WithClass withClass:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",withClass.getId());
            map.put("year", withClass.getYear());
            if(withClass.getWithSubject()!=null) map.put("subject",withClass.getWithSubject().getName());
            else map.put("subject",null);
            map.put("class", withClass.getWithClass()+"班");
            map.put("option",withClass.getId());
            list.add(map);
        }
        return list;
    }

    @PostMapping("/admin/manage/saveClass")
    public void saveClass(HttpServletRequest request, HttpServletResponse response, @RequestParam("year") Integer year, @RequestParam("id") Integer id, @RequestParam("subject") @Nullable Integer subject, @RequestParam("class") Integer withClass){
        try{
            WithSubject withSubject = null;
            if (year < 1 || year > 9999) {
                ResponseService.response(response, request, 1);return;
            }
            try{
                withSubject = subjectRepository.findById(subject);
            }
            catch (Exception e){
                ResponseService.response(response,request,2);return;
            }
            if(withClass > 99 || withClass < 1) {
                ResponseService.response(response, request, 3);return;
            }
            WithClass result;
            if(id == 0){
                result = new WithClass();
            }
            else result = classRepository.findById(id);
            result.setYear(year);
            result.setWithSubject(withSubject);
            result.setWithClass(withClass);
            classRepository.save(result);
            ResponseService.response(response,request,114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/admin/manage/getInitClass",produces = "application/json")
    @ResponseBody
    public Map getInitClass(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        WithClass withClass = classRepository.findById(id);
        Map map = new HashMap();
        map.put("year", withClass.getYear());
        if(withClass.getWithSubject()!=null) map.put("subject",withClass.getWithSubject().getId());
        else map.put("subject",null);
        map.put("class", withClass.getWithClass());
        return map;
    }

    @PostMapping("/admin/manage/deleteClass")
    public void deleteClass(@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response){
        try{
            WithClass withClass = classRepository.findById(id);
            List<Student> students = withClass.getStudentList();
            for (Student user : students) {
                user.setWithClass(null);
                studentRepository.save(user);
            }
            classRepository.delete(withClass);
            ResponseService.response(response, request, 114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //
    ///admin/manage/Subject.html
    //

    @GetMapping(value = "/admin/manage/getSubject",produces = "application/json")
    @ResponseBody
    public List<Map> getSubject(HttpServletRequest request, HttpServletResponse response){
        List<WithSubject> data = subjectRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(WithSubject subject:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",subject.getId());
            map.put("name", subject.getName());
            map.put("option",subject.getId());
            list.add(map);
        }
        return list;
    }

    @PostMapping("/admin/manage/saveSubject")
    public void saveSubject(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name,@RequestParam("id") Integer id){
        try{
            if (name.equals(null)) {
                ResponseService.response(response, request, 1);return;
            }
            WithSubject result;
            if(id == 0){
                result = new WithSubject();
            }
            else result = subjectRepository.findById(id);
            result.setName(name);
            subjectRepository.save(result);
            ResponseService.response(response,request,114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/admin/manage/getInitSubject",produces = "application/json")
    @ResponseBody
    public Map getInitSubject(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        WithSubject withSubject = subjectRepository.findById(id);
        Map map = new HashMap();
        map.put("name", withSubject.getName());
        return map;
    }

    @PostMapping("/admin/manage/deleteSubject")
    public void deleteSubject(@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response){
        try{
            WithSubject withSubject = subjectRepository.findById(id);
            List<WithClass> classes = withSubject.getClassList();
            for (WithClass withClass : classes) {
                withClass.setWithSubject(null);
                classRepository.save(withClass);
            }
            subjectRepository.delete(withSubject);
            ResponseService.response(response,request,114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //
    ///admin/user/admin.html
    //

    @GetMapping(value = "/admin/user/getUser",produces = "application/json")
    @ResponseBody
    public List<Map> getUser(@RequestParam("role") String role,HttpServletRequest request,HttpServletResponse response){
        List<WithUser> data = userRepository.findByAttribute(WithUser.getRoleAttribute(role.toUpperCase()));
        List<Map> list = new ArrayList<>();
        for(WithUser user:data){
            Map<String,Object> map = new HashMap<>();
            map.put("username",user.getUsername());
            map.put("name",user.getFirstName()+user.getLastName());
            map.put("sex",user.getSexes());
            map.put("email",user.getEmail());
            map.put("phone",user.getPhone());
            map.put("password",user.getPassword());
            map.put("introduction",user.getIntroductions());
            list.add(map);
        }
        return list;
    }
}
