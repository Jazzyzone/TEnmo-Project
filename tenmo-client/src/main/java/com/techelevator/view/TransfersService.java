package com.techelevator.view;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfers;

public class TransfersService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	private final ConsoleService console = new ConsoleService(System.in, System.out);
	
	public TransfersService(String url) {
		BASE_URL = url;
	}
	
	public BigDecimal viewCurrentBalance(int userId) throws Exception {
	
		Accounts accountObject = null;
		BigDecimal balance = null;
		
		try {
		balance = restTemplate.exchange(BASE_URL + "/accounts/{id}/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
		
		}catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
		return balance;
		
	}
	
	
	private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
	
	}
}
