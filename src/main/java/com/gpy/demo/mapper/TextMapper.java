package com.gpy.demo.mapper;

import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Draft;
import com.gpy.demo.entity.Text;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Repository
public interface TextMapper {
    //发布并保存
    @Insert("INSERT INTO text(txtid,txtstatus,txtcontent,id,date,title,author,handle,views,speciality,type) VALUES(default,default,#{content},#{id},#{date},#{title},#{author},default,default,#{speciality},#{type} )")
     void insert(@Param("content") String content,@Param("id") int id,@Param("date") Date date,@Param("title") String title,@Param("author")String author,@Param("speciality")String speciality,@Param("type")int type);

    //我的发布--5条
    @Select("SELECT * FROM text WHERE id=#{id} ORDER BY date DESC LIMIT 0,5")
     List<Text>show_myPublish(int id);

    //我的发布--所有
    @Select("SELECT * FROM text WHERE id=#{id} ORDER BY date DESC")
     List<Text>show_myall(int id);

    //其他人的发布--所有
    @Select("SELECT * FROM text WHERE id!=#{id} AND txtstatus=1 ORDER BY date DESC")
    List<Text>show_othersall(int id);

    //我的对接--所有（我是甲方）
    @Select("SELECT * FROM abutment WHERE party_A=#{name}  ORDER BY date DESC")
    List<Abutment>show_myabutment_A(String name);
    //我的对接--所有（我是乙方）
    @Select("SELECT * FROM abutment WHERE party_B=#{name} ORDER BY date DESC")
    List<Abutment>show_myabutment_B(String name);

    //撤回发布--删除发布
    @Delete("DELETE FROM text WHERE txtid=#{txtid}")
     void delete_text(int txtid);
    //撤回对接-1
    @Select("SELECT party_B from abutment WHERE txtid=#{txtid}")
    String[]person(int txtid);
    //-2删除对接
    @Delete("DELETE FROM abutment WHERE txtid=#{txtid}")
    void delete_abut(int txtid);
    //-3更新对接状态为发布状态--用下面update（110）

    //保存至草稿
    @Insert("INSERT INTO draft(txtid,id,content,title,date,author,status,speciality,type) VALUES(default,#{id},#{content},#{title},#{date},#{author},default,#{speciality},#{type})")
    void insert_draft(@Param("content") String content,@Param("id")int id,@Param("date") Date date,@Param("title") String title,@Param("author")String author,@Param("speciality")String speciality,@Param("type")int type);

    //发布页草稿显示--只显示前五条
    @Select("SELECT * FROM draft WHERE id=#{id} and status=0 ORDER BY date DESC LIMIT 0,5")
     List<Draft> show_draft(int id);

    //待审核发布
    @Select("SELECT * FROM text WHERE id=#{id} and txtstatus=0 and handle=0 ORDER BY date DESC")
    List<Text>show_wait(int id);


    //我的未审核通过发布
    @Select("SELECT * FROM text WHERE id=#{id} and txtstatus=0 and handle=1 ORDER BY date DESC")
    List<Text>show_nopass(int id);

    //修改发布页
    @Select("SELECT * FROM text WHERE txtid=#{txtid}")
    List<Text> updateNopass(int txtid);

    //发布内容更新
    @Update("UPDATE text SET title=#{title},txtcontent=#{text},date=#{date},speciality=#{speciality},type=${type} WHERE txtid=${txtid}")
    void text_update(@Param("title")String title,@Param("text")String text,@Param("date")Date date,@Param("speciality")String speciality,@Param("type")int type,@Param("txtid")int txtid);

    //草稿信息
    @Select("SELECT * FROM draft WHERE txtid=#{txtid}")
    List<Draft> draft_detail(int txtid);

    //删除草稿
    @Delete("DELETE FROM draft WHERE txtid=#{txtid}")
    void delete_draft(int txtid);


    //草稿发布-更新状态
    @Update("UPDATE draft SET status=1 WHERE txtid=#{txtid}")
     void update_draft_status(int txtid);

