package com.psl.gems.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.psl.gems.Helper.ExcelHelper;
import com.psl.gems.model.Book;
import com.psl.gems.dao.BookRepository;
@Service
public class ExcelService {
    @Autowired
    BookRepository bookRepository;
    public ByteArrayInputStream load() {
        List<Book> books = bookRepository.findAll();
        ByteArrayInputStream in = ExcelHelper.booksToExcel(books);
        return in;
    }
}
