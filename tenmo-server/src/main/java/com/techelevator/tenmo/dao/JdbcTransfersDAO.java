package com.techelevator.tenmo.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;


public class JdbcTransfersDAO implements TransfersDAO {
	
	private JdbcTemplate jdbcTemplate;

    @Override
	public int getUserCurrentBalanceByID(int userId) {
		
    	String sql = "SELECT balance FROM accounts WHERE user_id = ?";
    	
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
	}

	@Override
	public List<User> getAllExceptUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean transfer(int fromUser, int toUser, int amountTEBucks) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int updateFromUserBalance(int fromUser, int updateAmount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateToUserBalance(int toUser, int updateAmount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Transfers> getAllTransfers(int userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transfers getTransferByID(int transferID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
