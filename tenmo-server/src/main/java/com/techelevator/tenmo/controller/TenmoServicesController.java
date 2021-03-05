
package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TenmoServicesDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.exception.UserIdNotFoundException;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

//@PreAuthorize("isAuthenticated()")
@RestController
public class TenmoServicesController {

	private UserDAO userDAO;
	private TenmoServicesDAO tsDAO;
	
	public TenmoServicesController(TenmoServicesDAO tsDAO) {
		this.tsDAO = tsDAO;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET)
	public BigDecimal getBalance(@PathVariable int id) {
		return tsDAO.getUserCurrentBalanceByID(id);
	}

	@PreAuthorize("permitAll")
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> getAll() {
		return tsDAO.getAllUsers();
	}
	
	
	//@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfers", method = RequestMethod.POST)
	public String createTransfer(@RequestBody Transfers newTransfer) throws UserIdNotFoundException {
		return tsDAO.transfer(newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
	}
	
	
	
//	@PreAuthorize("permitAll")
//	
//	
//	
//	@PreAuthorize("permitAll")
	
	
	
}
