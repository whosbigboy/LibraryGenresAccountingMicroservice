package com.example.reports_registration;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.awt.Color;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ReportService {
 private final RestClient books, genres;
 public ReportService(@Value("${books.service.url}") String bu,@Value("${genres.service.url}") String gu){books=RestClient.builder().baseUrl(bu).build();genres=RestClient.builder().baseUrl(gu).build();}
 @SuppressWarnings("unchecked") private List<Map<String,Object>> books(){return Optional.ofNullable(books.get().uri("/books").retrieve().body(List.class)).orElse(List.of());}
 @SuppressWarnings("unchecked") private Map<Long,String> genres(){List<Map<String,Object>> all=Optional.ofNullable(genres.get().uri("/genres").retrieve().body(List.class)).orElse(List.of());Map<Long,String> result=new HashMap<>();for(Map<String,Object> g:all)result.put(((Number)g.get("id")).longValue(),String.valueOf(g.get("name")));return result;}
 private boolean free(Map<String,Object> b){Object c=b.get("cost");return c==null || new BigDecimal(c.toString()).compareTo(BigDecimal.ZERO)==0;}
 public byte[] excel() throws IOException {
  List<Map<String,Object>> free=books().stream().filter(this::free).toList(); try(Workbook w=new XSSFWorkbook();ByteArrayOutputStream out=new ByteArrayOutputStream()){Sheet s=w.createSheet("Бесплатные книги");Row h=s.createRow(0);h.createCell(0).setCellValue("Название");h.createCell(1).setCellValue("Описание");for(int i=0;i<free.size();i++){Row r=s.createRow(i+1);r.createCell(0).setCellValue(String.valueOf(free.get(i).getOrDefault("title","")));r.createCell(1).setCellValue(String.valueOf(free.get(i).getOrDefault("description","")));}w.write(out);return out.toByteArray();}
 }
 public byte[] docx() throws IOException {
  List<Map<String,Object>> all=books();Map<Long,String> gs=genres();try(XWPFDocument d=new XWPFDocument();ByteArrayOutputStream out=new ByteArrayOutputStream()){XWPFTable t=d.createTable(all.size()+1,4);String[] heads={"ID","Название","Жанр","Стоимость"};for(int i=0;i<4;i++)t.getRow(0).getCell(i).setText(heads[i]);for(int i=0;i<all.size();i++){Map<String,Object>b=all.get(i);t.getRow(i+1).getCell(0).setText(String.valueOf(b.get("id")));t.getRow(i+1).getCell(1).setText(String.valueOf(b.getOrDefault("title","")));t.getRow(i+1).getCell(2).setText(gs.getOrDefault(((Number)b.get("genreId")).longValue(),""));t.getRow(i+1).getCell(3).setText(free(b)?"бесплатная":String.valueOf(b.get("cost")));}d.write(out);return out.toByteArray();}
 }
 public byte[] pdf() throws IOException {
  List<Map<String,Object>> all=books();Map<Long,String> gs=genres();Map<String,Integer> counts=new HashMap<>();for(Map<String,Object>b:all)if(free(b)){String g=gs.getOrDefault(((Number)b.get("genreId")).longValue(),"Без жанра");counts.merge(g,1,Integer::sum);}
  DefaultPieDataset<String> ds=new DefaultPieDataset<>();counts.forEach(ds::setValue);JFreeChart chart=ChartFactory.createPieChart("Бесплатные книги по жанрам",ds,true,true,false);((PiePlot)chart.getPlot()).setSectionPaint("Без жанра",Color.LIGHT_GRAY);try(ByteArrayOutputStream image=new ByteArrayOutputStream();ByteArrayOutputStream out=new ByteArrayOutputStream()){ChartUtils.writeChartAsPNG(image,chart,600,400);Document doc=new Document();PdfWriter.getInstance(doc,out);doc.open();doc.add(new Paragraph("Бесплатные книги по жанрам"));doc.add(Image.getInstance(image.toByteArray()));doc.close();return out.toByteArray();}
 }
}
