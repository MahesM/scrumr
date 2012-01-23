package com.imaginea.scrumr.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.imaginea.scrumr.entities.User;

public interface UserServiceManager extends UserDetailsService {
	
	void createUser(User user);
	
	User readUser(Integer pkey);
	
	User readUser(String username);

	void updateUser(User user);
	
	void deleteUser(User user);

	void deleteUser(String username);

	boolean userExists(String username);
	
	List<User> fetchAllUsers();
}
