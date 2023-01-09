package moe.salamanda.salamanda.controllers.special;

import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.student.Activity;
import moe.salamanda.salamanda.models.student.Blog;
import moe.salamanda.salamanda.models.student.Comment;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Course;
import moe.salamanda.salamanda.models.teacher.CourseGrade;
import moe.salamanda.salamanda.models.teacher.Teacher;
import moe.salamanda.salamanda.repositories.student.ActivityRepository;
import moe.salamanda.salamanda.repositories.student.BlogRepository;
import moe.salamanda.salamanda.repositories.student.CommentRepository;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import moe.salamanda.salamanda.repositories.teacher.CourseGradeRepository;
import moe.salamanda.salamanda.repositories.teacher.CourseRepository;
import moe.salamanda.salamanda.services.RedisService;
import moe.salamanda.salamanda.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class StudentController {
    @Autowired
    RedisService redisService;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    StudentRepository studentRepository;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat formatForm = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/student/createBlog")
    public void createBlog(@RequestParam("title") String title, @RequestParam("content") String content, HttpServletRequest request, HttpServletResponse response){
        try{
            if(ObjectUtils.isEmpty(title)||ObjectUtils.isEmpty(content)){
                ResponseService.failureResponse(request,response,"标题或正文为空白！");
            }
            else{
                Blog blog = new Blog();
                blog.setTitle(title);
                blog.setContent(content);
                blog.setDate(new Date());
                String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
                Student student = studentRepository.findByUsername(username);
                blog.setAuthor(student);
                blogRepository.save(blog);
                ResponseService.successResponse(request, response, "success!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/student/getBlog", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public Map getBlog(@RequestParam("position") Integer position, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            Blog blog = null;
            if(position == 0) throw new RuntimeException();
            else if(position < 0){
                blog = blogRepository.randomPickOne();
            }
            else{
                blog = blogRepository.findById(position);
            }
            map.put("username", blog.getAuthor().getUsername());
            map.put("time", format.format(blog.getDate()));
            map.put("thumbnail", "/util/getThumbnail-chose?username=" + blog.getAuthor().getUsername());
            map.put("content", blog.getContent());
            map.put("title", blog.getTitle());
            map.put("id",blog.getId());
            map.put("exist", "yes");
        }
        catch (Exception e){
            map.put("exist","no");
            e.printStackTrace();
        }
        return map;
    }

    //
    ///student/course/**
    //

    @Autowired
    CourseRepository courseRepository;

    //
    //course.html
    //

    @GetMapping(value = "/student/course/getCourse",produces = "application/json")
    @ResponseBody
    public List<Map> getCourse(HttpServletRequest request,HttpServletResponse response){
        List<Course> data = courseRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(Course course:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",course.getId());
            map.put("name",course.getName());
            map.put("dateStart",format.format(course.getDateStart()));
            map.put("dateEnd",format.format(course.getDateEnd()));
            map.put("schedule",Course.getSchedule(course));
            map.put("grade",course.getGrade());
            map.put("limitation",course.getLimitation());
            map.put("description",course.getDescription());
            list.add(map);
        }
        return list;
    }

    //
    //score.html
    //

    @GetMapping(value = "/student/course/getScore",produces = "application/json")
    @ResponseBody
    public List<Map> getScore(HttpServletRequest request,HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Student student = studentRepository.findByUsername(username);
        List<CourseGrade> data = student.getCourseGrades();
        List<Map> list = new ArrayList<>();
        for(CourseGrade grade:data){
            Map<String,Object> map = new HashMap<>();
            map.put("id",grade.getCourse().getId());
            map.put("name",grade.getCourse().getName());
            map.put("grade",grade.getCourse().getGrade());
            map.put("score",grade.getNumber());
            list.add(map);
        }
        return list;
    }

    //
    //select.html
    //

    @Autowired
    CourseGradeRepository gradeRepository;

    private String getStatus(Student student,Course course){
        try{
            CourseGrade grade = gradeRepository.findByCourseAndStudent(student.getId(),course.getId());
            if(grade.equals(null)) return "未选择";
            else return "已选择";
        }
        catch (Exception e){
            return "未选择";
        }
    }

    @GetMapping(value = "/student/course/getSelect",produces = "application/json")
    @ResponseBody
    public List<Map> getSelect(HttpServletRequest request,HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Student student = studentRepository.findByUsername(username);
        List<Course> data = courseRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(Course course:data){
            Date date = new Date();
            if (date.compareTo(course.getSelectDateStart())>=0 &&date.compareTo(course.getSelectDateEnd())<=0){
                Map<String, Object> map = new HashMap<>();
                map.put("status", getStatus(student, course));
                map.put("id", course.getId());
                map.put("name", course.getName());
                map.put("dateStartChoose", format.format(course.getSelectDateStart()));
                map.put("dateEndChoose", format.format(course.getSelectDateEnd()));
                map.put("dateStart", format.format(course.getDateStart()));
                map.put("dateEnd", format.format(course.getDateEnd()));
                map.put("schedule", Course.getSchedule(course));
                map.put("grade", course.getGrade());
                map.put("limitation", course.getLimitation());
                map.put("number", course.getCourseGrades().size());
                map.put("description", course.getDescription());
                if(course.isLock()) map.put("option", "3:"+course.getId());
                else if(Course.isLimitationOverflow(course)) map.put("option", getStatus(student,course).equals("未选择")?"2":"0"+":"+course.getId());
                else map.put("option", student.courseOverflow(course)?"1":"0"+":"+course.getId());
                list.add(map);
            }
        }
        return list;
    }

    @PostMapping("/student/course/changeSelect")
    public void changeSelect(@RequestParam("id") Integer id,HttpServletResponse response,HttpServletRequest request){
        try{
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
            Student student = studentRepository.findByUsername(username);
            Course course = courseRepository.findById(id);
            System.out.println(getStatus(student,course));
            if(getStatus(student,course).equals("已选择")){
                if(course.getLimitation()<=course.getCourseGrades().size()){
                    ResponseService.response(response,request,114);
                    return;
                }
                CourseGrade grade = gradeRepository.findByCourseAndStudent(student.getId(),course.getId());
                gradeRepository.delete(grade);
                ResponseService.response(response,request,2);
                return;
            }
            else if(getStatus(student,course).equals("未选择")){
                CourseGrade grade = new CourseGrade();
                grade.setStudent(student);
                grade.setCourse(course);
                gradeRepository.save(grade);
                ResponseService.response(response,request,1);
                return;
            }
            ResponseService.response(response,request,114);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //
    ///student/student/**
    //

    @Autowired
    ActivityRepository activityRepository;

    @GetMapping(value = "/student/student/getActivity",produces = "application/json")
    @ResponseBody
    public List<Map> getActivity(@RequestParam("role")String role, HttpServletRequest request,HttpServletResponse response){
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Student student = studentRepository.findByUsername(username);
        List<Activity> data = student.getActivities();
        List<Map> list = new ArrayList<>();
        for(Activity activity:data){
            if(activity.getRole().equals(role)){
                Map<String, Object> map = new HashMap<>();
                map.put("option", activity.getId());
                map.put("name", activity.getName());
                map.put("dateStart", format.format(activity.getDateStart()));
                map.put("dateEnd", format.format(activity.getDateEnd()));
                map.put("contentA", activity.getContentA());
                map.put("contentB", activity.getContentB());
                list.add(map);
            }
        }
        return list;
    }

    @GetMapping(value = "/student/student/getInitActivity",produces = "application/json")
    @ResponseBody
    public Map getInitActivity(@RequestParam("id") Integer id, HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        Activity activity = activityRepository.findById(id);
        map.put("name", activity.getName());
        map.put("dateStart", formatForm.format(activity.getDateStart()));
        map.put("dateEnd", formatForm.format(activity.getDateEnd()));
        map.put("contentA", activity.getContentA());
        map.put("contentB", activity.getContentB());
        return map;
    }

    @PostMapping("/student/student/saveActivity")
    public void saveActivity(@RequestParam("id")Integer id, @RequestParam("name")String name, @RequestParam("dateStart") @Nullable String dateStart, @RequestParam("dateEnd") @Nullable String dateEnd, @RequestParam("contentA")String contentA, @RequestParam("contentB")String contentB, @RequestParam("role")String role, HttpServletRequest request, HttpServletResponse response){
        try{
            if (name.length() > 20) {
                ResponseService.response(response, request, 1);
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
            if(dateEnd.compareTo(dateStart)<0){
                ResponseService.response(response, request, 2);
                return;
            }
            if (contentA.length()>300){
                ResponseService.response(response, request, 3);
                return;
            }
            if (contentB.length()>300){
                ResponseService.response(response, request, 4);
                return;
            }
            Activity activity;
            if(id != 0) activity = activityRepository.findById(id);
            else{
                activity = new Activity();
                String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
                Student student = studentRepository.findByUsername(username);
                activity.setStudent(student);
                activity.setAttribute(Activity.getAttribute(role));
            }
            activity.setName(name);
            activity.setDateStart(start);
            activity.setDateEnd(end);
            activity.setContentA(contentA);
            activity.setContentB(contentB);
            activityRepository.save(activity);
            ResponseService.response(response, request, 114);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/student/student/deleteActivity")
    public void deleteActivity(@RequestParam("id") Integer id,HttpServletResponse response,HttpServletRequest request){
        try{
            Activity activity = activityRepository.findById(id);
            activityRepository.delete(activity);
            ResponseService.response(response,request,114);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/student/getData",produces = "application/json")
    @ResponseBody
    public Map getData( HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Student student = studentRepository.findByUsername(username);
        map.put("name",Student.getName(student));
        map.put("phone",student.getPhone());
        map.put("sex",student.getSexes());
        map.put("introduction",student.getIntroductions());
        map.put("with", WithClass.total(student.getWithClass()));
        try{
            map.put("studentID", Student.getStudentID(student));
        }
        catch (Exception e){
            map.put("studentID", "");
        }
        try{
            map.put("birthday", formatForm.format(student.getBirthday()));
        }
        catch (Exception e){
            map.put("birthday","");
        }
        return map;
    }

    @GetMapping(value = "/student/getPicture",produces = "application/json")
    @ResponseBody
    public Map getPicture( HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
        Student student = studentRepository.findByUsername(username);
        List<Comment> comments = student.getComments();
        Map<Integer,Integer> note = new HashMap<>();
        for(Comment comment:comments){
            try{
                note.put(comment.getContent(), note.get(comment.getContent()) + 1);
            }
            catch (Exception e){
                note.put(comment.getContent(),1);
            }
        }
        Integer key1=0,key2=0,key3=0;Integer value1=0,value2=0,value3=0;
        if(note.size() == 0) {
            map.put("total",0);
            return map;
        }
        for(Integer key: note.keySet()){
            if(note.get(key)>value3){
                key3=key;value3=note.get(key);
            }
        }
        if(note.size() == 1){
            map.put("total",1);
            map.put("val1",(double)value3/(double)comments.size());
            map.put("key1",Comment.getTag(key3));
            return map;
        }
        for(Integer key: note.keySet()){
            if(note.get(key)>value2&&key!=key3){
                key2=key;value2=note.get(key);
            }
        }
        if(note.size() == 2){
            map.put("total",2);
            map.put("val2",(double)value2/(double)comments.size());
            map.put("key2",Comment.getTag(key2));
            map.put("val1",(double)value3/(double)comments.size());
            map.put("key1",Comment.getTag(key3));
            return map;
        }
        for(Integer key: note.keySet()){
            if(note.get(key)>value1&&key!=key3&&key!=key2){
                key1=key;value1=note.get(key);
            }
        }
        map.put("total",3);
        map.put("val3",(double)value1/(double)comments.size());
        map.put("key3",Comment.getTag(key1));
        map.put("val2",(double)value2/(double)comments.size());
        map.put("key2",Comment.getTag(key2));
        map.put("val1",(double)value3/(double)comments.size());
        map.put("key1",Comment.getTag(key3));
        return map;
    }

    @GetMapping(value = "/student/getTags",produces = "application/json")
    @ResponseBody
    public List<Map> getTags(@RequestParam("id")Integer id, HttpServletRequest request,HttpServletResponse response){
        List<Map> list = new ArrayList<>();
        Map<Integer,Boolean> check = new HashMap<>();
        Random random = new Random();
        for(int i=1;i<=15;i++){
            Map<String, Object> map = new HashMap<>();
            int rand = random.nextInt(41);
            while(check.containsKey(rand)) rand = random.nextInt(41);
            check.put(rand,true);
            map.put("label",Comment.getTag(rand));
            map.put("url","javascript:upload('/student/addTag?tag="+rand+"&id="+id+"')");
            map.put("target","_top");
            list.add(map);
        }
        return list;
    }

    @Autowired
    CommentRepository commentRepository;

    @PostMapping("/student/addTag")
    public void addTag(@RequestParam("tag") Integer tag,@RequestParam("id") Integer id,HttpServletResponse response,HttpServletRequest request){
        try{
            if (tag < 0 || tag > 40){
                ResponseService.response(response,request,1);
                return;
            }
            Student student = null;
            try{
                student = studentRepository.findById(id);
            }
            catch (Exception e){
                ResponseService.response(response,request,2);
                return;
            }
            if(student == null){
                ResponseService.response(response,request,2);
                return;
            }
            Comment comment = new Comment();
            comment.setTo(student);
            comment.setContent(tag);
            commentRepository.save(comment);
            ResponseService.response(response,request,114);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/student/tagInit",produces = "application/json")
    @ResponseBody
    public Map tagInit( HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        Student student = studentRepository.randomPickOne();
        map.put("name",Student.getName(student));
        map.put("introduction",student.getIntroductions());
        map.put("id",student.getId());
        map.put("username",student.getUsername());
        return map;
    }

    @GetMapping(value = "/student/utils/getCourse",produces = "application/json")
    @ResponseBody
    public List getCourse(@RequestParam("date") @Nullable String str,HttpServletRequest request,HttpServletResponse response){
        List<List<Map>> result = new ArrayList<>();
        for(int i=0;i<=7;i++){
            result.add(new ArrayList<>());
            for(int j=0;j<=8;j++){
                result.get(i).add(new HashMap<>());
                result.get(i).get(j).put("exist",0);
            }
        }
        try{
            System.out.println(str);
            Date date = formatForm.parse(str);
            String username = redisService.get(redisService.DEFAULT_USERNAME_PREFIX + WebUtils.getCookie(request, "LOG").getValue());
            Student student = studentRepository.findByUsername(username);
            List<CourseGrade> grades = student.getCourseGrades();
            for(CourseGrade grade:grades){
                Course course = grade.getCourse();
                if(Course.isDateInside(date,course)) {
                    String[] schedules = course.getSchedule().split(";");
                    for(int i=1;i<schedules.length;i++){
                        String schedule[] = schedules[i].split("-");
                        int x = Integer.parseInt(schedule[0]);
                        int y = Integer.parseInt(schedule[1]);
                        result.get(x).get(y).put("exist",1);
                        result.get(x).get(y).put("id",course.getId());
                        result.get(x).get(y).put("content",course.getName());
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
