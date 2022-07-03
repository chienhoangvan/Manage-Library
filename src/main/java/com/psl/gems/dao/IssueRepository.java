package com.psl.gems.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.psl.gems.model.Issue;
import com.psl.gems.model.IssueStatus;
import com.psl.gems.model.User;
import org.springframework.web.bind.annotation.RequestParam;

public interface IssueRepository extends JpaRepository<Issue, Integer> {
	
	List<Issue> findByStatus(IssueStatus status);
	List<Issue> findByUser(User user);
	
	List<Issue> findByUserAndStatus(User user, IssueStatus status);

	@Query(value = "SELECT COUNT(i.id) FROM issue i, book b, book_obj bo WHERE b.title = ?1 AND b.isbn = bo.book_isbn AND bo.id = i.book_obj_id ", nativeQuery = true)
	Long countByTitle(String title);

}
