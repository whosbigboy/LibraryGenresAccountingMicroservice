package com.example.reports_registration;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/reports") public class ReportController {
 private final ReportService service; public ReportController(ReportService s){service=s;}
 @GetMapping(value="/free-books.xlsx",produces="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") public ResponseEntity<byte[]> excel() throws Exception{return download(service.excel(),"free-books.xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");}
 @GetMapping(value="/free-books.pdf",produces=MediaType.APPLICATION_PDF_VALUE) public ResponseEntity<byte[]> pdf() throws Exception{return download(service.pdf(),"free-books.pdf",MediaType.APPLICATION_PDF_VALUE);}
 @GetMapping(value="/books.docx",produces="application/vnd.openxmlformats-officedocument.wordprocessingml.document") public ResponseEntity<byte[]> docx() throws Exception{return download(service.docx(),"books.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");}
 private ResponseEntity<byte[]> download(byte[] b,String name,String type){return ResponseEntity.ok().contentType(MediaType.parseMediaType(type)).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+name+"\"").body(b);}
}
