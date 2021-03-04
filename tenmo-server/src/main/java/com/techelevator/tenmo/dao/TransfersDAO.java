package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

public interface TransfersDAO {

	
	int getUserCurrentBalanceByID(int userID);
	
	List<User> getAllExceptUser();
	
	boolean transfer (int fromUser, int toUser, int amountTEBucks);
	
	void updateFromUserBalance (int fromUser, int updateAmount);
	
	void updateToUserBalance (int toUser, int updateAmount);
	
	List<Transfers> getAllTransfers(int userID);
	
	Transfers getTransferByID (int transferID);
}
