package usermicroservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import usermicroservice.dao.UserDAO;
import usermicroservice.domains.User;

@RestController
@CrossOrigin
public class HomeController {

	@Autowired
	UserDAO userDAO;

	@RequestMapping("/users")
	public List<User> getUsers() {
		return userDAO.findAll();
	}

	@RequestMapping("/users/{id}")
	public ResponseEntity<User> getUserByID(@PathVariable long id) {
		User user = userDAO.findByUserID(id);
		ResponseEntity<User> response;
		if (user == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(user, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping("/users/find/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		User user = userDAO.findByEmail(email);
		ResponseEntity<User> response;
		if (user == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(user, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users/find")
	public ResponseEntity<User> getUserByEmailAndPassword(@RequestBody String jsonString) {
		JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);
		String email = jobj.get("email").getAsString();
		String password = jobj.get("password").getAsString();
		User user = userDAO.findByEmailAndPassword(email, password);
		ResponseEntity<User> response;
		if (user == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(user, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		ResponseEntity<User> response;
		User savedUser = userDAO.save(user);
		response = new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/users/{id}")
	public void deleteUser(@PathVariable Long id) {
		userDAO.deleteById(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Validated User user) {
		User userDB = userDAO.findById(id).orElse(null);
		ResponseEntity<User> response;

		if (userDB != null) {
			if (user.getName() != null) {
				userDB.setName(user.getName());
			}
			if (user.getSurname() != null) {
				userDB.setSurname(user.getSurname());
			}
			if (user.getAdmin() == 0) {
				userDB.setAdmin((byte) 0);
			} else {
				userDB.setAdmin((byte) 1);
			}
			if (user.getBookings() != null) {
				userDB.setBookings(user.getBookings());
			}
			if (user.getEmail() != null) {
				userDB.setEmail(user.getEmail());
			}
			if (user.getHomes() != null) {
				userDB.setHomes(user.getHomes());
			}
			if (user.getMessages1() != null) {
				userDB.setMessages1(user.getMessages1());
			}
			if (user.getMessages2() != null) {
				userDB.setMessages2(user.getMessages2());
			}
			if (user.getPassword() != null) {
				userDB.setPassword(user.getPassword());
			}
			response = new ResponseEntity<>(userDB, HttpStatus.OK);
			return response;
		}
		response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return response;
	}
}
