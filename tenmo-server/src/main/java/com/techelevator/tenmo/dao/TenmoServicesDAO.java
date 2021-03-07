package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.exception.TransferIdNotFoundException;
import com.techelevator.tenmo.exception.UserIdNotFoundException;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

public interface TenmoServicesDAO {

	
	BigDecimal getUserCurrentBalanceByID(int userID);
	
	List<User> getAllUsers();
	
	String transfer (int fromUser, int toUser, BigDecimal amountTEBucks) throws UserIdNotFoundException;
	
	
	List<Transfers> getAllTransfers();
	
	Transfers getTransferByID (long transferID) throws TransferIdNotFoundException;

	String UpdateFromUserBalance(int fromUser, int toUser, BigDecimal amountTEBucks) throws UserIdNotFoundException;

	void UpdateToUserBalance(int toUser, BigDecimal amountTEBucks) throws UserIdNotFoundException; 
}
