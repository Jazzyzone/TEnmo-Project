package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

public class Transfer {

	private int transfer_id;
	
//	@NotBlank(message = "This field can must have a value.")
	private int transfer_type_id;
	
//	@NotBlank(message = "This field can must have a value.")
	private int transfer_status_id;
	
//	@NotBlank(message = "This field can must have a value.")
	private int account_from;
	
//	@NotBlank(message = "This field can must have a value.")
	private int account_to;
	
//	@NotBlank(message = "This field can must have a value.")
	private BigDecimal amount;
	
	//private String toUsername;
	//private String fromUsername;
	
//	public Transfers(int transferID, int transferTypeID, int transferStatusID, int accountFrom, int accountTo, BigDecimal amount)
//			//,String toUsername, String fromUsername) 
//			{
//		
//		this.transferID = transferID;
//		this.transferTypeID = transferTypeID;
//		this.transferStatusID = transferStatusID;
//		this.accountFrom = accountFrom;
//		this.accountTo = accountTo;
//		this.amount = amount;
//		//this.toUsername = toUsername;
//		//this.fromUsername = fromUsername;
//	}


	public int getTransfer_id() {
		return transfer_id;
	}

	public void setTransfer_id(int transfer_id) {
		this.transfer_id = transfer_id;
	}

	public int getTransfer_type_id() {
		return transfer_type_id;
	}

	public void setTransfer_type_id(int transfer_type_id) {
		this.transfer_type_id = transfer_type_id;
	}

	public int getTransfer_status_id() {
		return transfer_status_id;
	}

	public void setTransfer_status_id(int transfer_status_id) {
		this.transfer_status_id = transfer_status_id;
	}

	public int getAccount_from() {
		return account_from;
	}

	public void setAccount_from(int account_from) {
		this.account_from = account_from;
	}

	public int getAccount_to() {
		return account_to;
	}

	public void setAccount_to(int account_to) {
		this.account_to = account_to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Transfers [transferID=" + transfer_id + ", transfer_type_id=" + transfer_type_id
				+ ", transfer_status_id=" + transfer_status_id + ", account_from=" + account_from + ", account_to="
				+ account_to + ", amount=" + amount + "]";
	}

//	public String getToUsername() {
//		return toUsername;
//	}
//
//	public void setToUsername(String toUsername) {
//		this.toUsername = toUsername;
//	}
//
//	public String getFromUsername() {
//		return fromUsername;
//	}
//
//	public void setFromUsername(String fromUsername) {
//		this.fromUsername = fromUsername;
//	}
	
	

}
