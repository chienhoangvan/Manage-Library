package com.psl.gems.model;

import lombok.Data;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Data
@Entity
public class Book {
	
	@Id
	private long ISBN;
	private String title;
	private String author;
	private String language;

	private String description;

	private String NXB;

//	private boolean available;

	private Long amount;
	
	@OneToMany(mappedBy = "book", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<BookObj> bookObjs;
	
	
	public long getISBN() {
		return ISBN;
	}
	public void setISBN(long iSBN) {
		ISBN = iSBN;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public Set<BookObj> getBookObjs() {
		return bookObjs;
	}
	public void setBookObjs(Set<BookObj> bookObjs) {
		this.bookObjs = bookObjs;
	}
	@Override
	public String toString() {
		return "Book [ISBN=" + ISBN + ", title=" + title + ", author=" + author + "]";
	}
	
	
	
}
