package com.gpy.demo.controller;
import com.gpy.demo.common.IDUtils;
import com.gpy.demo.entity.Users;
import com.gpy.demo.service.MailService;
import com.gpy.demo.service.MessageService;
import com.gpy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class Checkuser {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private  MessageService messageService;
//    @RequestMapping("/login")
//    public String login(Users user, HttpSession session, HttpServletResponse resp)
//    {
//        String s1=user.getUsername();
//        String s2=user.getPassword();
//
//       Users u= userService.checklogin(s1,s2);
//       if(u!=null) {
//           session.setAttribute("name",u.getUsername());
//           session.setAttribute("id",u.getId());
//           session.setAttribute("num",messageService.find(u.getUsername()).size());
//           session.setAttribute("num2",messageService.find_user(u.getUsername()).size());
//           try {
//               resp.sendRedirect("http://localhost:8080/index");
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
//       }
//        return "error";
//
//    }

    @RequestMapping("/register")
    public void register(String username,String password,String emailaddress,String phonenum)
    {
       String name=username;
       String pass=password;
       String email=emailaddress;
       String phone=phonenum;
       String code= IDUtils.getUUID()+ IDUtils.getUUID();
       userService.Insert(pass,name,phone,email,code);
        String subject = "来自gpy的激活邮件";
        //user/checkCode?code=code(激活码)是我们点击邮件链接之后根据激活码查询用户，如果存在说明一致，将用户状态修改为“1”激活
        //上面的激活码发送到用户注册邮箱
        String context = "<a href=\"http://localhost:8080/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
        //发送激活邮件
        mailService.sendHtmlMail (email,subject,context);
    }

    @RequestMapping("/checkCode")
    public void checkcode(String code,HttpServletResponse resp)
    {
      if (userService.checkcode(code)>0)
      {
         userService.update(userService.checkcode(code));
      }
      else
      {
          try {
              resp.sendRedirect("http://localhost:8080/registerstart");
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
        try {
            resp.sendRedirect("http://localhost:8080/index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/update_myself")
    public String update_myself(@RequestParam("username")String username,@RequestParam("password")String password,@RequestParam("phonenum")String phonenum,@RequestParam("email")String email,@RequestParam("id")int id,HttpSession session)
    {
        userService.updatemyself(username,password,phonenum,email,id);
        session.invalidate();
        return "redirect:/loginstart";
    }
    @RequestMapping("/logout")
    public void logout(HttpSession session,HttpServletResponse resp)
    {
        session.invalidate();
        try {
            resp.sendRedirect("http://localhost:8080/index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
