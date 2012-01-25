package com.imaginea.scrumr.security;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@SuppressWarnings("deprecation")
public class AuthTest extends AbstractDependencyInjectionSpringContextTests {
	
	private IDao<IEntity, Integer> genericDao;
	private UserServiceManager _userServiceManager;

	public void setGenericDao(IDao<IEntity, Integer> genericDao) {
		this.genericDao = genericDao;
	}
	
	public void setUserServiceManager(UserServiceManager _userServiceManager) {
		this._userServiceManager = _userServiceManager;
	}

	public void testInsertUser(){
		User user = new User();
		
		user.setUsername("test");
		user.setPassword("test123");
		user.setEmailId("a@a.com");
		user.setFullName("testUser");
		
//		_userServiceManager.createUser(user);
		
		User myUser = (User) _userServiceManager.loadUserByUsername("test");
		
		assertEquals("test", myUser.getUsername());
		
		
			
	}
	
	
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:applicationContext.xml" };
	}

	
}
