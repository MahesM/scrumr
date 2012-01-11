package com.imaginea.scrumr.utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.imaginea.scrumr.entities.ProjectEntity;
import com.imaginea.scrumr.entities.SprintEntity;
import com.imaginea.scrumr.entities.UserEntity;
import com.imaginea.scrumr.entities.UserStoryEntity;
import com.imaginea.scrumr.utilities.HibernateSessionFactory;


public class DatabaseHandler {

	public boolean createProject(ProjectEntity project){
		System.out.println("CREATING PROJECT");
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
			session.save(project);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public List<ProjectEntity> getAllProjects(String userId){
		System.out.println("All Projects: "+userId);
		List<ProjectEntity> projects = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
            Query q = session.createQuery("from ProjectEntity as p order by p.id desc");
            if( q.list().size() > 0 ){
            	projects = (List<ProjectEntity>) q.list();
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return projects;
	}
	
	public List<ProjectEntity> getAllProjects(){
		List<ProjectEntity> projects = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
            Query q = session.createQuery("from ProjectEntity as p order by p.id desc");
            if( q.list().size() > 0 ){
            	projects = (List<ProjectEntity>) q.list();
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return projects;
	}
	
	public ProjectEntity getProject(Long projectId){
		ProjectEntity project = null;
		System.out.println("getProjectfor: "+projectId);
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
            Query q = session.createQuery("from ProjectEntity as p where p.id = "+projectId);
            if( q.list().size() > 0 ){
            	project = (ProjectEntity) q.list().get(0);
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return project;
	}

	public UserEntity getUserInfo(String userId){
		UserEntity user = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		try{
            Query q = session.createQuery("from UserEntity u where u.id = \'"+userId+"\'");
            if( q.list().size() > 0 ){
            	user = (UserEntity) q.list().get(0);
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return user;
	}
	
	public List<UserEntity> getAllUsers(){
		List<UserEntity> users = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		try{
            Query q = session.createQuery("from UserEntity");
            if( q.list().size() > 0 ){
            	users = (List<UserEntity>) q.list();
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return users;
	}
	
	public List<UserEntity> getAllUsers(Long projectId){
		List<UserEntity> users = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		try{
            Query q = session.createQuery("from UserEntity as u join fetch u.project as p where p.id = "+projectId);
            if( q.list().size() > 0 ){
            	users = (List<UserEntity>) q.list();
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return users;
	}
	
	//User Stories
	public UserStoryEntity getUserStoryEntity(Long storyId){
		UserStoryEntity story = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		try{
            Query q = session.createQuery("from UserStoryEntity as us where us.id = "+storyId);
            if(q.list().size() > 0){
            	story = (UserStoryEntity) q.list().get(0);
            }
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return story;
	}
	
	public List<UserStoryEntity> getAllUserStoryEntities(Long projectId){
		List<UserStoryEntity> stories = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
System.out.println("PRJ: "+projectId);
		try{
            Query q = session.createQuery("from UserStoryEntity as ustory join fetch ustory.project as p where p.id = "+projectId+" order by ustory.id desc");
            if(q.list().size() > 0){
            	stories = (List<UserStoryEntity>) q.list();
            }
            System.out.println("STR: "+stories);
            transaction.commit();	
         }
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
        return stories;
	}
	
	public List<UserStoryEntity> getAllUserStoryEntities(Long projectId, int sprintId, String nameString){
		List<UserStoryEntity> stories = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		String query = null;
		if(nameString != null && nameString.length() > 0){
			query = "from UserStoryEntity as ustory join fetch ustory.project as p where p.id = "+projectId+" and ustory.sprint_id = "+sprintId+" and  lower(ustory.title) LIKE lower('%"+nameString+"%') order by ustory.id desc";
		}else{
			query = "from UserStoryEntity as ustory join fetch ustory.project as p where p.id = "+projectId+" and ustory.sprint_id = "+sprintId+" order by ustory.id desc";
		}
		System.out.println("QUERY1: "+query);
		try{
            Query q = session.createQuery(query);
            if(q.list().size() > 0){
            	stories = (List<UserStoryEntity>) q.list();
            }
         }
		catch (HibernateException e) {
			e.printStackTrace();
		}
        return stories;
	}
	
	public List<UserStoryEntity> getAllUserStoryEntities(Long projectId, int sprintId){
		List<UserStoryEntity> stories = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		String query = null;
		
		query = "from UserStoryEntity as ustory join fetch ustory.project as p where p.id = "+projectId+" and ustory.sprint_id <= "+sprintId+" order by ustory.id desc";
		
		System.out.println("QUERY2: "+query);
		try{
            Query q = session.createQuery(query);
            if(q.list().size() > 0){
            	stories = (List<UserStoryEntity>) q.list();
            }
         }
		catch (HibernateException e) {
			e.printStackTrace();
		}
        return stories;
	}
	
	public boolean createUserStory(UserStoryEntity story, Long projectId){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
			Query q1 = session.createQuery("from ProjectEntity as p where p.id = "+projectId);
            ProjectEntity project = (ProjectEntity) q1.list().get(0);
            story.setProject(project);
			session.save(story);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addUserToProject(String userid, Long projectId){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		ProjectEntity project = null;
		UserEntity user = null;
		try{
			Query q1 = session.createQuery("from ProjectEntity as p where p.id = "+projectId);
			if( q1.list().size() > 0 ){
				project = (ProjectEntity) q1.list().get(0);
			}
            
            Query q2 = session.createQuery("from UserEntity u where u.id = \'"+userid+"\'");
            if( q2.list().size() > 0 ){
            	user = (UserEntity) q2.list().get(0);
            }
            
            project.addAssignees(user);
			session.save(project);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateProject(ProjectEntity project){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
			session.merge(project);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateUserStory(UserStoryEntity story){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
			session.merge(story);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean removeUserFromProject(String userid, Long projectId){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		ProjectEntity project = null;
		UserEntity user = null;
		try{
			Query q1 = session.createQuery("from ProjectEntity as p where p.id = "+projectId);
			if( q1.list().size() > 0 ){
				project = (ProjectEntity) q1.list().get(0);
			}
            
            Query q2 = session.createQuery("from UserEntity u where u.id = \'"+userid+"\'");
            if( q2.list().size() > 0 ){
            	user = (UserEntity) q2.list().get(0);
            }
            
            project.removeAssignees(user);
			session.save(project);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addUserToStory(String userid, Long storyId){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		UserStoryEntity story = null;
		UserEntity user = null;
		try{
			Query q1 = session.createQuery("from UserStoryEntity as p where p.id = "+storyId);
			if( q1.list().size() > 0 ){
				story = (UserStoryEntity) q1.list().get(0);
			}
            
            Query q2 = session.createQuery("from UserEntity u where u.id = \'"+userid+"\'");
            if( q1.list().size() > 0 ){
            	user = (UserEntity) q2.list().get(0);
            }
            
            story.addAssignees(user);
			session.save(story);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean removeUserFromStory(String userid, Long storyId){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		UserStoryEntity story = null;
		UserEntity user = null;
		try{
			Query q1 = session.createQuery("from UserStoryEntity as p where p.id = "+storyId);
			if( q1.list().size() > 0 ){
				story = (UserStoryEntity) q1.list().get(0);
			}
            
            Query q2 = session.createQuery("from UserEntity u where u.id = \'"+userid+"\'");
            if( q1.list().size() > 0 ){
            	user = (UserEntity) q2.list().get(0);
            }
            
            story.removeAssignees(user);
			session.save(story);
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateCurrentSprint(String[] storyIds,String status, int sprint ){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		UserStoryEntity story = null;
		SprintEntity sprintid = null;
		try{
			for(String s: storyIds){
				Query q = session.createQuery("from UserStoryEntity as us where us.id = "+Long.parseLong(s));
				Query q2 = session.createQuery("from SprintEntity as s where s.id = "+sprint);
				if(q.list().size() > 0){
	            	story = (UserStoryEntity) q.list().get(0);
	            	sprintid = (SprintEntity) q2.list().get(0);
	            	story.setSprint_id(sprintid);
	            	story.setStatus(status);
	            	session.save(story);
	            }
			}
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateCurrentSprint(String storyId,String status, int sprint ){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		UserStoryEntity story = null;
		SprintEntity sprintid = null;
		try{
			Query q = session.createQuery("from UserStoryEntity as us where us.id = "+Long.parseLong(storyId));
			Query q2 = session.createQuery("from SprintEntity as s where s.id = "+sprint);
			if(q.list().size() > 0){
            	story = (UserStoryEntity) q.list().get(0);
            	sprintid = (SprintEntity) q2.list().get(0);
            	story.setSprint_id(sprintid);
            	story.setStatus(status);
            	session.save(story);
            }
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateStatus(Long id, String status){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		UserStoryEntity story = null;
		try{
			Query q = session.createQuery("from UserStoryEntity as us where us.id = "+id);
			if(q.list().size() > 0){
            	story = (UserStoryEntity) q.list().get(0);
            	story.setStatus(status);
            	session.save(story);
            }
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteStory(Long id){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		UserStoryEntity story = null;
		try{
			Query q = session.createQuery("delete from UserStoryEntity as us where us.id = "+id);
			q.executeUpdate();
			transaction.commit();
			return true;
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	public List<SprintEntity> getAllSprints(Long id){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		List<SprintEntity> sprints = null;
		try{
			Query q = session.createQuery("from SprintEntity as s join fetch s.project as sp where sp.id = "+id);
			if(q.list().size() > 0){
				sprints = (List<SprintEntity>) q.list();
			}
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return sprints;
	}
	
	public SprintEntity getSprint(Long id){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		SprintEntity sprint = null;
		try{
			Query q = session.createQuery("from SprintEntity as s where s.id = "+id);
			if(q.list().size() > 0){
				sprint = (SprintEntity) q.list().get(0);
			}
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return sprint;
	}
	
	public SprintEntity getSprint(int id){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		SprintEntity sprint = null;
		try{
			Query q = session.createQuery("from SprintEntity as s where s.id = "+id);
			if(q.list().size() > 0){
				sprint = (SprintEntity) q.list().get(0);
			}
		}
		catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return sprint;
	}
	
	public boolean createSprint(SprintEntity sprint, Long projectId){
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		ProjectEntity project = null;
		if(sprint != null){
			try{
				Query q1 = session.createQuery("from ProjectEntity as p where p.id = "+projectId);
				if( q1.list().size() > 0 ){
					project = (ProjectEntity) q1.list().get(0);
				}
				sprint.setProject(project);
				session.save(sprint);
				transaction.commit();
				return true;	
			}
			catch (HibernateException e) {
				transaction.rollback();
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
}