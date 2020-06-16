package com.gpy.demo.service;

import com.github.pagehelper.PageHelper;
import com.gpy.demo.entity.Abutment;
import com.gpy.demo.entity.Draft;
import com.gpy.demo.entity.Text;
import com.gpy.demo.mapper.SearchMapper;
import com.gpy.demo.mapper.TextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TextService {
    @Autowired
    private TextMapper textMapper;
    @Autowired
    private SearchMapper searchMapper;

    public void insert(String content, int id, Date date,String title,String author,String speciality,int type)
    {
        textMapper.insert(content,id,date,title,author,speciality,type);
    }

    public void delete_text(int txtid)
    {
        textMapper.delete_text(txtid);
    }

    public String[]getPerson(int txtid){ return textMapper.person(txtid);}

    public void delete_abut(int txtid){textMapper.delete_abut(txtid);}

    public void update_abut_text(int txtid)
    {
        textMapper.update_abut_text(txtid);
    }

//    public List<Text> show_Mypublish(int id)
//    {
//        return  textMapper.show_myPublish(id);
//    }

    public List<Text>show_myall(int page,int size,int id)
    {
        PageHelper.startPage(page,size);
        return textMapper.show_myall(id);
    }
    public List<Text>show_ohtersall(int page,int size,int id)
    {
        PageHelper.startPage(page,size);
        return textMapper.show_othersall(id);
    }


    public void insert_draft(String content,int id,Date date,String title,String author,String speciality,int type)
    {
        textMapper.insert_draft(content,id,date,title,author,speciality,type);
    }
    public List<Draft>show_draft(int id)
    {
        return textMapper.show_draft(id);
    }
    public List<Text>show_wait(int id){return textMapper.show_wait(id);}
    public List<Text>show_nopass(int id){return textMapper.show_nopass(id);}
    public List<Text>updateNopass(int txtid){return textMapper.updateNopass(txtid);}

    public void text_update(String title,String text,Date date,String speciality,int type,int txtid)
    {
        textMapper.text_update(title,text,date,speciality,type,txtid);
    }

    public List<Draft>show_draft_detail(int txtid)
    {
        return textMapper.draft_detail(txtid);
    }

    public void delete_draft(int txtid)
    {
        textMapper.delete_draft(txtid);
    }

    public void draft_upload(int txtid)
    {
        textMapper.update_draft_status(txtid);
    }

    public void update(int txtid)
    {
        textMapper.update(txtid);
    }

    public void update_handle(int txtid)
    {
        textMapper.update_handle(txtid);
    }

    public List<Text> show_admin()
    {
       return textMapper.show_admin();
    }

    public List<Text> show(int page,int size)
    {
        PageHelper.startPage(page ,size );
        return textMapper.show();
    }
    public void views_update(List<Text>list)
    {   for(Text t:list)
    {
        int count=t.getViews();
        int txtid=t.getTxtid();
        textMapper.views_update(count,txtid);
    }
       System.out.print("传值完毕！---------------------------");
    }

    public List<Abutment> show_myabutment_A(int page, int size, String name)
    {
        PageHelper.startPage(page,size);
        return textMapper.show_myabutment_A(name);
    }
    public List<Abutment> show_myabutment_B(int page, int size, String name)
    {
        PageHelper.startPage(page,size);
        return textMapper.show_myabutment_B(name);
    }
    public List<Text>search(String content)
    {
        return  textMapper.search(content);
    }

    public List<Text>search_byuser(int id,String content)
    {
        return  textMapper.search_byuser(id,content);
    }


    public List<Text> show_num()
    {
        return  textMapper.show();
    }
    public List<Text> show_detail(int txtid)
    {
        return  textMapper.show_detail(txtid);
    }

    public List<Text>showMyNeeds(int id)
    {
        return textMapper.showMyNeeds(id);
    }

    public List<Text>showMyResults(int id)
    {
        return textMapper.showMyResults(id);
    }

    public int showNeedsNum(int id)
    {
        return textMapper.showNeedsNum(id);
    }
    public int showPassNeedsNum(int id)
    {
        return textMapper.showPassNeedsNum(id);
    }
    public int showResultsNum(int id)
    {
        return textMapper.showResultsNum(id);
    }
    public int showPassResultsNum(int id)
    {
        return textMapper.showPassResultsNum(id);
    }

    public int countAll()
    {
        return textMapper.countAll();
    }
    public int countPassAll()
    {
        return textMapper.countPassAll();
    }
    public int needsCount()
    {
        return textMapper.needsCount();
    }
    public int passNeedsCount()
    {
        return  textMapper.passNeedsCount();
    }
    public int resultsCount()
    {
        return textMapper.resultsCount();
    }
    public int passResultsCount()
    {
        return textMapper.passResultsCount();
    }

    public int abutAll(){return textMapper.abutAll();}
    public int needsAbut()
    {
        return textMapper.needsAbut();
    }
    public int needsAbutSuccess()
    {
        return textMapper.needsAbutSuccess();
    }
    public int resultsAbut()
    {
        return textMapper.resultsAbut();
    }
    public int resultsAbutSuccess()
    {
        return textMapper.resultsAbutSuccess();
    }


}
