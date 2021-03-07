package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

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
				
				
				//if(thisUser.getUsername() != currentUser.getUser().getUsername()) {	
					System.out.println(thisUser.getId() + "\t\t" + thisUser.getUsername());
				//}
	//			allUsers.add(thisUser);
			
			}
		} catch (RestClientResponseException ex) {
			throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
	}

	private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
	
	}
}
