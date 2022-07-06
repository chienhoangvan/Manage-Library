package com.psl.gems.controller;

import com.psl.gems.model.IssueStatus;
import com.psl.gems.model.User;
import com.psl.gems.model.Issue;
import com.psl.gems.service.IssueService;
import com.psl.gems.service.PdfGenerateService;
import com.psl.gems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Controller
@RequestMapping(value = "/pdf")
public class PdfGeneratorRunner {

    @Autowired
    private PdfGenerateService pdfGenerateService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    @PostMapping(value = "/getIssuesPdf")
    public String printIssuesPDF(Model model, @RequestParam(defaultValue = "0") int issueId,
                               @RequestParam(defaultValue = "0") int userId,
                               @RequestParam(required = false) IssueStatus status) {
        Map<String, Object> data = new HashMap<>();
        User customer = new User();
        if (userId == 0) {
            customer = null;
        } else {
            customer = userService.findById(userId);
        }

//        model.addAttribute("customer", customer);
        data.put("customer", customer);

//        List<QuoteItem> quoteItems = new ArrayList<>();
//        QuoteItem quoteItem1 = new QuoteItem();
//        quoteItem1.setDescription("Test Quote Item 1");
//        quoteItem1.setQuantity(1);
//        quoteItem1.setUnitPrice(100.0);
//        quoteItem1.setTotal(100.0);
//        quoteItems.add(quoteItem1);
//
//        data.put("quoteItems", quoteItems);

        List<Issue> issues = new ArrayList<>();
        if (issueId != 0) {
            issues = new ArrayList<Issue>();
            issues.add(issueService.findById(issueId));
        } else if (userId != 0) {
            if (status == null) {
                issues = issueService.findByUser(userService.findById(userId));
            } else {
                issues = issueService.findByUserAndStatus(userService.findById(userId), status);
            }
        } else if (status != null) {
            issues = issueService.findByStatus(status);
        } else {
            issues = issueService.findAll();
        }
        model.addAttribute("issues", issues);
        data.put("issues", issues);

        pdfGenerateService.generatePdfFile("quotation", data, "ticketOfIssues.pdf");

        return "employee/employee-get-issues.html";
    }
}
