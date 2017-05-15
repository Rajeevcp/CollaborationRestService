package com.niit.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.dao.FriendDAO;
import com.niit.collaboration.dao.UserDAO;
import com.niit.collaboration.model.Friend;
import com.niit.collaboration.model.User;

@RestController
public class FriendController {

	@Autowired
	private HttpSession session;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private User user;

	private static final Logger logger = LoggerFactory.getLogger(FriendController.class);
	@Autowired
	private Friend friend;

	@Autowired
	private FriendDAO friendDAO;

	// Need to change it as RequestMapping and ID should take from session
	@GetMapping("/friends/{id}")
	public ResponseEntity<List<Friend>> getMyFriend(@PathVariable("id") String id) {

		List<Friend> friend = friendDAO.listMyFriend(id);
		return new ResponseEntity<List<Friend>>(friend, HttpStatus.OK);

	}

	@RequestMapping(value = "/addFriend/{friendID}", method = RequestMethod.GET)
	public ResponseEntity<Friend> sendFriendRequest(@PathVariable("friendID") String friendID) {
		logger.debug("->->->->calling method sendFriendRequest");
		// String loggedInUserID = (String)
		// httpSession.getAttribute("loggedInUserID");
		user = (User) session.getAttribute("user");
		String logged_user_id = user.getId();
		friend.setUser_id(user.getId());
		friend.setFriend_id(friendID);
		friend.setStatus('R'); // N - New, R->Rejected, A->Accepted

		// Is the user already sent the request previous?

		// check whether the friend exist in user table or not
		if (isUserExist(friendID) == false) {
			friend.setErrorCode("404");
			friend.setErrorMessage("No user exist with the id :" + friendID);
		}

		else if (friendDAO.get(friendID, logged_user_id) != null) {
			logger.debug("Enter into Friend Request already send");
			friend.setErrorCode("404");
			friend.setErrorMessage("You already sent the friend request to " + friendID);

		} else {
			friendDAO.save(friend);

			friend.setErrorCode("200");
			friend.setErrorMessage("Friend request successfull.." + friendID);
		}

		return new ResponseEntity<Friend>(friend, HttpStatus.OK);

	}

	private boolean isUserExist(String id) {
		if (userDAO.get(id) == null)
			return false;
		else
			return true;
	}

	@RequestMapping(value = "/getMyFriendRequests/", method = RequestMethod.GET)
	public ResponseEntity<List<Friend>> getMyFriendRequests() {
		logger.debug("->->->->calling method getMyFriendRequests");
		user = (User) session.getAttribute("user");
		String loggedInUserID = user.getId();
		List<Friend> myFriendRequests = friendDAO.getNewFriendRequests(loggedInUserID);

		return new ResponseEntity<List<Friend>>(myFriendRequests, HttpStatus.OK);

	}

	@RequestMapping(value = "/myFriends", method = RequestMethod.GET)
	public ResponseEntity<List<Friend>> getMyFriends() {
		logger.debug("->->->->calling method getMyFriends");
		user = (User) session.getAttribute("user");
		String loggedInUserID = user.getId();
		List<Friend> myFriends = new ArrayList<Friend>();
		if (loggedInUserID == null) {
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to know your friends");
			myFriends.add(friend);
			return new ResponseEntity<List<Friend>>(myFriends, HttpStatus.OK);

		}

		logger.debug("getting friends of : " + loggedInUserID);
		myFriends = friendDAO.listMyFriend(loggedInUserID);
		logger.debug("getting friends size : " + myFriends.size());
		
		if (myFriends.isEmpty()) {
			logger.debug("Friends does not exsit for the user : " + loggedInUserID);
			friend.setErrorCode("404");
			friend.setErrorMessage("You does not have any friends");
			myFriends.add(friend);
		}
		logger.debug("Send the friend list ");
		return new ResponseEntity<List<Friend>>(myFriends, HttpStatus.OK);
	}

}
