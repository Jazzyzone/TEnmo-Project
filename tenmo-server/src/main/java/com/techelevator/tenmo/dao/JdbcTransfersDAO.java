package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;


public class JdbcTransfersDAO implements TransfersDAO {

	private JdbcTemplate jdbcTemplate;

	@Override
	public int getUserCurrentBalanceByID(int userId) {

		String sql = "SELECT balance FROM accounts WHERE user_id = ?";

		Integer balance = jdbcTemplate.queryForObject(sql, Integer.class, userId);
		
		if (balance != null) {
			return balance;
		} else {
			return -1;
		}
	}

	@Override
	public List<User> getAllExceptUser() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT user_id AS ID, username AS Name FROM users WHERE username NOT ILIKE ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			User user = mapRowToUser(results);
			users.add(user);
		}
		return users;
	}

	@Override
	public boolean transfer(int fromUser, int toUser, int amountTEBucks) {

		if(amountTEBucks > getUserCurrentBalanceByID(fromUser)) {
			return false;
		} else {

			String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount)\r\n" + 
					"VALUES(2, 2, \r\n" + 
					"(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?)), \r\n" + 
					"(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?)),\r\n" + 
					"?)";
			try {
				jdbcTemplate.update(sql, fromUser, toUser, amountTEBucks);
			} catch (DataAccessException e) {
				return false;
			}

			return true;
		}
	}

	@Override
	public void updateFromUserBalance(int fromUser, int updateAmount) {
		
		
		String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		
		jdbcTemplate.update(sql, updateAmount, fromUser);
	}

	@Override
	public void updateToUserBalance(int toUser, int updateAmount) {
		
		
		String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		
		jdbcTemplate.update(sql, updateAmount, toUser);;
	}

	@Override
	public List<Transfers> getAllTransfers(int userID) {
		List<Transfers> allTransfers = new ArrayList<>();
		String sql = "SELECT t.transfer_id AS ID, u.username AS From_To, t.amount AS Amount\r\n" + 
				"FROM transfers AS t INNER JOIN accounts AS a ON a.account_id = t.account_to\r\n" + 
				"INNER JOIN users AS u ON u.user_id = a.user_id WHERE u.username NOT ILIKE ?";
		
		String sql2 = "SELECT t.transfer_id AS ID, u.username AS From_To, t.amount AS Amount\r\n" + 
				"FROM transfers AS t INNER JOIN accounts AS a ON a.account_id = t.account_from\r\n" + 
				"INNER JOIN users AS u ON u.user_id = a.user_id WHERE u.username NOT LIKE ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			Transfers transfers = mapRowToTransfer(results);
			allTransfers.add(transfers);
		}
		return allTransfers;
	}

	@Override
	public Transfers getTransferByID(int transferID) {

		Transfers transferObject = new Transfers(0, 0, 0, 0, 0, 0);
		String sql = "SELECT t.transfer_id AS ID, "
				
				+ "(SELECT username FROM users WHERE user_id = "
					+ "(SELECT user_id FROM accounts WHERE account_id = "
						+ "(SELECT account_from FROM transfers WHERE transfer_id = 3001))) AS From," 
		        
		        + "(SELECT username FROM users WHERE user_id = "
		        	+ "(SELECT user_id FROM accounts WHERE account_id = "
		        		+ "(SELECT account_to FROM transfers WHERE transfer_id = 3001))) AS To,"
		        
		        + "tt.transfer_type_desc AS Type, ts.transfer_status_desc AS Status, t.amount AS Amount"

		+ "FROM transfer_types AS tt"
		       + "INNER JOIN transfers AS t"
		               + "ON t.transfer_type_id = tt.transfer_type_id"
		               + "INNER JOIN accounts AS a"
		                    +    "ON a.account_id = t.account_from"
		                        +"INNER JOIN users AS u"
		                               + "ON u.user_id = a.user_id"
		                                +"INNER JOIN transfer_statuses AS ts"
		                                       + "ON ts.transfer_status_id = t.transfer_status_id"
		+ "WHERE u.username NOT LIKE ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		
		jdbcTemplate.update(sql, transferID);
		
		return null;


	}

	private User mapRowToUser(SqlRowSet rs) {
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password_hash"));
		user.setActivated(true);
		user.setAuthorities("USER");
		return user;
	}

	private Transfers mapRowToTransfer(SqlRowSet ts) {
		Transfers transfer = new Transfers(0, 0, 0, 0, 0, 0);
		transfer.setTransferTypeID(ts.getInt("transfer_type_id"));
		transfer.setTransferStatusID(ts.getInt("transfer_status_id"));
		transfer.setAccountFrom(ts.getInt("account_from"));
		transfer.setAccountTo(ts.getInt("account_to"));
		transfer.setAmount(ts.getInt("amount"));
		return transfer;
	}

}
