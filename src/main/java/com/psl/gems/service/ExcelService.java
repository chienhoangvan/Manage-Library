package com.psl.gems.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.psl.gems.Helper.ExcelHelper;
import com.psl.gems.model.Book;
import com.psl.gems.dao.BookRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {
    @Autowired
    BookRepository bookRepository;

    public ByteArrayInputStream load() {
        List<Book> books = bookRepository.findAll();
        ByteArrayInputStream in = ExcelHelper.booksToExcel(books);
        return in;
    }

    public ByteArrayInputStream loadExport() {
        List<Book> books = bookRepository.findAll();
        ByteArrayInputStream in = ExcelHelper.booksToExcelExport(books);
        return in;
    }

    public void save(MultipartFile file) {
        try {
            List<Book> tutorials = ExcelHelper.excelToBooks(file.getInputStream());
            bookRepository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
