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

public class TenmoService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	private final ConsoleService console = new ConsoleService(System.in, System.out);
	
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
	
	public List<User> listUsers() throws TenmoServiceException{
		
		List<User> allUsers = new ArrayList<>();
		
		
		try {
			User[] userArray = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET,
					makeAuthEntity(), User[].class).getBody();
			
			for (User userArray2 : userArray) {
				allUsers.add(userArray2);
			}
		} catch (RestClientResponseException ex) {
			throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		
		return allUsers;
	}
	
	
	private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
	
	}
}
