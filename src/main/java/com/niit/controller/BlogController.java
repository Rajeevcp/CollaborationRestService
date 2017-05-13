package com.niit.controller;

import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.dao.BlogDAO;
import com.niit.collaboration.model.Blog;
import com.niit.collaboration.model.User;

@RestController
public class BlogController {

	@Autowired
	private Blog blog;

	@Autowired
	private BlogDAO blogDAO;

	@Autowired
	private User user;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("helloblog")
	public String blogHelloUser() {
		return "Hello Blog Controller";
	}

	@RequestMapping("blogs")
	public ResponseEntity<List<Blog>> getAllBlog() {

		List<Blog> blogList = blogDAO.list();

		return new ResponseEntity<List<Blog>>(blogList, HttpStatus.OK);
	}

	@GetMapping("blog/{id}")
	public ResponseEntity<Blog> getBlogById(@PathVariable("id") String id) {
		blog = blogDAO.getBlogById(id);

		if (blog == null) {
			blog = new Blog();
			blog.setErrorCode("404");
			blog.setErrorMessage("Blog doesn't exist with this id " + id);
		} else {
			blog.setErrorCode("200");
			blog.setErrorMessage("Success");
		}
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);

	}

	@RequestMapping(value = "blog/create", method = RequestMethod.POST)
	public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {

		user = (User) session.getAttribute("user");
		blog.setUser_id(user.getId());
		if (blogDAO.getBlogById(blog.getId()) == null) {
			if (blogDAO.save(blog) == true) {
				blog.setErrorCode("200");
				blog.setErrorMessage("Blog Created");
			} else {
				blog.setErrorCode("404");
				blog.setErrorMessage("Failed to create Blog");
			}
			return new ResponseEntity<Blog>(blog, HttpStatus.OK);
		}
		blog.setErrorCode("404");
		blog.setErrorMessage("Blog exist with this id " + blog.getId());
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	@RequestMapping(value = "blog/update", method = RequestMethod.PUT)
	public ResponseEntity<Blog> updateBlog(@RequestBody Blog blog) {

		//user = (User) session.getAttribute("user");
		//blog.setUser_id(user.getId());
		
		if (blogDAO.getBlogById(blog.getId()) != null) {
			if (blogDAO.update(blog) == true) {
				blog.setErrorCode("200");
				blog.setErrorMessage("Blog Updated Successfully");
			} else {
				blog.setErrorCode("404");
				blog.setErrorMessage("Blog Updated Failed");
			}
			return new ResponseEntity<Blog>(blog, HttpStatus.OK);
		}
		blog.setErrorCode("404");
		blog.setErrorMessage("No blog Exist with id " + blog.getId());
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	@GetMapping("blog/accept/{id}")
	public ResponseEntity<Blog> acceptBlog(@PathVariable("id") String id) {
		if (blogDAO.getBlogById(id) != null) {
			blog = blogDAO.getBlogById(id);
			blog.setStatus('Y');
			if (blogDAO.update(blog) == true) {
				blog.setErrorCode("200");
				blog.setErrorMessage("Blog accepted");
			} else {
				blog.setErrorCode("404");
				blog.setErrorMessage("Request Failed");
			}
			return new ResponseEntity<Blog>(blog, HttpStatus.OK);
		}
		blog.setErrorCode("404");
		blog.setErrorMessage("Blog not exist with this id " + id);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	@GetMapping("blog/reject/{id}")
	public ResponseEntity<Blog> rejectBlog(@PathVariable("id") String id) {
		if (blogDAO.getBlogById(id) != null) {
			blog = blogDAO.getBlogById(id);
			blog.setStatus('N');
			if (blogDAO.update(blog) == true) {
				blog.setErrorCode("200");
				blog.setErrorMessage("Rejected");
			} else {
				blog.setErrorCode("404");
				blog.setErrorMessage("Rejection Failed");
			}
			return new ResponseEntity<Blog>(blog, HttpStatus.OK);
		}
		blog.setErrorCode("404");
		blog.setErrorMessage("Blog doesn't exist with id " + id);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);

	}

}
