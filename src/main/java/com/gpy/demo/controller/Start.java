package com.gpy.demo.controller;

import com.github.pagehelper.PageInfo;
import com.gpy.demo.entity.Text;
import com.gpy.demo.entity.Users;
import com.gpy.demo.service.TextService;
import com.gpy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class Start {
    @Autowired
    private TextService textService;
    @Autowired
    private UserService  userService;
    @RequestMapping("/loginstart")
    public String show_login()
    {
        return   "login";
    }

    @RequestMapping("/registerstart")
    public String show_register()
    {
        return "register";
    }

    @RequestMapping("/admin")
    public String show_admin()
    {
        return "admin_login";
    }

    @RequestMapping("/updatestart")
    public String show_update()
    {
        return "update";
    }

    @RequestMapping("/tologin")
    public String tologin(){
        return "redirect:/startPage";
    }

   }
