package com.gpy.demo.service;

import com.github.pagehelper.PageHelper;
import com.gpy.demo.entity.Users;
import com.gpy.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public Users check(String username) {
        return userMapper.check(username);
    }

    public void Insert(String pass, String name, String phone, String email, String code) {
        userMapper.Insert(pass, name, phone, email, code);
    }

    public void updatemyself(String password, String username, String phonenum, String email, int id) {
        userMapper.updatemyself(password, username, phonenum, email, id);
    }

    public int checkcode(String code) {
        return userMapper.checkcode(code);
    }

    public void update(int id) {
        userMapper.updatestatus(id);
    }

    //     public Users checklogin(String name, String pass)
//     {
//         return userMapper.checklogin(name,pass);
//     }
    public int login(String name, String pass) {
        return userMapper.login(name, pass);
    }

    public void addUser(String user, String phone) {
        userMapper.admin_addUser(user, phone);
    }

    public int getusernum(String username, String phonenum) {
        return userMapper.getusernum(username, phonenum);
    }

    public void delUser(String phonenum) {
        userMapper.admin_deleteUser(phonenum);
    }
    public void updateUser(String name,String phone,String email,int id)
    {
        userMapper.admin_updateUser(name,phone,email,id);
    }

    public void resetUser(String phonenum) {
        userMapper.admin_reset(phonenum);
    }

    public List<Users> showAlluser(int page,int size)
    {
        PageHelper.startPage(page,size);
        return userMapper.showAlluser();
    }
    public List<Users>showFilter(int page,int size,String name){
        PageHelper.startPage(page,size);
        return userMapper.shouFilter(name);
    }
}
