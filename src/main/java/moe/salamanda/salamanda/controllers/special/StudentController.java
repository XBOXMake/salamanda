package moe.salamanda.salamanda.controllers.special;

import moe.salamanda.salamanda.models.student.Activity;
import moe.salamanda.salamanda.models.student.Blog;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Course;
import moe.salamanda.salamanda.repositories.student.ActivityRepository;
import moe.salamanda.salamanda.repositories.student.BlogRepository;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
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
            if(position<0) throw new RuntimeException();
            else{
                Blog blog = blogRepository.getBlogByTime(position);
                map.put("username", blog.getAuthor().getUsername());
                map.put("time", format.format(blog.getDate()));
                map.put("thumbnail", "/util/getThumbnail-chose?username=" + blog.getAuthor().getUsername());
                map.put("content", blog.getContent());
                map.put("title", blog.getTitle());
                map.put("exist", "yes");
            }
        }
        catch (Exception e){
            map.put("exist","no");
            e.printStackTrace();
        }
        return map;
    }

    @Autowired
    CourseRepository courseRepository;

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
            map.put("schedule",course.getSchedule());
            map.put("grade",course.getGrade());
            map.put("limitation",course.getLimitation());
            map.put("description",course.getDescription());
            list.add(map);
        }
        return list;
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
            if (dateStart.equals(null)||dateStart.equals("null")){
                ResponseService.response(response, request, 5);
                return;
            }
            Date start = formatForm.parse(dateStart);
            if(dateEnd.equals(null)||dateEnd.equals("null")){
                ResponseService.response(response, request, 6);
                return;
            }
            Date end = formatForm.parse(dateEnd);
            if(dateEnd.compareTo(dateStart)!=1){
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
}
