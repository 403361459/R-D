package com.gpy.demo.service;

import com.github.pagehelper.PageHelper;
import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Message;
import com.gpy.demo.entity.Workexp;
import com.gpy.demo.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public void insert(String message, String sender, String receiver, Date date,int txtid,String title)
    {
        messageMapper.insert(message,sender,receiver,date,txtid,title);
    }
    public void insert_user(String message, String sender, String receiver, Date date,int txtid,String title)
    {
        messageMapper.insert_user(message,sender,receiver,date,txtid,title);
    }
    public List<Message> myhistory(int page,int size,String receiver)
    {
        PageHelper.startPage(page ,size);
        return messageMapper.myhistory(receiver);
    }

    public void reAbut_update(Date date,int txtid)
    {
        messageMapper.reAbut_update(date,txtid);
    }

    public List<Message> find(String receiver)
    {
       return  messageMapper.find(receiver);
    }
    public List<Message> find_user(String receiver)
    {
        return  messageMapper.find_user(receiver);
    }

    public void handel_all(String receiver)
    {
        messageMapper.handel_all(receiver);
    }

    public void handel_one(int  id)
    {
        messageMapper.handel_one(id);
    }

    public  void delete(int txtid)
    {
        messageMapper.delete(txtid);
    }
    public void abut(String party_A,String party_B,Date date,String title,int txtid,int type,String speciality)
    {
        messageMapper.abut_insert(party_A,party_B,date,title,txtid,type,speciality);
    }
    public void exp_insert(String realname,String exp,int txtid,int userid,String user)
    {
        messageMapper.exp_insert(realname,exp,txtid,userid,user);
    }
    public List<Workexp> getMessage(int txtid, String user)
    {
       return  messageMapper.getMessage(txtid,user);
    }

    public void abut_success(int txtid,String receiver)
    {
        messageMapper.abut_success(txtid,receiver);
    }

    public List<Abutment> show_abut(int page,int size)
    {
        PageHelper.startPage(page ,size);
        return  messageMapper.show_abut();
    }
//    public List<Abutment> show_abut_all()
//    {
//        return  messageMapper.show_abut();
//    }


    public void delete_abut(int txtid,String party_B)
    {
        messageMapper.delete_abut(txtid,party_B);
    }
    public void delete_workexp(int txtid,String user)
    {
        messageMapper.delete_workexp(txtid,user);
    }


    public int sendCount(String user,int txtid)
    {
        return messageMapper.sendCount(user,txtid);
    }

    public void resendAbut(Date date,String user,int txtid)
    {
        messageMapper.resendAbut(date,user,txtid);
    }

    public void resendMine(String realname,String experience,String user,int txtid)
    {
        messageMapper.resendMine(realname,experience,user,txtid);
    }

}
