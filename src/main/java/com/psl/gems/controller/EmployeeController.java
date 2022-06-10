package com.psl.gems.controller;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.psl.gems.model.Book;
import com.psl.gems.model.BookObj;
import com.psl.gems.model.Issue;
import com.psl.gems.model.IssueStatus;
import com.psl.gems.model.User;
import com.psl.gems.security.CurrentUserFinder;
import com.psl.gems.service.BookObjService;
import com.psl.gems.service.BookService;
import com.psl.gems.service.IssueService;
import com.psl.gems.service.UserService;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    UserService usService;

    @Autowired
    BookService bookService;

    @Autowired
    BookObjService bookObjService;

    @Autowired
    IssueService issueService;

    @Autowired
    CurrentUserFinder currentUserFinder;

    @GetMapping
    public String employeeHomePage(Model model) {
        int currentUserId = currentUserFinder.getCurrentUserId();
        User currentUser = usService.findById(currentUserId);
        model.addAttribute("currentUser", currentUser);

        return "employee/employee-home.html";
    }

    /*-----------------------Manage User--------------*/
    @GetMapping(value = "/users/showusers")
    public String showUsers(Model model,
                            @RequestParam(required = false) String name,
                            @RequestParam(defaultValue = "0") int user_id) {
        List<User> users;
        if (user_id != 0) {
            users = new ArrayList<User>();
            users.add(usService.findById(user_id));
        } else if (name != null && !name.isEmpty()) {
            users = usService.getByName(name);
        } else {
            users = usService.findAll();
        }
        model.addAttribute("users", users);
        return "employee/employee-show-users.html";
    }

    @GetMapping(value = "/users/showuserinfo")
    public String showUserInfo(@RequestParam int user_id,
                               Model model) {
        User user = usService.findById(user_id);
        model.addAttribute("user", user);
        return "employee/employee-show-user-info.html";
    }

    /*-----------------------Manage Book -------------------*/
    @GetMapping(value = "/books")
    public String books() {
        return "redirect:/employee/books/showbooks";
    }

    @GetMapping(value = "/books/{isbn}")
    public String viewBook(Model model, @PathVariable long isbn) {
        Book book = bookService.findById(isbn);
        model.addAttribute("book", book);
        return "employee/view-book.html";
    }

    @GetMapping(value = "/books/showbooks")
    public String showBooks(Model model,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String author) {
        List<Book> books;
        if ((title == null || title.isEmpty()) && (author == null || author.isEmpty())) {
            books = bookService.findAll();
        } else {
            books = bookService.searchBooks(title, author);
        }
        model.addAttribute("books", books);
        return "employee/employee-show-books.html";
    }

    @GetMapping(value = "/browsebooks")
    public String browseBooks(Model model, @RequestParam(required = false) String title,
                              @RequestParam(required = false) String author) {
        List<Book> books;
        if ((title == null || title.isEmpty()) && (author == null || author.isEmpty())) {
            books = bookService.findAll();
        } else {
            books = bookService.searchBooks(title, author);
        }
        model.addAttribute("books", books);
        return "employee/employee-directloan.html";
    }

    @GetMapping(value = "/books/view-direct-loan/{isbn}")
    public String directLoanviewBook(Model model, @PathVariable long isbn) {
        boolean available = bookService.checkAvailabilityById(isbn);
        Book book = bookService.findById(isbn);
        model.addAttribute("book", book);
        model.addAttribute("isAvailable", available);
        return "employee/employee-directloan-view-book.html";
    }


    @PostMapping(value = "books/directloan/{bookId}")
    public String reserveBook(@PathVariable Long bookId,
                              @RequestParam Map<String, String> requestBody,
                              @RequestParam int userId) {
        Book book = bookService.findById(bookId);
        BookObj bookObj = bookObjService.getAvailableCopyByBookId(book.getISBN());
        if (bookObj == null) {
            // TODO: Redirect and say that book is unavailable.
            return "redirect:/employee/browsebooks";
        }
        User userLend = usService.findById(userId);
        LocalDate issueDate = LocalDate.parse(requestBody.get("issueDate"));
        LocalDate returnDate = LocalDate.parse(requestBody.get("returnDate"));
        requestBody.get("returnDate");
        Issue issue = new Issue();
        issue.setBookObj(bookObj);
        issue.setUser(userLend);
        issue.setIssueDate(issueDate);
        issue.setReturnDate(returnDate);
        issue.setStatus(IssueStatus.ISSUED);
        issueService.save(issue);
        // TODO: Redirect and say reservation made successfully.
        return "redirect:/employee/browsebooks";
    }

    @PostMapping(value = "/books/update/{isbn}")
    public String updateBook(@PathVariable long isbn, @RequestParam String title, @RequestParam String author,
                             @RequestParam String language, @RequestParam String description,
                             @RequestParam String NXB, @RequestParam Long amount,
                             @RequestParam String style, @RequestParam Long price) {
        Book book = bookService.findById(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setLanguage(language);
        book.setDescription(description);
        book.setNXB(NXB);
        book.setAmount(amount);
        book.setStyle(style);
        book.setPrice(price);
        bookService.save(book);
        return "redirect:/employee/books/" + isbn;
    }

    @PostMapping(value = "/books/{isbn}/add-copy")
    public String addBookCopy(@PathVariable long isbn) {
        Book book = bookService.findById(isbn);
        BookObj bookObj = new BookObj();
        bookObj.setBook(book);
        bookObjService.save(bookObj);
        return "redirect:/employee/books/" + isbn;
    }

    @GetMapping(value = "/books/newbook")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "employee/employee-new-book.html";
    }

    @PostMapping(value = "/books/save")
    public String saveBook(Book book) {
        bookService.save(book);
        BookObj bookObj = new BookObj();
        bookObj.setBook(book);
        bookObjService.save(bookObj);
        return "redirect:/employee/books/booksaved";
    }

    @GetMapping(value = "/books/booksaved")
    public String bookSaved() {
        return "employee/employee-book-saved.html";
    }

    @GetMapping(value = "/books/areyousuretodeletebook")
    public String areYouSureToDeleteBook(@RequestParam Long deleteBookId, Model model) {
        Book book = bookService.findById(deleteBookId);
        model.addAttribute("deleteBook", book);
        return "employee/employee-delete-book.html";
    }

    @PostMapping(value = "/books/deletebook")

    public String deleteBook(@RequestParam Long deleteBookId) {
        bookService.deleteById(deleteBookId);
        return "redirect:/employee/books/bookdeleted";
    }

    @GetMapping(value = "/books/bookdeleted")
    public String bookDeleted() {
        return "employee/employee-book-deleted.html";
    }

    @GetMapping(value = "/books/changebookinfo")
    public String changeBookInfo(@RequestParam Long changeBookId, Model model) {
        Book book = bookService.findById(changeBookId);
        model.addAttribute("book", book);
        return "employee/employee-change-book-info.html";
    }

    @PutMapping(value = "/books/savebookchange")
    public String updatebookinfo(Book book) {
        bookService.save(book);
        return "employee/employee-book-information-changed.html";
    }


    /*-----------------Manage Issues-------------------*/
    @GetMapping(value = "/management/reservations")
    public String checkIssues(Model model, @RequestParam(defaultValue = "0") int issueId, @RequestParam(required = false) IssueStatus status) {
        List<Issue> issues;
        if (issueId != 0) {
            issues = new ArrayList<Issue>();
            issues.add(issueService.findById(issueId));
        } else if (status != null) {
            issues = issueService.findByStatus(status);
        } else {
            issues = issueService.findAll();
        }
        model.addAttribute("issues", issues);
        return "employee/employee-reservations.html";
    }

    @GetMapping(value = "issues/{issueId}")
    public String viewIssue(Model model, @PathVariable int issueId) {
        Issue issue = issueService.findById(issueId);
        model.addAttribute("issue", issue);
        return "employee/view-issue.html";
    }

    @PostMapping(value = "/issues/{issueId}/update-status")
    public String updateIssue(@PathVariable int issueId, @RequestParam(required = false) IssueStatus status) {
        Issue issue = issueService.findById(issueId);
        issue.setStatus(status);
        issueService.save(issue);
        return "redirect:/employee/management/reservations";
    }

    @GetMapping(value = "/management/directloan")
    public String showBooks2(Model model,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) String author) {
        List<Book> books;
        if ((title == null || title.isEmpty()) && (author == null || author.isEmpty())) {
            books = bookService.findAll();
        } else {
            books = bookService.searchBooks(title, author);
        }
        model.addAttribute("books", books);
        return "employee/employee-directloan.html";
    }
    // changeBookInfo(), updateBookInfo(), returnBooks(), reservations()

}

