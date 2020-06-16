package com.gpy.demo.common;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CommonDate {
    public Date newDate()
    {
        java.util.Date date=new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置为东八区
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String datestr=sdf.format(date);
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date newDate=null;
        try {
            newDate = df.parse(datestr);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }


}
