package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class Transfers {

	private int transferID;
	
	@NotBlank(message = "This field can must have a value.")
	private int transferTypeID;
	
	@NotBlank(message = "This field can must have a value.")
	private int transferStatusID;
	
	@NotBlank(message = "This field can must have a value.")
	private int accountFrom;
	
	@NotBlank(message = "This field can must have a value.")
	private int accountTo;
	
	@NotBlank(message = "This field can must have a value.")
	private int amount;
	
	public Transfers(int transferID, int transferTypeID, int transferStatusID, int accountFrom, int accountTo, int amount) {
		
		this.transferID = transferID;
		this.transferTypeID = transferTypeID;
		this.transferStatusID = transferStatusID;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;	
	}

	public int getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(int transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public int getTransferStatusID() {
		return transferStatusID;
	}

	public void setTransferStatusID(int transferStatusID) {
		this.transferStatusID = transferStatusID;
	}

	public int getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}

	public int getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getTransferID() {
		return transferID;
	}

	@Override
	public String toString() {
		
		return "Transfers [transferID=" + transferID
				+ ", transferTypeID=" + transferTypeID
				+ ", transferStatusID="+ transferStatusID 
				+ ", accountFrom=" + accountFrom 
				+ ", accountTo=" + accountTo 
				+ ", amount=" + amount+ "]";
	}
	
	
	
}
