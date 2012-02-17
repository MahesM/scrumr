package com.imaginea.scrumr.security;

import java.util.Hashtable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.UserServiceManager;

public class CustomUserDetailsManagerImpl implements UserServiceManager {
	
	private IDao<IEntity, Integer> genericDao;
	
	@Transactional
	public void createUser(User user) {
		if(user != null) {
			genericDao.save(user);
			String encPassword = user.getPassword(); 
			user.setPassword(encPassword);  
			genericDao.update(user);
		}
	}
	
	public User readUser(Integer pkey) {
		return genericDao.find(User.class, pkey);
	}
	
	public User readUser(String username) {
		
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("username", username);
		return genericDao.getEntity(User.class, "users.selectUserByUserName", ht);
		
	}

	@Transactional
	public void updateUser(User user) {
		if(user != null) {
			genericDao.save(user);
		}
	}
	
	@Transactional
	public void deleteUser(User user) {
		if(user != null) {
			genericDao.delete(user);
		}
	}

	@Transactional
	public void deleteUser(String username) {
		
		User user = readUser(username);
		deleteUser(user);
		
	}

	public boolean userExists(String username) {
		User user = readUser(username);
		
		if (user == null) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public List<User> fetchAllUsers(){
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		return genericDao.getEntities(User.class, "users.fetchAllUsers",ht);
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		User user = this.readUser(username);
		return user;
	}

	/* Getters and Setters */
	
	public IDao<IEntity, Integer> getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(IDao<IEntity, Integer> genericDao) {
		this.genericDao = genericDao;
	}

	
}
