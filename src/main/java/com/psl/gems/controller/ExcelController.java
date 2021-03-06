package com.psl.gems.controller;

import com.psl.gems.Helper.ExcelHelper;
import com.psl.gems.message.ResponseMessage;
import com.psl.gems.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.psl.gems.service.ExcelService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/excel")
public class ExcelController {
    @Autowired
    ExcelService fileService;

    @GetMapping(value = "/download")
    public String downloadTemplate() {
        return "excel/download-template";
    }

    @GetMapping(value = "/download-template")
    public ResponseEntity<Resource> getFile() {
        String filename = "books_template.xlsx";
        InputStreamResource file = new InputStreamResource(fileService.load());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping(value = "/import")
    public String importFileBooks() {
        return "excel/import-file-excel-books";
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                fileService.save(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = fileService.getAllBooks();
            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/export")
    public String ExportBooks() {
        return "excel/export-file-excel-books";
    }

    @GetMapping(value = "/export-books")
    public ResponseEntity<Resource> getFileBook() {
        String filename = "books.xlsx";
        InputStreamResource file = new InputStreamResource(fileService.loadExport());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    /*-------------------Export file User--------------------*/

    @GetMapping(value = "/exportusers")
    public String ExportUsers() {
        return "excel/export-file-excel-users";
    }

    @GetMapping(value = "/export-users")
    public ResponseEntity<Resource> getFileUser() {
        String filename = "users.xlsx";
        InputStreamResource file = new InputStreamResource(fileService.loadUserExport());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
