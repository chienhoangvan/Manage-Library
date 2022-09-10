package com.psl.gems.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.psl.gems.model.User;
import com.psl.gems.service.UserService;

@Service
public class CurrentUserFinder {

	@Autowired
	UserService usService;
	
	public int getCurrentUserId() {
		UserDetails details = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username=details.getUsername();
		
		User user= usService.getByUsername(username);
		return user.getUser_id();

	}
	
	public User getCurrentUser() {
		User currentUser= usService.findById(getCurrentUserId());
		return currentUser;
	}
	
	
}
