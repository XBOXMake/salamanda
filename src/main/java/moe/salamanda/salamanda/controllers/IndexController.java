package moe.salamanda.salamanda.controllers;

import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    private final String INDEX_ERROR_PREFIX = "SOMETHING_GOING_WRONG__INDEX_CONTROLLER__";

    @Autowired
    RedisService redisService;

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
}
