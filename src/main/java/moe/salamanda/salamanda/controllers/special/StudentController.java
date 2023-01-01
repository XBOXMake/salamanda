package moe.salamanda.salamanda.controllers.special;

import moe.salamanda.salamanda.models.student.Blog;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Course;
import moe.salamanda.salamanda.repositories.student.BlogRepository;
import moe.salamanda.salamanda.repositories.student.StudentRepository;
import moe.salamanda.salamanda.repositories.teacher.CourseRepository;
import moe.salamanda.salamanda.services.RedisService;
import moe.salamanda.salamanda.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

}
