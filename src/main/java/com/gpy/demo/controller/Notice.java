package com.gpy.demo.controller;

import com.github.pagehelper.PageInfo;
import com.gpy.demo.common.CommonDate;
import com.gpy.demo.entity.Message;
import com.gpy.demo.entity.Workexp;
import com.gpy.demo.service.MessageService;
import com.gpy.demo.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class Notice {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TextService textService;
    @Autowired
    private RedisTemplate redisTemplate;
    //发送系统消息
    @RequestMapping("/update")
    @ResponseBody
    public void update(HttpServletRequest req,int txtid,String message,String sender,String receiver,String title)
    {
        txtid=Integer.parseInt(req.getParameter("txtid"));
        message="已通过您的发布";
        sender="系统消息";
        receiver=req.getParameter("author");
        title=req.getParameter("title");
        Date newDate=new CommonDate().newDate();
        textService.update(txtid);
        textService.update_handle(txtid);
        messageService.insert(message,sender,receiver,newDate,txtid,title);
//        redisTemplate.opsForValue().set("sys_"+receiver,messageService.find(receiver));

    }

    @RequestMapping("/send")
    @ResponseBody
    public void messageInsert(HttpServletRequest req,int txtid, String message, String sender, String receiver,String title)
    {
      String reason=req.getParameter("reason");
      if(reason=="")
      {
          message="未审核通过";
      }
       else
      {
          message="未审核通过------"+reason;
      }
       sender="系统消息";
       receiver=req.getParameter("author");
       title=req.getParameter("title");
        Date newDate=new CommonDate().newDate();
        txtid=Integer.parseInt(req.getParameter("txtid"));
        textService.update_handle(txtid);
        messageService.insert(message,sender,receiver,newDate,txtid,title);
//       redisTemplate.opsForValue().set("sys_"+receiver,messageService.find(receiver));
    }

    //系统消息
    @RequestMapping("/system_Message")
    public String  show_sys_message(Model model)
    {

        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user=u.getUsername();
//        List<Message>m1=(List<Message>)redisTemplate.opsForValue().get("sys_"+user);
//       if(m1==null)
//      {
//
//          m1=messageService.find(user);
//          redisTemplate.opsForValue().set("sys_"+user,m1);
//          //设置redis过期时间
//          redisTemplate.expire("sys_"+user,60, TimeUnit.MINUTES);
//      }
//      model.addAttribute("list",m1);
        model.addAttribute("list",messageService.find(user));
        return "message";
    }

    //对接消息

    @RequestMapping("/user_Message")
    public String  show_user_message(Model model)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user=u.getUsername();
//        List<Message>m2=(List<Message>)redisTemplate.opsForValue().get("abut_"+user);
//        if(m2==null)
//        {
//            m2=messageService.find_user(user);
//            redisTemplate.opsForValue().set("abut_"+user,m2);
//            //设置redis过期时间
//            redisTemplate.expire("abut_"+user,60, TimeUnit.MINUTES);
//        }
//        model.addAttribute("mylist",m2);
        model.addAttribute("mylist",messageService.find_user(user));
        return "myMessage";
    }
    @RequestMapping("/user_Message/user_details")
    public String getUserDetails(@RequestParam("user")String user,@RequestParam("txtid")int txtid, Model model)
    {
        List<Workexp>userList=messageService.getMessage(txtid,user);
        model.addAttribute("list",userList);
        return "user_details";
    }

     @RequestMapping("/already_read_all")
     @ResponseBody
    public void handel_all(HttpServletRequest req)
     {
        String receiver=req.getParameter("author");
         messageService.handel_all(receiver);
//         redisTemplate.opsForValue().set("sys_"+receiver,messageService.find(receiver));
     }

    @RequestMapping("/already_read")
    @ResponseBody
    public void handel_one(HttpServletRequest req)
    {
        int id=Integer.parseInt(req.getParameter("id"));
       // String receiver=req.getParameter("author");
        messageService.handel_one(id);
//        redisTemplate.opsForValue().set("sys_"+receiver,messageService.find(receiver));
    }

    //发送对接请求
    @RequestMapping("/abut")
    @ResponseBody
    public void abut_insert(HttpServletRequest req)
    {
        String party_A=req.getParameter("party_A");
        String party_B=req.getParameter("party_B");
        String title=req.getParameter("title");
        int txtid=Integer.parseInt(req.getParameter("txtid"));
        String realname=req.getParameter("realname");
        String exp=req.getParameter("experience");
        String user=req.getParameter("user");
        int userid=Integer.parseInt(req.getParameter("userid"));
        Date newDate=new CommonDate().newDate();
        int type=Integer.parseInt(req.getParameter("type"));
        String speciality=req.getParameter("speciality");
        String message="来自"+party_B+"的对接请求";
        //判断是否重复提交对接//在对方未处理前更新信息
        if(messageService.sendCount(party_B,txtid)>0)
        {
            //更新对接表
            messageService.resendAbut(newDate,party_B,txtid);
            //更新履历表
            messageService.resendMine(realname,exp,party_B,txtid);
            //更新发送信息的时间
            messageService.reAbut_update(newDate,txtid);
//            redisTemplate.opsForValue().set("abut_"+party_A,messageService.find_user(party_A));
        }
        else
        {
            //插入个人简历表
            messageService.exp_insert(realname,exp,txtid,userid,user);
            //插入对接表
            messageService.abut(party_A,party_B,newDate,title,txtid,type,speciality);
            //发送消息
            messageService.insert_user(message,party_B,party_A,newDate,txtid,title);
//            redisTemplate.opsForValue().set("abut_"+party_A,messageService.find_user(party_A));
        }

    }
    //更改对接状态
    @RequestMapping("/abut_result")
    @ResponseBody
    public void abut_result(HttpServletRequest req)
    {     Date newDate=new CommonDate().newDate();
          String sender=req.getParameter("sender");
          String receiver=req.getParameter("receiver");
          String title=req.getParameter("title");
          int id=Integer.parseInt(req.getParameter("id"));//信息id
          int txtid=Integer.parseInt(req.getParameter("txtid"));//文章id
          int status=Integer.parseInt(req.getParameter("status"));
          String message=req.getParameter("message");
          if(status==1)//通过
          {
              messageService.abut_success(txtid,receiver);
              textService.update_abut_text(txtid);
              messageService.insert(message,sender,receiver,newDate,txtid,title);//系统通知
              messageService.handel_one(id);
              //更新缓存
//              redisTemplate.opsForValue().set("abut_"+sender,messageService.find_user(sender));
//              redisTemplate.opsForValue().set("sys_"+receiver,messageService.find(receiver));
          }
          else
          {
              messageService.insert(message,sender,receiver,newDate,txtid,title);
              messageService.handel_one(id);
              //删除对接和个人表,删除信息表的对接请求
              messageService.delete_workexp(txtid,receiver);
              messageService.delete_abut(txtid,receiver);

//              redisTemplate.opsForValue().set("abut_"+sender,messageService.find_user(sender));
//              redisTemplate.opsForValue().set("sys_"+receiver,messageService.find(receiver));
          }

    }

    @RequestMapping("/newMessage")
    public String newMessage(@RequestParam("receiver") String receiver,HttpSession session)
    {

        session.setAttribute("num",messageService.find(receiver).size());
        session.setAttribute("num2",messageService.find_user(receiver).size());
        return "redirect:/index";
    }

      @RequestMapping("/history")
        public String history(@RequestParam(name="page",defaultValue = "1")int page, @RequestParam(value = "size",defaultValue ="10")int size,Model model)
      {
          User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          String user=u.getUsername();
          List<Message>list=messageService.myhistory(page,size,user);
          PageInfo<Message>pageInfo=new PageInfo<>(list);
          model.addAttribute("content",pageInfo.getList());
          model.addAttribute("list",pageInfo);
          return "history_msg";
      }

}
