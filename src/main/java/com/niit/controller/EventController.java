package com.niit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.niit.collaboration.dao.EventDAO;
import com.niit.collaboration.dao.UserDAO;
import com.niit.collaboration.model.Blog;
import com.niit.collaboration.model.Event;
import com.niit.collaboration.model.JobApplication;
import com.niit.collaboration.model.User;

@RestController
public class EventController {
	@Autowired
	private Event event;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private User user;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private HttpSession session;
	
	@RequestMapping("events")
	public ResponseEntity<List<Event>> getAllEvent() {

		List<Event> eventList = eventDAO.listEvent();

		return new ResponseEntity<List<Event>>(eventList, HttpStatus.OK);
	}
	
	@RequestMapping("event/create")
	public ResponseEntity<Event> createEvent(@RequestBody Event event) {
		//System.out.println("Controller started"+event.getName());
		user = (User) session.getAttribute("user");
		event.setUser_id(user.getId());
	
	
	        
		
			if (eventDAO.save(event) == true) {
				event.setErrorCode("200");
				event.setErrorMessage("Event Created");
			} else {
				event.setErrorCode("404");
				event.setErrorMessage("Event Creation Failed");
			}
			return new ResponseEntity<Event>(event, HttpStatus.OK);
		
		

	}
	
	
	

}
