package com.gpy.demo.controller;

import com.gpy.demo.entity.Abutment;
import com.gpy.demo.service.MessageService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class Excel {
    @Autowired
    MessageService messageService;
    @RequestMapping("/ExcelDownload")
    @ResponseBody
    public void excelDownload(@RequestBody List<Abutment>list, HttpSession session) throws IOException {
        //获取所有的信息
        session.setAttribute("abutlist", list);
//          System.out.println("session存储成功！");
//          for(int i=0;i<list.size();i++){
//            System.out.print(list.get(i).getParty_A());
//            System.out.print(list.get(i).getParty_B());
//            System.out.print(list.get(i).getTitle());
//            System.out.print(list.get(i).getType());
//            System.out.print(list.get(i).getDate());
//    }
    }

    @RequestMapping("/ExcelDownload/download")
    public void excel (HttpServletResponse response,HttpServletRequest req) throws IOException {
      //表头数据
      String[] header = {"甲方","乙方","项目","类型","时间"};

       //声明一个工作簿
       HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格
       HSSFSheet sheet = workbook.createSheet("对接信息表");

      //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(25);

        //创建标题的显示样式
       HSSFCellStyle headerStyle = workbook.createCellStyle();

       //创建第一行表头
       HSSFRow headrow = sheet.createRow(0);

      //遍历添加表头
      for (int i = 0; i < header.length; i++) {
           //创建一个单元格
          HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
         HSSFRichTextString text = new HSSFRichTextString(header[i]);

            //将内容对象的文字内容写入到单元格中
           cell.setCellValue(text);
         cell.setCellStyle(headerStyle);
        }
        List<Abutment>list=(List<Abutment>)req.getSession().getAttribute("abutlist");
        //获取所有的信息
        for(int i=0;i<list.size();i++) {
            HSSFRow row1 = sheet.createRow(i+1);
            //第1列创建并赋值
            row1.createCell(0).setCellValue(new HSSFRichTextString(list.get(i).getParty_A()));
            row1.createCell(1).setCellValue(new HSSFRichTextString(list.get(i).getParty_B()));
            //第3列创建并赋值
            row1.createCell(2).setCellValue(new HSSFRichTextString(list.get(i).getTitle()));
            //第4列创建并赋值
            if (list.get(i).getType() == 0) {
                row1.createCell(3).setCellValue(new HSSFRichTextString("需求"));
            }
            else
                {
                    row1.createCell(3).setCellValue(new HSSFRichTextString("成果"));
                }
            //第5列创建并赋值
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String time = sdf.format(list.get(i).getDate());
           row1.createCell(4).setCellValue(new HSSFRichTextString(time));
        }

       //准备将Excel的输出流通过response输出到页面下载
        response.setContentType("application/octet-stream");
       //这后面可以设置导出Excel的名称
       response.setHeader("Content-disposition", "attachment;filename=Abutment.xls");
       response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载
      workbook.write(response.getOutputStream());

    }
}
