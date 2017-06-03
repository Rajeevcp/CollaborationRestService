package com.niit.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.niit.collaboration.dao.UserDAO;
import com.niit.collaboration.model.User;

@RestController
public class UserController {

	@Autowired
	private User user;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private HttpSession session;

	@GetMapping("/hello")
	public String printWelcome() {
		System.out.println("Started");
		// ModelAndView mv = new ModelAndView("/foo");
		user = (User) session.getAttribute("user");
		return "Hellosssssssss" + user.getId();

	}

	@GetMapping("/user/logout")
	public void logoutUser() {
		session.invalidate();
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUser() {
		List<User> userList = userDAO.list();
		user = (User) session.getAttribute("user");
		//System.out.println("given id is " + user.getId());
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserByID(@PathVariable("id") String id) {

		user = userDAO.get(id);

		if (user == null) {
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with the id :" + id);
		} else {
			user.setErrorCode("200");
			user.setErrorMessage("success");
		}

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PutMapping(value = "/user/update/")
	public ResponseEntity<User> updateUser(@RequestBody User user) {

		if (userDAO.get(user.getId()) == null) {

			user = new User(); // ?
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with id " + user.getId());
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		userDAO.update(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/validate/", method = RequestMethod.POST)
	public ResponseEntity<User> validateUser(@RequestBody User user) {
		user = userDAO.isValidCredentials(user.getId(), user.getPassword());
		if (user != null) {
			user.setErrorCode("200");
			user.setErrorMessage("You have successfully logged in.");
			session.setAttribute("user", user);
		} else {
			user = new User(); // Do wee need to create new user?
			user.setErrorCode("404");
			user.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {

		if (userDAO.get(user.getId()) == null) {

			if (userDAO.save(user) == true) {
				user.setErrorCode("200");
				user.setErrorMessage(
						"Thank you  for registration. You have successfully registered as " + user.getRole());
			} else {
				user.setErrorCode("404");
				user.setErrorMessage("Could not complete the operatin please contact Admin");

			}

			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		user.setErrorCode("404");
		user.setErrorMessage("User already exist with id : " + user.getId());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping("/imageUpload")
	public void ImageUpload(@RequestBody MultipartFile file, HttpSession session) throws IOException {

		user = (User) session.getAttribute("user");
		String username = user.getId(); /* Get Logged in Username */
		/* Get user object based on username */
		System.out.println(file.getContentType() + '\n' + file.getName() + '\n' + file.getSize() + '\n'
				+ file.getOriginalFilename());
		user.setImage(file.getBytes());
		userDAO.update(user);
	}

	@GetMapping("/myProfile")
	public ResponseEntity<User> profileimage(HttpSession session) {
		user = (User) session.getAttribute("user");
		String uid = user.getId();
		User users = userDAO.get(uid);
		return new ResponseEntity<User>(users, HttpStatus.OK);
	}
}
