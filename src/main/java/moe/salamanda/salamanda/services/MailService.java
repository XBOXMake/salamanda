package moe.salamanda.salamanda.services;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    public static void codeSender(String address,String checkCode) throws MessagingException{
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(25);
        mailSender.setUsername("");
        mailSender.setPassword("");
        mailSender.setDefaultEncoding("UTF-8");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,false,"UTF-8");
        helper.setFrom("xboxmake@163.com");
        helper.setTo(address);
        helper.setBcc("xboxmake.avi@gmail.com");
        helper.setSubject("Thank you for using Salamanda Management System");
        helper.setText("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Mail Message</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h3>Thank you for using the Salamanda Management System</h3>\n" +
                "        <div>Dear User ("+address+"):</div>\n" +
                "        <div>Here is your check code</div>\n" +
                "        <div>("+checkCode+")</div>\n" +
                "        <div>Hope you have an amazing experience in this software</div>\n" +
                "        <div style=\"text-align:right;\">yours</div>\n" +
                "        <div style=\"text-align:right;\">PointAAAAA</div>\n" +
                "    </body>\n" +
                "</html>",true);
        mailSender.send(message);
    }
    public static void passwordSender(String address,String password) throws MessagingException{
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(25);
        mailSender.setUsername("xboxmake@163.com");
        mailSender.setPassword("LDEMFWHOIHQWVUQK");
        mailSender.setDefaultEncoding("UTF-8");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,false,"UTF-8");
        helper.setFrom("xboxmake@163.com");
        helper.setTo(address);
        helper.setBcc("xboxmake.avi@gmail.com");
        helper.setSubject("Thank you for using Salamanda Management System");
        helper.setText("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Mail Message</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h3>Thank you for using the Salamanda Management System</h3>\n" +
                "        <div>Dear User ("+address+"):</div>\n" +
                "        <div>Here is your new password</div>\n" +
                "        <div>("+password+")</div>\n" +
                "        <div>Hope you have an amazing experience in this software</div>\n" +
                "        <div style=\"text-align:right;\">yours</div>\n" +
                "        <div style=\"text-align:right;\">PointAAAAA</div>\n" +
                "    </body>\n" +
                "</html>",true);
        mailSender.send(message);
    }
}
