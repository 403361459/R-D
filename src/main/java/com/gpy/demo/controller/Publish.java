package com.gpy.demo.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gpy.demo.common.CommonDate;
import com.gpy.demo.entity.Text;
import com.gpy.demo.entity.Users;
import com.gpy.demo.service.MessageService;
import com.gpy.demo.service.TextService;
import com.gpy.demo.service.UserService;
import com.mysql.cj.Session;
import com.sun.media.sound.ModelDestination;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller
public class Publish {
    @Autowired
    private TextService textService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/publish")
    public String Show_publish(Model model)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        Users user=userService.check(username);
        int id=user.getId();
        model.addAttribute("list",textService.show_draft(id));
        model.addAttribute("list2",textService.show_wait(id));
        model.addAttribute("list3",textService.show_nopass(id));
        return "publish";
    }
    //撤回--删除发布
     @RequestMapping("/delete_text")
     @ResponseBody
     public void delete_text(@RequestParam("txtid")int txtid)
     {
      //   User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      //   String username=u.getUsername();
         textService.delete_text(txtid);
         //清空所有关于此发布的消息
         messageService.delete(txtid);
         //清空所有关于此发布对接中信息
         textService.delete_abut(txtid);

//         redisTemplate.opsForValue().set("sys_"+username,messageService.find(username));
//         redisTemplate.opsForValue().set("abut_"+username,messageService.find_user(username));

     }

     //撤销对接-1查询所有相关对接用户
     @RequestMapping("/delete_abut/search")
     @ResponseBody
     public String[] delete_search(@RequestParam("txtid")int txtid)
     {
         return textService.getPerson(txtid);
     }
     //-2删除发布并发送消息通知
    @RequestMapping("/delete_abut/delete")
    @ResponseBody
    public void delete_sendMsg(@RequestParam("txtid")int txtid,@RequestParam("title")String title,@RequestParam("sender")String party_A,@RequestParam("list")String []list)
    {
        textService.delete_abut(txtid);
        textService.update(txtid);
        String msg="对方已撤销和您的对接";
        String sender=party_A;
        Date newDate=new CommonDate().newDate();
        for(int i=0;i<list.length;i++)
        {
           // System.out.print(list[i].replace("\"", ""));
            messageService.insert(msg,sender,list[i].replace("\"", ""),newDate,txtid,title);
            messageService.delete_workexp(txtid,list[i].replace("\"", ""));
        }

    }
    @RequestMapping("/publish/draft")
    public String  Show_draft(@RequestParam("txtid")int txtid,Model model)
    {
        model.addAttribute("list",textService.show_draft_detail(txtid));
        return "draft";
    }
    //返回重新发布页
    @RequestMapping("/publish/republish")
    public String republish(@RequestParam("txtid")int txtid,Model model)
    {
        model.addAttribute("list",textService.updateNopass(txtid));
        return "republish";
    }
     //返回修改页
    @RequestMapping("/publish/update")
    public String text_update(@RequestParam("txtid")int txtid,Model model)
    {
        model.addAttribute("list",textService.updateNopass(txtid));
        return "text_update";
    }

    //修改发布内容
    @RequestMapping("/publish/update/success")
    @ResponseBody
    public void text_update_success(@RequestParam("txtid")int txtid,@RequestParam("text")String text,@RequestParam("title")String title,@RequestParam("speciality")String speciality,@RequestParam("type")int type)
    {
            Date newdate=new CommonDate().newDate();
            textService.text_update(title,text,newdate,speciality,type,txtid);
    }

    @RequestMapping("/draft/delete")
    @ResponseBody
    public void delete_draft(@RequestParam("txtid")int txtid)
    {
        textService.delete_draft(txtid);
    }

    @RequestMapping("/draft/upload")
    @ResponseBody
    public void delete_upload(@RequestParam("txtid")int txtid)
    {
        textService.draft_upload(txtid);
    }

    @RequestMapping("/upload")
    public String upload(HttpServletRequest req)
    {
        String txt=req.getParameter("editor_txt");
        String title=req.getParameter("title");
        //获取登录用户信息
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String author=u.getUsername();
        Users user=userService.check(author);
        int id=user.getId();
        String speciality=req.getParameter("speciality");
        int type=Integer.parseInt(req.getParameter("type"));
        Date newDate=new CommonDate().newDate();
        textService.insert(txt,id,newDate,title,author,speciality,type);
        return "redirect:/index";
    }

    @RequestMapping("/draft")
    @ResponseBody
    public String draft(HttpServletRequest req)
    {
        String txt=req.getParameter("content");
        String title=req.getParameter("title");
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String author=u.getUsername();
        Users user=userService.check(author);
        int id=user.getId();
        String speciality=req.getParameter("speciality");
        int type=Integer.parseInt(req.getParameter("type"));
        Date newDate=new CommonDate().newDate();
        textService.insert_draft(txt,id,newDate,title,author,speciality,type);
        return "redirect:/index";
    }

}
