package com.gpy.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Text;
import com.gpy.demo.entity.Users;
import com.gpy.demo.service.MessageService;
import com.gpy.demo.service.SearchService;
import com.gpy.demo.service.TextService;
import com.gpy.demo.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@Controller
public class Administrator {
    @Autowired
    private UserService userService;
    @Autowired
    private TextService textService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SearchService searchService;
    @RequestMapping("/admin-login")
    public String login(HttpServletRequest req, HttpServletResponse resp){
        String name=req.getParameter("name");
        String pass=req.getParameter("pass");

        if(userService.login(name,pass)>0)
        {
            try {
                resp.sendRedirect("http://localhost:8080/show");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error";
    }
    @RequestMapping("/show")
    public  String show(Model model)
    {
        List<Text> list=textService.show_admin();
        model.addAttribute("list",list);
        model.addAttribute("countAll",textService.countAll());
        model.addAttribute("countPassAll",textService.countPassAll());
        model.addAttribute("needs",textService.needsCount());
        model.addAttribute("passNeeds",textService.passNeedsCount());
        model.addAttribute("results",textService.resultsCount());
        model.addAttribute("passResults",textService.passResultsCount());
        return "admin_index";
    }
//    @RequestMapping("/update")
//    @ResponseBody
//    public void update(HttpServletRequest req,int txtid,String author)
//    {
//       txtid=Integer.parseInt(req.getParameter("txtid"));
//       author=req.getParameter("author");
//        textService.update(txtid);
//
//    }
//    @RequestMapping("/send")
//    public void send(HttpServletRequest req,int txtid,String author)
//    {
//        txtid=Integer.parseInt(req.getParameter("txtid"));
//        author=req.getParameter("author");
//    }
      @RequestMapping("/show_abut")
      public String show_abut(@RequestParam(name="page",defaultValue="1")int page,@RequestParam(value = "size",defaultValue ="5")int size,Model model)
      {
          List<Abutment>list=messageService.show_abut(page,size);
          PageInfo<Abutment> pageInfo = new PageInfo<>(list);//pageInfo 封装
          model.addAttribute("list",pageInfo);
          model.addAttribute("content",pageInfo.getList());
          model.addAttribute("unhandled",textService.abutAll());
          //需求
          model.addAttribute("needsAbut",textService.needsAbut());
          model.addAttribute("needsAbutSuccess",textService.needsAbutSuccess());
          //成果
          model.addAttribute("resultsAbut",textService.resultsAbut());
          model.addAttribute("resultsAbutSuccess",textService.resultsAbutSuccess());
          return "admin_showabut";
      }
      //用户管理
       @RequestMapping("/user_Management")
       public String user_control(@RequestParam(name="page",defaultValue="1")int page,@RequestParam(value = "size",defaultValue ="10")int size,Model model)
      {
          List<Users>list=userService.showAlluser(page,size);
          PageInfo<Users>pageInfo=new PageInfo<>(list);
          model.addAttribute("list",pageInfo);
          model.addAttribute("content",pageInfo.getList());
          return "admin_userManagement";
      }

      //add
     @RequestMapping("/user_Management/add")
     @ResponseBody
     public void adduser(@RequestParam("user")String user,@RequestParam("phonenum")String phonenum)
      {
          userService.addUser(user,phonenum);
       }
       //search
       @RequestMapping("/user_Management/search")
       @ResponseBody
       public String searchuser(@RequestParam("user")String user,@RequestParam("phonenum")String phonenum)
       {
           if(userService.getusernum(user,phonenum)>0)
           {
               return "error";
           }
           else
               return "success";
       }
       //user filter
       @RequestMapping("/user_Management/userFilter")
       public String userFilter(@RequestParam(name="page",defaultValue="1")int page,@RequestParam(value = "size",defaultValue ="10")int size,@RequestParam("searchUser")String name,Model model)
       {   List<Users>list=userService.showFilter(page,size,name);
           PageInfo<Users>pageInfo=new PageInfo<>(list);
           model.addAttribute("list",pageInfo);
           model.addAttribute("content",pageInfo.getList());
           return "admin_userManagement";
       }

       //delete
    @RequestMapping("/user_Management/delete")
    @ResponseBody
    public void deleteUser(@RequestParam("phone")String phone)
    {
        userService.delUser(phone);
    }
      //edit
    @RequestMapping("/user_Management/update")
    @ResponseBody
    public void updateUser(@RequestParam("name")String name,@RequestParam("phone")String phone,@RequestParam("email")String email,@RequestParam("id")int id)
    {
          userService.updateUser(name,phone,email,id);
    }
    //reset
    @RequestMapping("/user_Management/reset")
    @ResponseBody
    public void reset(@RequestParam("phone")String phone)
    {
        userService.resetUser(phone);
    }

    //filter -abut
    @RequestMapping("/abutFilter")
    public String abutFilter(@RequestParam(name="page",defaultValue="1")int page,@RequestParam(value = "size",defaultValue ="10")int size,@RequestParam("A")String A,@RequestParam("B")String B,@RequestParam("title")String title,@RequestParam("speciality")String speciality,@RequestParam("type")int type,@RequestParam("time")int time,Model model)
    {
        List<Abutment>list=searchService.getResult(page,size,A,B,title,speciality,type,time);
        PageInfo<Abutment> pageInfo = new PageInfo<>(list);
        model.addAttribute("list",pageInfo);
        model.addAttribute("content",pageInfo.getList());
        return "admin_Filter";

    }

}
