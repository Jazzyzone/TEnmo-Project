package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.exception.TransferIdNotFoundException;
import com.techelevator.tenmo.exception.UserIdNotFoundException;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

@Component
public class JdbcTenmoServicesDAO implements TenmoServicesDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcTenmoServicesDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public BigDecimal getUserCurrentBalanceByID(int userId) {

		Accounts account = null;
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";

		BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
		
		return result;
		
		
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		
//		WE EXCLUDED THE PASSWORD HASH AS THE USER SHOULD NEVER HAVE ACCESS TO THIS PRIVALIDGED COMPANY INFORMAION!!!!!!!!!!
//		ALL PASSWORD HASHES APPEAR AS NULL!!!!
		
		String sql = "SELECT user_id, username FROM users";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		
		while(results.next()) {
			User user = mapRowToUser(results);
			users.add(user);
		}
		return users;
	}

	@Override
	public String transfer(int fromUser, int toUser, BigDecimal amountTEBucks) throws UserIdNotFoundException  {

		String responseString = "Transfer unsuccessful.";

			String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) \r\n" + 
					"VALUES(2, 2, ?, ?, ?)";  
					//+"(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?)), \r\n" + 
					//"(SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?)),\r\n" + 
					//"?)";
			
			try {
				jdbcTemplate.update(sql, fromUser, toUser, amountTEBucks);
				
				responseString = "Congratulations! Your transfer was successful!";
			} 
			catch (DataAccessException e) {
				
				return responseString;
			}

			return responseString;
		}

	@Override
	public void updateFromUserBalance(Accounts updatedAccount) {
		
		String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		
		jdbcTemplate.update(sql, updatedAccount.getBalance(), updatedAccount.getUserId());
	}

	@Override
	public void updateToUserBalance(Accounts updatedAccount) {
		
		
		String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		
		jdbcTemplate.update(sql, updatedAccount.getBalance(), updatedAccount.getUserId());
	}

	//Make a method for a username by ID
	
	@Override
	public List<Transfers> getAllTransfers() {
		List<Transfers> allTransfers = new ArrayList<>();
		String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers";
				
//				"SELECT t.transfer_id, u.username, t.amount AS Amount\r\n" + 
//				     "FROM transfers AS t \r\n" + 
//				     "INNER JOIN accounts AS a ON a.account_id = t.account_to OR \r\n" + 
//				                            "a.account_id = t.account_from \r\n" + 
//				     "INNER JOIN users AS u ON u.user_id = a.user_id"; 
//		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			Transfers transferObject = mapRowToTransfer(results);
			allTransfers.add(transferObject);
		}
		return allTransfers;
	}

	@Override
	public Transfers getTransferByID(long transferID) throws TransferIdNotFoundException {

		Transfers transferObject = new Transfers();
		String sql = "SELECT t.transfer_id AS ID, "
				
				+ "(SELECT username FROM users WHERE user_id = "
					+ "(SELECT user_id FROM accounts WHERE account_id = "
						+ "(SELECT account_from FROM transfers WHERE transfer_id = ?))) AS From," 
		        
		        + "(SELECT username FROM users WHERE user_id = "
		        	+ "(SELECT user_id FROM accounts WHERE account_id = "
		        		+ "(SELECT account_to FROM transfers WHERE transfer_id = ?))) AS To,"
		        
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
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferID);
		
		if( results.next()) {
			transferObject = mapRowToTransfer(results);
		}
		
		return transferObject;


	}

	private User mapRowToUser(SqlRowSet rs) {
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setUsername(rs.getString("username"));
//		user.setPassword(rs.getString("password_hash"));  -EXCLUDED FOR SECURITY REASONS-
		user.setActivated(true);
		user.setAuthorities("USER");
		return user;
	}

	private Transfers mapRowToTransfer(SqlRowSet ts) {
		Transfers transfer = new Transfers();
		transfer.setTransferID(ts.getInt("transfer_id"));
		transfer.setTransferTypeID(ts.getInt("transfer_type_id"));
		transfer.setTransferStatusID(ts.getInt("transfer_status_id"));
		transfer.setAccountFrom(ts.getInt("account_from"));
		transfer.setAccountTo(ts.getInt("account_to"));
		transfer.setAmount(ts.getBigDecimal("amount"));
		return transfer;
	}
	
	
//	DO WE NEED THIS ONE????????????????????
	
//	private Accounts mapRowToAccounts(SqlRowSet account) {
//		Accounts accountObject = new Accounts(0, 0, null);
//		accountObject.setAccountId(account.getInt("account_id"));
//		accountObject.setUserId(account.getInt("user_id"));
//		accountObject.setBalance(account.getBigDecimal("balance"));
//		return accountObject;
//	}

}
