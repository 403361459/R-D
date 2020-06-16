package com.gpy.demo.controller;
import com.github.pagehelper.PageInfo;
import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Message;
import com.gpy.demo.entity.Text;
import com.gpy.demo.entity.Users;
import com.gpy.demo.service.MessageService;
import com.gpy.demo.service.SearchService;
import com.gpy.demo.service.TextService;
import com.gpy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class Textshow {

    @Autowired
    private TextService textService;

    @Autowired
    private UserService userService;

    @Autowired
    private SearchService searchService;

    @Resource
    private RedisTemplate<String,Integer>redisTemplate;

    //首页展示-index
    @RequestMapping({"/index","/"})
    public String  show(@RequestParam(name="page",defaultValue = "1")int page, @RequestParam(value = "size",defaultValue ="5")int size, Model model) {
        List<Text> list = textService.show(page, size);
        PageInfo<Text> pageInfo = new PageInfo<>(list);//pageInfo 封装
        model.addAttribute("list", pageInfo);
        model.addAttribute("content", pageInfo.getList());
        return "indexpage";
    }

   //详细页
    @RequestMapping("/index/detail")
    public String show_details(@RequestParam(name="txtid")int txtid,Model model)
    {
        List<Text>list =textService.show_detail(txtid);
        model.addAttribute("list",list);
        return  "details";
    }


    //访问量--入redis
    @ResponseBody
    @RequestMapping("/views_count")
    public void count(@RequestParam(name="user")String user,@RequestParam(name="txtid")int txtid)
    {
        if(!redisTemplate.hasKey("views_"+txtid))
        {
            redisTemplate.opsForValue().set("views_"+txtid,1);

        }
        int res=redisTemplate.opsForValue().get("views_"+txtid);
        redisTemplate.opsForValue().set("views_"+txtid,res+1);
    }


    //redis同步至数据库--每日凌晨1点自动开始同步
    @Scheduled(cron = "0 0 1 * * ?")//  * /2 * * * * ？
    public void sync_count()
    {
        List<Text> List=new ArrayList<>();
        Set<String> keySet = redisTemplate.keys("views_*");
        int length="views_".length();
        for(String str:keySet)
        {
            Text text=new Text();
            text.setTxtid(Integer.parseInt(str.substring(length)));
            text.setViews(redisTemplate.opsForValue().get(str));
            List.add(text);
        }
      textService.views_update(List);
    }

      
      //管理员界面--详细页
    @RequestMapping("/show/detail")
    public String show_details_admin(@RequestParam(name="txtid")int txtid,Model model)
    {
        List<Text>list =textService.show_detail(txtid);
        model.addAttribute("list",list);
        return  "details_admin";
    }

//    //模糊查询--首页
//    @RequestMapping("/index/search")
//    @ResponseBody
//    public void search(@RequestParam("content") String content,HttpSession session)
//    {
//        session.setAttribute("content",textService.search(content));
//    }
//


    //我的发布
    @RequestMapping("/mypublish")
    public String show_myall(@RequestParam(name="page",defaultValue = "1")int page, @RequestParam(value = "size",defaultValue ="10")int size, Model model)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        Users user=userService.check(username);
        int id=user.getId();
        List<Text>list=textService.show_myall(page,size,id);
        PageInfo<Text>pageInfo=new PageInfo<>(list);
        model.addAttribute("content",pageInfo.getList());
        model.addAttribute("list",pageInfo);
        return "publish_Filter";
    }
    //我的发布-条件筛选页
    @RequestMapping("/publish/filter")
    public String  toFilter(@RequestParam(name="page",defaultValue = "1")int page, @RequestParam(value = "size",defaultValue ="10")int size,Model model, @RequestParam(name="title")String title,@RequestParam(name="type")int type,@RequestParam(name="style")String style,@RequestParam(name="status")int status,@RequestParam(name="time")int time)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        Users user=userService.check(username);
        int id=user.getId();
        System.out.println(style);
        List<Text> list=searchService.getlist(page,size,id,title,type,style,status,time);
        PageInfo<Text> pageInfo=new PageInfo<>(list);
        model.addAttribute("content",pageInfo.getList());
        model.addAttribute("list",pageInfo);
        return "publish_Filter";
    }
    //查询其他人发布-默认所有页
    @RequestMapping("/publish/others")
    public String  toOthersPublish(@RequestParam(name="page",defaultValue = "1")int page, @RequestParam(value = "size",defaultValue ="10")int size,Model model)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        Users user=userService.check(username);
        int id=user.getId();
        List<Text>list=textService.show_ohtersall(page,size,id);
        PageInfo<Text> pageInfo=new PageInfo<>(list);
        model.addAttribute("content",pageInfo.getList());
        model.addAttribute("list",pageInfo);
        return "others_Filter";
    }

    //查询其他人的发布-条件筛选
    @RequestMapping("/publish/others/filter")
    public String  toOthersPublish_Filter(@RequestParam(name="page",defaultValue = "1")int page, @RequestParam(value = "size",defaultValue ="10")int size,Model model, @RequestParam(name="title")String title,@RequestParam(name="author")String author,@RequestParam(name="type")int type,@RequestParam(name="style")String style,@RequestParam(name="time")int time)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        Users user=userService.check(username);
        int id=user.getId();
        List<Text> list=searchService.getOthers(page,size,id,title,author,type,style,time);
        PageInfo<Text> pageInfo=new PageInfo<>(list);
        model.addAttribute("content",pageInfo.getList());
        model.addAttribute("list",pageInfo);
        return "others_Filter";
    }

    //我的对接--首页
    @RequestMapping("/myabutment")
    public  String show_myabutment(@RequestParam(name="page",defaultValue = "1")int page,@RequestParam(name="page2",defaultValue = "1")int page2,@RequestParam(value = "size",defaultValue ="5")int size, Model model)
    {
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        List<Abutment>list=textService.show_myabutment_A(page,size,username);
        List<Abutment>list2=textService.show_myabutment_B(page2,size,username);
        PageInfo<Abutment>pageInfo=new PageInfo<>(list);
        PageInfo<Abutment>pageInfo2=new PageInfo<>(list2);
        model.addAttribute("content",pageInfo.getList());
        model.addAttribute("list",pageInfo);
        model.addAttribute("content2",pageInfo2.getList());
        model.addAttribute("list2",pageInfo2);
        return "user_abutment";
    }

    //导航页--登录跳转页
    @RequestMapping("/startPage")
    public String start(Model model){
        User u=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=u.getUsername();
        Users user=userService.check(username);
        int id=user.getId();
        model.addAttribute("needsNum",textService.showNeedsNum(id));
        model.addAttribute("passNeedsNum",textService.showPassNeedsNum(id));

        model.addAttribute("resultsNum",textService.showResultsNum(id));
        model.addAttribute("passResultsNum",textService.showPassResultsNum(id));

        model.addAttribute("needs",textService.showMyNeeds(id));
        model.addAttribute("results",textService.showMyResults(id));
        return "startPage";
    }

}
