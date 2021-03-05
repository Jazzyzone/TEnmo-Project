
package com.techelevator.tenmo.controller;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.dao.UserDAO;

//@PreAuthorize("isAuthenticated()")
@RestController
public class TransfersController {

	private UserDAO userDAO;
	private TransfersDAO transfersDAO;
	
	public TransfersController(TransfersDAO transfersDAO) {
		this.transfersDAO = transfersDAO;
	}

	@RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET)
	public BigDecimal getBalance(@PathVariable int id) {
		return transfersDAO.getUserCurrentBalanceByID(id);
	}

}
