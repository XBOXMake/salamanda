package moe.salamanda.salamanda.controllers.special;

import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.student.Activity;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Course;
import moe.salamanda.salamanda.models.teacher.CourseGrade;
import moe.salamanda.salamanda.models.teacher.Teacher;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import moe.salamanda.salamanda.repositories.teacher.CourseGradeRepository;
import moe.salamanda.salamanda.repositories.teacher.CourseRepository;
import moe.salamanda.salamanda.repositories.teacher.TeacherRepository;
import moe.salamanda.salamanda.services.RedisService;
import moe.salamanda.salamanda.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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

    //
    ///teacher/course/**
    //

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseGradeRepository gradeRepository;
    @Autowired
    RedisService redisService;

    //
    //select.html
    //

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat formatForm = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping(value = "/teacher/course/getSelect",produces = "application/json")
    @ResponseBody
    public List<Map> getSelect(HttpServletRequest request,HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Teacher teacher = teacherRepository.findByUsername(username);
        List<Course> data = teacher.getCourses();
        List<Map> list = new ArrayList<>();
        for(Course course:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",course.getId());
            map.put("name",course.getName());
            map.put("dateStart",format.format(course.getDateStart()));
            map.put("dateEnd",format.format(course.getDateEnd()));
            map.put("grade",course.getGrade());
            map.put("limitation",course.getLimitation());
            map.put("number",course.getCourseGrades().size());
            map.put("option",course.getId());
            list.add(map);
        }
        return list;
    }

    @GetMapping(value = "/teacher/course/getSelectForm",produces = "application/json")
    @ResponseBody
    public List<Map> getSelect(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        List<Map> list = new ArrayList<>();
        if(id == 0){
            Map<String,Object> map = new HashMap<>();
            map.put("studentID",0);
            map.put("name",0);
            map.put("with",0);
            map.put("email",0);
            list.add(map);
            return list;
        }
        Course course = courseRepository.findById(id);
        List<CourseGrade> data = course.getCourseGrades();
        for(CourseGrade grade:data){
            Map<String,Object> map = new HashMap<>();
            map.put("studentID",Student.getStudentID(grade.getStudent()));
            map.put("name",Student.getName(grade.getStudent()));
            map.put("with",WithClass.total(grade.getStudent().getWithClass()));
            map.put("email",grade.getStudent().getEmail());
            list.add(map);
        }
        return list;
    }

    //
    //score.html
    //

    @GetMapping(value = "/teacher/course/getScore",produces = "application/json")
    @ResponseBody
    public List<Map> getScore(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        List<Map> list = new ArrayList<>();
        if(id == 0){
            Map<String,Object> map = new HashMap<>();
            map.put("studentID",0);
            map.put("name",0);
            map.put("with",0);
            map.put("grade",0);
            map.put("option",0);
            list.add(map);
            return list;
        }
        Course course = courseRepository.findById(id);
        List<CourseGrade> data = course.getCourseGrades();
        for(CourseGrade grade:data){
            Map<String,Object> map = new HashMap<>();
            map.put("studentID",Student.getStudentID(grade.getStudent()));
            map.put("name",Student.getName(grade.getStudent()));
            map.put("with",WithClass.total(grade.getStudent().getWithClass()));
            map.put("grade",grade.getNumber());
            map.put("option",grade.getId());
            list.add(map);
        }
        return list;
    }

    @GetMapping(value = "/teacher/course/getInitScore",produces = "application/json")
    @ResponseBody
    public Map getInitActivity(@RequestParam("id") Integer id, HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        CourseGrade grade = gradeRepository.findById(id);
        map.put("score", grade.getNumber());
        return map;
    }

    @PostMapping("/teacher/course/saveScore")
    public void saveActivity(@RequestParam("id")Integer id, @RequestParam("score") @Nullable Integer score, HttpServletRequest request, HttpServletResponse response){
        try{
            if (score!=null) {
                if (score>100||score<0){
                    ResponseService.response(response, request, 1);
                    return;
                }
            }
            CourseGrade grade = gradeRepository.findById(id);
            grade.setNumber(score);
            gradeRepository.save(grade);
            ResponseService.response(response, request, 114);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //
    //course.html
    //

    @GetMapping(value = "/teacher/course/getCourse",produces = "application/json")
    @ResponseBody
    public List<Map> getCourse(HttpServletRequest request,HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Teacher teacher = teacherRepository.findByUsername(username);
        List<Course> data = teacher.getCourses();
        List<Map> list = new ArrayList<>();
        for(Course course:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",course.getId());
            map.put("name",course.getName());
            map.put("dateStartChoose",format.format(course.getSelectDateStart()));
            map.put("dateEndChoose",format.format(course.getSelectDateEnd()));
            map.put("dateStart",format.format(course.getDateStart()));
            map.put("dateEnd",format.format(course.getDateEnd()));
            map.put("schedule",course.getSchedule());
            map.put("grade",course.getGrade());
            map.put("limitation",course.getLimitation());
            map.put("number",course.getCourseGrades().size());
            map.put("description",course.getDescription());
            map.put("option",course.isLock()==true?"1":"0"+"&"+course.getId());
            list.add(map);
        }
        return list;
    }

    @GetMapping(value = "/teacher/course/getInitCourse",produces = "application/json")
    @ResponseBody
    public Map getInitCourse(@RequestParam("id") Integer id, HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        Course course = courseRepository.findById(id);
        map.put("name",course.getName());
        map.put("dateStartChoose",formatForm.format(course.getSelectDateStart()));
        map.put("dateEndChoose",formatForm.format(course.getSelectDateEnd()));
        map.put("dateStart",formatForm.format(course.getDateStart()));
        map.put("dateEnd",formatForm.format(course.getDateEnd()));
        map.put("schedule",course.getSchedule());
        map.put("credit",course.getGrade());
        map.put("limitation",course.getLimitation());
        map.put("description",course.getDescription());
        return map;
    }

    @PostMapping("/teacher/course/saveCourse")
    public void saveCourse(@RequestParam("id")Integer id, @RequestParam("name")String name, @RequestParam("dateStartChoose") @Nullable String dateStartChoose, @RequestParam("dateEndChoose") @Nullable String dateEndChoose, @RequestParam("dateStart") @Nullable String dateStart, @RequestParam("dateEnd") @Nullable String dateEnd, @RequestParam("schedule") @Nullable String schedule,@RequestParam("description") @Nullable String description, @RequestParam("limitation") @Nullable Integer limitation, @RequestParam("credit") @Nullable Integer grade, HttpServletRequest request, HttpServletResponse response){
        try{
            Course course;
            if(id != 0) course = courseRepository.findById(id);
            else{
                course = new Course();
                String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
                Teacher teacher = teacherRepository.findByUsername(username);
                course.setTeacher(teacher);
            }
            if (name.length() > 10) {
                ResponseService.response(response, request, 1);
                return;
            }
            if (dateStartChoose.equals(null)||dateStartChoose.equals("")){
                ResponseService.response(response, request, 2);
                return;
            }
            Date startChoose = formatForm.parse(dateStartChoose);
            if(dateEndChoose.equals(null)||dateEndChoose.equals("")){
                ResponseService.response(response, request, 3);
                return;
            }
            Date endChoose = formatForm.parse(dateEndChoose);
            System.out.println(dateEndChoose.compareTo(dateStartChoose));
            if(dateEndChoose.compareTo(dateStartChoose)<0){
                ResponseService.response(response, request, 4);
                return;
            }
            if (dateStart.equals(null)||dateStart.equals("")){
                ResponseService.response(response, request, 5);
                return;
            }
            Date start = formatForm.parse(dateStart);
            if(dateEnd.equals(null)||dateEnd.equals("")){
                ResponseService.response(response, request, 6);
                return;
            }
            Date end = formatForm.parse(dateEnd);
            System.out.println(dateEnd.compareTo(dateStart));
            if(dateEnd.compareTo(dateStart)<0){
                ResponseService.response(response, request, 7);
                return;
            }
            if (limitation<0){
                ResponseService.response(response, request, 8);
                return;
            }
            if (id!=0&&limitation<course.getCourseGrades().size()){
                ResponseService.response(response, request, 9);
                return;
            }
            if (grade<0){
                ResponseService.response(response, request, 10);
                return;
            }
            if (schedule.equals(null)||schedule.length()>50){
                ResponseService.response(response, request, 11);
                return;
            }
            if (description.equals(null)||description.length()>200){
                ResponseService.response(response, request, 12);
                return;
            }
            course.setName(name);
            course.setSelectDateStart(startChoose);
            course.setSelectDateEnd(endChoose);
            course.setDateStart(start);
            course.setDateEnd(end);
            course.setSchedule(schedule);
            course.setLimitation(limitation);
            course.setGrade(grade);
            course.setDescription(description);
            courseRepository.save(course);
            ResponseService.response(response, request, 114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/teacher/course/deleteCourse")
    public void deleteCourse(@RequestParam("id") Integer id,HttpServletResponse response,HttpServletRequest request){
        try{
            Course  course= courseRepository.findById(id);
            for(CourseGrade grade:course.getCourseGrades()){
                gradeRepository.delete(grade);
            }
            courseRepository.delete(course);
            ResponseService.response(response,request,114);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/teacher/course/lockCourse")
    public void lockCourse(@RequestParam("id") Integer id,HttpServletResponse response,HttpServletRequest request){
        try{
            Course course= courseRepository.findById(id);
            course.setLock(true);
            courseRepository.save(course);
            ResponseService.response(response,request,114);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/teacher/course/excelIn")
    public void excelIn(@RequestParam("id") Integer id, @RequestParam("content")String content,HttpServletRequest request,HttpServletResponse response) throws Exception{
        System.out.println(id);
        Map<String,Student> pre = new HashMap<>();
        List<Student> data = studentRepository.findAll();
        JSONArray list;
        try{
            list = new JSONArray(content);
        }
        catch (Exception e){
            ResponseService.response(response, request, -1);
            return;
        }
        Course course = courseRepository.findById(id);
        int count = 0;
        for(Student student:data){
            String ID = Student.getStudentID(student);
            if(ID!=null && !ID.equals("")){
                pre.put(ID,student);
            }
        }
        for (int i=0;i<list.length();i++){
            try{
                JSONObject object = list.getJSONObject(i);
                String ID = object.getString("学号");
                Student student = pre.get(ID);
                if(student!=null){
                    CourseGrade grade = gradeRepository.findByCourseAndStudent(student.getId(),course.getId());
                    if(grade!=null){
                        Integer number = Integer.parseInt(object.getString("成绩"));
                        if(number>=0&&number<=100) {
                            grade.setNumber(number);
                            gradeRepository.save(grade);
                            count++;
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            ResponseService.response(response, request, count);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/teacher/getData",produces = "application/json")
    @ResponseBody
    public Map getData( HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Teacher teacher = teacherRepository.findByUsername(username);
        map.put("name",Teacher.getName(teacher));
        map.put("phone",teacher.getPhone());
        map.put("sex",teacher.getSexes());
        map.put("introduction",teacher.getIntroductions());
        map.put("research",teacher.getResearchDirect());
        map.put("papers",teacher.getPapers());
        return map;
    }
}
