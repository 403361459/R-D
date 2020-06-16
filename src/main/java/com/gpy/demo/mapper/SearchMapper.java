package com.gpy.demo.mapper;

import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Text;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface SearchMapper {
    List<Text> getlist(@Param("id")int id,@Param("title") String title,@Param("type")int type,@Param("style") String style,@Param("status") int status,@Param("time")int time);

    List<Text> getOthers(@Param("id")int id,@Param("title") String title, @Param("author") String author,@Param("type")int type, @Param("style") String style,@Param("time")int time);

    List<Abutment> getResult(@Param("party_A")String party_A, @Param("party_B")String party_B, @Param("title")String title,@Param("speciality")String speciality, @Param("type")int type, @Param("time")int time);

}
