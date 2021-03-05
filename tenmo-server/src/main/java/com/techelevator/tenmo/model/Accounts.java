package com.techelevator.tenmo.model;

public class Accounts {
	
	private int accountId;
	private int userId;
	private int balance;
	
	
	public Accounts(int accountId, int userId, int balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}

	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getBalance() {
		return balance;
	}


	public void setBalance(int balance) {
		this.balance = balance;
	}


	public int getAccountId() {
		return accountId;
	}
	
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	
	@Override
	public String toString() {
		return "Accounts [accountId=" + accountId + ", userId=" + userId + ", balance=" + balance + "]";
	}


}
