package moe.salamanda.salamanda.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseService {
    public static void successResponse(HttpServletRequest request, HttpServletResponse response,String message) throws Exception{
        response.setContentType("application/json;charset-UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("message",message);
        result.put("status",200);
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
    public static void failureResponse(HttpServletRequest request, HttpServletResponse response,String message) throws Exception{
        response.setContentType("application/json;charset-UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result = new HashMap<>();
        result.put("message",message);
        result.put("status",500);
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
    public static void failureResponse(HttpServletRequest request, HttpServletResponse response,Exception exception) throws Exception{
        response.setContentType("application/json;charset-UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result = new HashMap<>();
        result.put("message",exception.getMessage());
        result.put("status",500);
        String str = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(str);
    }
}