    //管理员页面显示
    @Select("SELECT * FROM text  WHERE handle=0 ORDER BY date DESC")
     List<Text> show_admin();

    //用户首页显示
    @Select("SELECT * FROM text  WHERE txtstatus=1 ORDER BY date DESC")
     List<Text> show();

    //访问量更新
    @Update("UPDATE text SET views=#{count} WHERE txtid=#{txtid}")
    void views_update(@Param("count")int count, @Param("txtid")int txtid);

    //模糊查询-首页
    @Select("SELECT * FROM text WHERE txtstatus=1 and txtcontent like CONCAT('%',#{content},'%')" )
     List<Text>search(String content);

    //模糊查询-用户页
    @Select("SELECT * FROM text WHERE id=#{id} and txtcontent like CONCAT('%',#{content},'%')" )
    List<Text>search_byuser(@Param("id")int id,@Param("content") String content);

    //对接成功-更新text表
    @Update("UPDATE text SET txtstatus=2 WHERE txtid=#{txtid}")
    void update_abut_text(int txtid);

    //更新发布状态 0--未审核发布 1--已通过审核并发布
    @Update("UPDATE text SET txtstatus=1 WHERE txtid=#{txtid}")
     void update(int txtid);

    //更新管理界面待审核的发布
    @Update("UPDATE text SET handle=1 WHERE txtid=#{txtid}")
     void  update_handle(int txtid);

    //详细页
    @Select("SELECT * FROM text WHERE txtid=#{txtid}")
      List<Text> show_detail(int txtid);

    //导航页--需求
    @Select("SELECT * FROM text WHERE id=#{id} and type=0 ORDER BY date DESC LIMIT 0,5")
    List<Text>showMyNeeds(int id);

    //导航页--成果
    @Select("SELECT * FROM text WHERE id=#{id} and type=1 ORDER BY date DESC LIMIT 0,5")
    List<Text>showMyResults(int id);

    //导航页--共发布需求
    @Select("SELECT COUNT(*) FROM text WHERE id=#{id} and type=0")
    int showNeedsNum(int id);

    //导航页-共通过需求
    @Select("SELECT COUNT(*) FROM text WHERE id=#{id} and type=0 and txtstatus!=0")
    int showPassNeedsNum(int id);

    //导航页--共发布成果
    @Select("SELECT COUNT(*) FROM text WHERE id=#{id} and type=1")
    int showResultsNum(int id);

    //导航页--共通过成果
    @Select("SELECT COUNT(*) FROM text WHERE id=#{id} and type=1 and txtstatus!=0")
    int showPassResultsNum(int id);

    //管理员页-共审核
    @Select("SELECT COUNT(*) FROM text ")
    int countAll();

    //审核通过
    @Select("SELECT COUNT(*) FROM text WHERE txtstatus!=0")
    int countPassAll();

    //需求--共发布
    @Select("SELECT COUNT(*) FROM text WHERE type=0")
    int needsCount();

    //需求共通过审核
    @Select("SELECT COUNT(*) FROM text WHERE  txtstatus!=0 and type=0")
    int passNeedsCount();

    //成果--共发布
    @Select("SELECT COUNT(*) FROM text WHERE type=1")
    int resultsCount();

    //成果共通过审核
    @Select("SELECT COUNT(*) FROM text WHERE  txtstatus!=0 and type=1")
    int passResultsCount();


    //所有对接
    @Select("SELECT COUNT(*) FROM abutment")
    int abutAll();

    //需求共对接
    @Select("SELECT COUNT(*) FROM abutment WHERE type=0")
    int needsAbut();

    //需求对接成功
    @Select("SELECT COUNT(*) FROM abutment WHERE type=0 and status=1")
    int needsAbutSuccess();

    //成果共对接
    @Select("SELECT COUNT(*) FROM abutment WHERE type=1")
    int resultsAbut();

    //成果对接成功
    @Select("SELECT COUNT(*) FROM abutment WHERE type=1 and status=1")
    int resultsAbutSuccess();

}
