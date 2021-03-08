package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.exception.TransferIdNotFoundException;
import com.techelevator.tenmo.exception.UserIdNotFoundException;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface TenmoServicesDAO {

	
	BigDecimal getUserCurrentBalanceByID(int userID);
	
	List<User> getAllUsers();
	
	void UpdateToUserBalance(int toUser, BigDecimal amountTEBucks) throws UserIdNotFoundException; 
	
	void UpdateFromUserBalance(int fromUser, BigDecimal amountTEBucks) throws UserIdNotFoundException;
	
	void transfer (int fromUser, int toUser, BigDecimal amountTEBucks) throws UserIdNotFoundException;

	Transfer getTransferByID (long transferID) throws TransferIdNotFoundException;
	
	List<Transfer> getAllTransfers();
	
	int getAccountIdFromUserId(int userId);
	
}
