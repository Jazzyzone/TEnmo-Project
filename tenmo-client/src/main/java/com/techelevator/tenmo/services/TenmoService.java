package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfers;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;
import com.techelevator.tenmo.models.AuthenticatedUser;

public class TenmoService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	private final ConsoleService console = new ConsoleService(System.in, System.out);
	private AuthenticatedUser currentUser = new AuthenticatedUser();
	
	public TenmoService(String url) {
		BASE_URL = url;
	}
	
	public BigDecimal viewCurrentBalance(int userId) throws TenmoServiceException {
	
		BigDecimal balance = null;
		
		try {
		balance = restTemplate.exchange(BASE_URL + "/accounts/" + userId + "/balance", HttpMethod.GET,
				makeAuthEntity(), BigDecimal.class).getBody();
		
		}catch (RestClientResponseException ex) {
           throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
		return balance;
		
	}
	
	public void listUsers() throws TenmoServiceException{
	
		
		try {
			User[] userArray = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET,
					makeAuthEntity(), User[].class).getBody();
			
			for (User thisUser : userArray) {
				
				
				if(App.USER_ID != thisUser.getId()) {	
					System.out.println(thisUser.getId() + "\t\t" + thisUser.getUsername());
				}
			
			}
		} catch (RestClientResponseException ex) {
			throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
	}
	
	public String makeATransfer(int accountFromId, int accountToId, BigDecimal amountTEBucks) throws TenmoServiceException {
		
		Transfers transferObject = new Transfers(2, 2, accountFromId, accountToId, amountTEBucks);

		BigDecimal currentUserBalance = viewCurrentBalance(App.USER_ID);
		
		BigDecimal currentUserUpdatedBalance = currentUserBalance.subtract(amountTEBucks);
		
		BigDecimal zeroBalance = new BigDecimal(0);
		
		//checking for 0 or more dollar balance after transfer
		if (currentUserUpdatedBalance.compareTo(zeroBalance) >= 0) {

		    try {
		      transferObject = restTemplate.postForObject(BASE_URL + "/transfers", makeTransferEntity(transferObject), 
		    		  Transfers.class);
		    } catch (RestClientResponseException ex) {
		    	
		      throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		    }

		    return "Congratulations, your transfer was successful";
		}
		
		return "Sorry, insufficient funds";
	}
	
	
	public void currentUserAccountUpdate(int userId, BigDecimal amountTEBucks) throws TenmoServiceException {
		
		BigDecimal currentUserBalance = viewCurrentBalance(App.USER_ID);
		
		BigDecimal currentUserUpdatedBalance = currentUserBalance.subtract(amountTEBucks);
		
		Accounts accountObject = new Accounts(App.USER_ID, currentUserUpdatedBalance);
		
		 try {
		      restTemplate.exchange(BASE_URL + "/accounts/" + App.USER_ID + "/decreased/balance", HttpMethod.PUT, 
		    		  
		    		  makeAccountEntity(accountObject), Accounts.class);
		    } catch (RestClientResponseException ex) {
		    	
		      throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		    }
		
	}
	
public void toUserAccountUpdate(int userId, BigDecimal amountTEBucks) throws TenmoServiceException {
		
		BigDecimal toUserBalance = viewCurrentBalance(App.TO_USER_ID);
		
		BigDecimal currentUserUpdatedBalance = toUserBalance.add(amountTEBucks);
		
		Accounts accountObject = new Accounts(App.TO_USER_ID, currentUserUpdatedBalance);
		
		 try {
		      restTemplate.exchange(BASE_URL + "/accounts/" + App.TO_USER_ID + "/increased/balance", HttpMethod.PUT, 
		    		  
		    		  makeAccountEntity(accountObject), Accounts.class);
		    } catch (RestClientResponseException ex) {
		    	
		      throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		    }
		
	}

	
	
	private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(App.AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
	
	}
	
	private HttpEntity<Transfers> makeTransferEntity(Transfers transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(App.AUTH_TOKEN);
        HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
	
	private HttpEntity<Accounts> makeAccountEntity(Accounts account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(App.AUTH_TOKEN);
        HttpEntity<Accounts> entity = new HttpEntity<>(account, headers);
        return entity;
    }
}
