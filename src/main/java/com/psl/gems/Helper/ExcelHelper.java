package com.psl.gems.Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import com.psl.gems.model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.psl.gems.model.Book;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"ISBN", "title", "author", "language", "description", "NXB", "style", "amount", "price"};
    static String SHEET = "Books";

    static String[] HEADERs_2 = {"User_id", "FUll Name", "Date of Birth", "Phone Number", "Role", "Username", "password", "address"};
    static String SHEET_2 = "Users";

    public static ByteArrayInputStream booksToExcelExport(List<Book> books) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            int rowIdx = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(book.getISBN());
                row.createCell(1).setCellValue(book.getTitle());
                row.createCell(2).setCellValue(book.getAuthor());
                row.createCell(3).setCellValue(book.getLanguage());
                row.createCell(4).setCellValue(book.getDescription());
                row.createCell(5).setCellValue(book.getNXB());
                row.createCell(6).setCellValue(book.getStyle());
                row.createCell(7).setCellValue(book.getAmount());
                row.createCell(8).setCellValue(book.getPrice());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream booksToExcel(List<Book> books) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream usersToExcelExport(List<User> users) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET_2);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs_2.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs_2[col]);
            }

            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getUser_id());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getDateOfBirth());
                row.createCell(3).setCellValue(user.getNumber());
                row.createCell(4).setCellValue(user.getRole());
                row.createCell(5).setCellValue(user.getUsername());
                row.createCell(6).setCellValue(user.getPassword());
                row.createCell(7).setCellValue(user.getAddress());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Book> excelToBooks(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Book> books = new ArrayList<Book>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Book book = new Book();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            book.setISBN((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            book.setTitle(currentCell.getStringCellValue());
                            break;
                        case 2:
                            book.setAuthor(currentCell.getStringCellValue());
                            break;
                        case 3:
                            book.setLanguage(currentCell.getStringCellValue());
                            break;
                        case 4:
                            book.setDescription(currentCell.getStringCellValue());
                            break;
                        case 5:
                            book.setNXB(currentCell.getStringCellValue());
                            break;
                        case 6:
                            book.setStyle(currentCell.getStringCellValue());
                            break;
                        case 7:
                            book.setAmount((long) currentCell.getNumericCellValue());
                            break;
                        case 8:
                            book.setPrice((long) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                books.add(book);
            }
            workbook.close();
            return books;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}