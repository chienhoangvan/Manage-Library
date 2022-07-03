package com.psl.gems.controller;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.psl.gems.model.Book;
import com.psl.gems.model.Issue;
import com.psl.gems.service.BookObjService;
import com.psl.gems.service.BookService;
import com.psl.gems.service.IssueService;
import com.psl.gems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/statistical")
public class ChartController {
    @Autowired
    IssueService issueService;

    @Autowired
    BookService bookService;

    @Autowired
    BookObjService bookObjService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/chart")
    public String getPieChart(Model model) {
        List<Book> books = bookService.findAll();
        Map<String, Long> graphData = new TreeMap<>();
        for(Book book : books) {
            graphData.put(book.getTitle(), issueService.countByTitle(book.getTitle()));
        }
        model.addAttribute("chartData", graphData);
        return "statistical/chart.html";
    }
}