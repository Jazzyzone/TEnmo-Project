package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Accounts {

	private int account_id;
	private int user_id;
	private BigDecimal balance;
	
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Accounts [account_id=" + account_id + ", user_id=" + user_id + ", balance=" + balance + "]";
	}
	
	

//	public Accounts(int accountId, int userId, BigDecimal balance) {
//		this.accountId = accountId;
//		this.userId = userId;
//		this.balance = balance;
//	}
//
//	public Accounts(int userId, BigDecimal balance) {
//		this.userId = userId;
//		this.balance = balance;
//	}
	


}
