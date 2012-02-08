package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Comment;

public interface CommentManager {
	
	void createComment(Comment comment);
	
	Comment readComment(Integer pkey);
	
	void updateComment(Comment comment);
	
	void deleteComment(Comment comment);

	List<Comment> fetchCommentsByStory(Integer pkey);
}
