package com.niit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.dao.ChatDAO;
import com.niit.collaboration.dao.UserDAO;
import com.niit.collaboration.model.Blog;
import com.niit.collaboration.model.Chat;
import com.niit.collaboration.model.User;

@RestController
public class PersonalChatController {
	
	@Autowired
	private User user;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private Chat chat;
	
	@Autowired
	private ChatDAO chatDAO;

	@Autowired
	private HttpSession session;
	
	@GetMapping("chat/{friend_id}")
	public ResponseEntity<List<Chat>> getAllchat(@PathVariable("friend_id") String friend_id) {
		user = (User) session.getAttribute("user");
		String user_id = user.getId();
		List<Chat> chatList = chatDAO.getChatByFriend(user_id, friend_id);

		return new ResponseEntity<List<Chat>>(chatList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "chating/save", method = RequestMethod.POST)
	public ResponseEntity<Chat> createBlog(@RequestBody Chat chat) {

		user = (User) session.getAttribute("user");
		chat.setUser_id(user.getId());
		
		//blog.setUser_id(user.getId());
		
			if (chatDAO.save(chat) == true) {
				System.out.println("chat saved successs-----");
				chat.setErrorCode("200");
				chat.setErrorMessage("Blog Created");
			} else {
				System.out.println("chat saved failed-----");
				chat.setErrorCode("404");
				chat.setErrorMessage("Failed to create Blog");
			}
			return new ResponseEntity<Chat>(chat, HttpStatus.OK);
		
	}

}
