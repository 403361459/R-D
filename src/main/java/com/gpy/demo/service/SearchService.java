package com.gpy.demo.service;

import com.github.pagehelper.PageHelper;
import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Text;
import com.gpy.demo.mapper.SearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private SearchMapper searchMapper;

    public List<Text> getlist(int page, int size, int id, String title,int type,String style, int status, int time)
    {
        PageHelper.startPage(page,size);
        return searchMapper.getlist(id,title,type,style,status,time);
    }

    public List<Text> getOthers(int page,int size,int id,String title,String author,int type,String style,int time)
    {
        PageHelper.startPage(page,size);
        return searchMapper.getOthers(id,title,author,type,style,time);
    }

    public List<Abutment> getResult(int page,int size,String party_A,String party_B,String title,String speciality,int type,int time)
    {
        PageHelper.startPage(page,size);
        return searchMapper.getResult(party_A,party_B,title,speciality,type,time);
    }
}
