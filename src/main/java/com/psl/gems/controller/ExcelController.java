package com.psl.gems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.psl.gems.service.ExcelService;

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
        String filename = "books.xlsx";
        InputStreamResource file = new InputStreamResource(fileService.load());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
