package moe.salamanda.salamanda.controllers;

import moe.salamanda.salamanda.services.MailService;
import moe.salamanda.salamanda.services.RandomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @RequestMapping("/check-code")
    @ResponseBody
    public String checkCode(@RequestParam(value = "address") String address){
        String code = RandomService.checkCode();
        try {
            MailService.codeSender(address,code);
            return "Success";
        }catch (Exception e){
            return "Failed";
        }
    }
}
