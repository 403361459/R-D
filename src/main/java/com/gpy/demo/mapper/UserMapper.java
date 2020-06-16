package com.gpy.demo.mapper;
import com.gpy.demo.entity.Users;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    //add user
    @Insert("INSERT INTO user(id,password,username,phonenum,email,status,code,role) VALUES(default,#{pass},#{name},#{phone},#{email},default,#{code},'user')")
     void Insert(@Param("pass") String pass,@Param("name") String name,@Param("phone") String phone,@Param("email") String email,@Param("code") String code);
   //add user search
    @Select("SELECT COUNT(*) FROM user WHERE username=#{username} or phonenum=#{phonenum}")
    int getusernum(@Param("username")String username,@Param("phonenum")String phonenum);
    //update
    @Update("UPDATE user SET password=#{password},username=#{username},phonenum=#{phonenum},email=#{email} WHERE id=#{id}")
     void updatemyself(@Param("password")String password,@Param("username")String username,@Param("phonenum")String phonenum,@Param("email")String email,@Param("id")int id);
    //check email
    @Select("SELECT id FROM user WHERE code=#{code}")
    int checkcode(String code);

    //update status
    @Update("UPDATE user SET status=1,code=null WHERE id=#{id}")
    void updatestatus(int id);
    //check login
    @Select("SELECT * FROM user WHERE status=1 and (username=#{name} or phonenum=#{name}) and password=#{pass}")
     Users checklogin(@Param("name") String name, @Param("pass") String pass);

    //spring security
    @Select("SELECT * FROM user WHERE (username=#{username} or phonenum=#{username}) and status=1")
    Users check(String username);

    //admin login
    @Select("SELECT admin_id FROM backstage WHERE admin_name=#{name} and admin_pass=#{pass}")
    int login(@Param("name") String name, @Param("pass") String pass);

    //admin add user
    @Insert("INSERT INTO user(id,password,username,phonenum,status,role)VALUES(default,123456,#{username},#{phonenum},1,'user')")
    void admin_addUser(@Param("username")String username,@Param("phonenum")String phonenum);
    //delete
    @Delete("DELETE FROM user WHERE phonenum=#{phonenum}")
    void admin_deleteUser(String phonenum);

    // update user by admin
     @Update("UPDATE user SET username=#{name},phonenum=#{phone},email=#{email} WHERE id=#{id}")
     void admin_updateUser(@Param("name")String name,@Param("phone")String phone,@Param("email")String email,@Param("id")int id);

    //reset pass
    @Update("UPDATE user SET password=123456 WHERE phonenum=#{phonenum}")
    void admin_reset(String phonenum);

    //admin show all user
    @Select("SELECT * FROM user WHERE status=1")
    List<Users> showAlluser();

    //filter user
   @Select("SELECT * FROM user WHERE username LIKE CONCAT('%',#{name},'%')")
   List<Users>shouFilter(String name);
}