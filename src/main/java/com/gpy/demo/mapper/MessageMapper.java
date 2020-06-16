package com.gpy.demo.mapper;

import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Message;
import com.gpy.demo.entity.Workexp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageMapper {
    //插入信息-------系统消息
    @Insert("INSERT INTO message(id,message_content,sender,receiver,date,status,kind,txtid,title) VALUES (default,#{Message},#{sender},#{receiver},#{date},default,default,#{txtid},#{title})")
     void insert(@Param("Message") String message, @Param("sender") String sender,@Param("receiver") String receiver,@Param("date") Date date,@Param("txtid")int txtid,@Param("title")String title);

    //插入信息-------非系统消息
    @Insert("INSERT INTO message(id,message_content,sender,receiver,date,status,kind,txtid,title) VALUES (default,#{Message},#{sender},#{receiver},#{date},default,1,#{txtid},#{title})")
     void insert_user(@Param("Message") String message, @Param("sender") String sender,@Param("receiver") String receiver,@Param("date") Date date,@Param("txtid")int txtid,@Param("title")String title);

    //查询个人消息记录
    @Select("SELECT * FROM message WHERE receiver=#{receiver} ORDER BY date DESC")
    List<Message> myhistory(String receiver);

    //更新对接信息时间
    @Update("UPDATE message SET date=#{date} WHERE txtid=#{txtid}")
    void reAbut_update(@Param("date")Date date,@Param("txtid")int txtid);

    //用户查找新消息--系统
    @Select("SELECT * from message WHERE status=0 and kind=0 and receiver=#{receiver} ORDER BY date DESC")
    List<Message> find(String receiver);

    //用户查找新消息--非系统
    @Select("SELECT * from message WHERE status=0 and kind=1 and receiver=#{receiver} ORDER BY date DESC")
     List<Message> find_user(String receiver);


    //用户处理系统消息--全部已读
    @Update("UPDATE message SET status=1 WHERE receiver=#{receiver} and kind=0")
     void handel_all(String receiver);

    //用户处理系统消息--单条已读--根据信息id
    @Update("UPDATE message SET status=1 WHERE id=#{id}")
     void handel_one(int id);

    //用户对接
    @Insert("INSERT INTO abutment(id,party_A,party_B,date,status,title,txtid,type,speciality) VALUES (default,#{party_A},#{party_B},#{date},default,#{title},#{txtid},#{type},#{speciality})")
     void abut_insert(@Param("party_A")String party_A,@Param("party_B")String party_B,@Param("date")Date date,@Param("title")String title,@Param("txtid")int txtid,@Param("type")int type,@Param("speciality")String speciality);

    //查询是否多次发送对接
    @Select("SELECT COUNT(*) FROM abutment WHERE party_B=#{user} and txtid=#{txtid}")
    int sendCount(@Param("user")String user,@Param("txtid")int txtid);

    //重复发送对接--覆盖上次信息
    @Update("UPDATE abutment SET date=#{date} WHERE party_B=#{user} and txtid=#{txtid}")
    void resendAbut(@Param("date")Date date,@Param("user")String user,@Param("txtid")int txtid);

    //用户个人简历介绍
    @Insert("INSERT INTO workexperience(id,realname,experience,txtid,userid,user) VALUES (default,#{realname},#{exp},#{txtid},#{userid},#{user})")
    void exp_insert(@Param("realname")String realname,@Param("exp")String exp,@Param("txtid")int txtid,@Param("userid")int userid,@Param("user")String user);

   //覆盖个人简历
    @Update("UPDATE workexperience SET realname=#{realname} , experience=#{experience} WHERE user=#{user} and txtid=#{txtid}")
    void resendMine(@Param("realname")String realname,@Param("experience")String experience,@Param("user")String user,@Param("txtid")int txtid);

    //查询个人信息
    @Select("SELECT * FROM workexperience WHERE txtid=#{txtid} and user=#{user}")
    List<Workexp>getMessage(@Param("txtid")int txtid,@Param("user")String user);

    //清空某txtid对接消息--在撤回某发布后
    @Update("DELETE FROM message WHERE txtid=#{txtid}")
    void delete(int txtid);

   //用户拒绝对接后-------------------

    //-删除对接表
    @Delete("DELETE FROM abutment WHERE txtid=#{txtid} and party_B=#{party_B}")
    void delete_abut(@Param("txtid")int txtid,@Param("party_B")String party_B);
    //-删除个人信息表
    @Delete("DELETE FROM workexperience WHERE txtid=#{txtid} and user=#{user}")
    void delete_workexp(@Param("txtid")int txtid,@Param("user")String user);

    //-------------------------------

    //用户对接成功
    @Update("UPDATE abutment SET status=1 WHERE txtid=#{txtid} and party_B=#{receiver}")
     void abut_success(@Param("txtid")int txtid,@Param("receiver")String receiver);

    //显示对接信息
    @Select("SELECT * FROM abutment WHERE status=1 ORDER BY date DESC")
     List<Abutment> show_abut();

}

