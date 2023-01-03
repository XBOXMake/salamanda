package moe.salamanda.salamanda.controllers.special;

import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TeacherController {
    @Autowired
    StudentRepository studentRepository;

    @GetMapping(value = "/teacher/getInfo",produces = "application/json")
    @ResponseBody
    public List<Map> getInfo(HttpServletRequest request, HttpServletResponse response){
        List<Student> data = studentRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(Student user:data){
            Map<String,Object> map = new HashMap<>();
            try{
                map.put("studentID",Student.getStudentID(user));
            }
            catch (Exception e){
                map.put("studentID","");
            }
            map.put("name",user.getFirstName()+user.getLastName());
            try{
                map.put("with", WithClass.total(user.getWithClass()));
            }
            catch (Exception e){
                map.put("with","");
            }
            map.put("email",user.getEmail());
            list.add(map);
        }
        return list;
    }

}
