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
import com.techelevator.tenmo.models.Transfer;
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

		} catch (RestClientResponseException ex) {
			throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return balance;

	}

	public void listUsers() throws TenmoServiceException {

		try {
			User[] userArray = restTemplate
					.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

			for (User thisUser : userArray) {

				if (App.USER_ID != thisUser.getId()) {
					System.out.println(thisUser.getId() + "\t\t" + thisUser.getUsername());
				}

			}
		} catch (RestClientResponseException ex) {
			throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
	}

	public boolean makeATransfer(int accountFromUserId, int accountToUserId, BigDecimal amountTEBucks)
			throws TenmoServiceException {

		int fromUserAccountId = 0;

		int toUserAccountId = 0;

		fromUserAccountId = restTemplate.exchange(BASE_URL + "/accounts/" + accountFromUserId + "/accountid",
				HttpMethod.GET, makeAuthEntity(), int.class).getBody();
		toUserAccountId = restTemplate.exchange(BASE_URL + "/accounts/" + accountToUserId + "/accountid",
				HttpMethod.GET, makeAuthEntity(), int.class).getBody();

		Transfer transferObject = new Transfer();

		transferObject.setTransfer_status_id(2);
		transferObject.setTransfer_type_id(2);
		transferObject.setAccount_from(fromUserAccountId);
		transferObject.setAccount_to(toUserAccountId);
		transferObject.setAmount(amountTEBucks);

		BigDecimal currentUserBalance = viewCurrentBalance(App.USER_ID);
		// double balance = currentUserBalance.doubleValue();

		BigDecimal currentUserUpdatedBalance = currentUserBalance.subtract(amountTEBucks);

		BigDecimal zeroBalance = new BigDecimal(0);
		// double zero = zeroBalance.doubleValue();

		// checking for 0 or more dollar balance after transfer
		if (currentUserUpdatedBalance.compareTo(zeroBalance) >= 0) {

			try {

				restTemplate.exchange(BASE_URL + "/transfers", HttpMethod.POST, makeTransferEntity(transferObject),
						Transfer.class);
				return true;
			} catch (RestClientResponseException ex) {

				throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			}
		}
		return false;

	}

	public void currentUserAccountUpdate(int userId, BigDecimal amountTEBucks) throws TenmoServiceException {

		BigDecimal currentUserBalance = viewCurrentBalance(App.USER_ID);

		BigDecimal currentUserUpdatedBalance = currentUserBalance.subtract(amountTEBucks);

		// Accounts accountObject = new Accounts(App.USER_ID,
		// currentUserUpdatedBalance);
		Accounts accountObject = new Accounts();

		accountObject.setUser_id(userId);
		accountObject.setBalance(currentUserUpdatedBalance);

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

		// Accounts accountObject = new Accounts(App.TO_USER_ID,
		// currentUserUpdatedBalance);

		Accounts accountObject = new Accounts();
		accountObject.setUser_id(userId);
		accountObject.setBalance(currentUserUpdatedBalance);

		try {
			restTemplate.exchange(BASE_URL + "/accounts/" + App.TO_USER_ID + "/increased/balance", HttpMethod.PUT,
					makeAccountEntity(accountObject), Accounts.class);

		} catch (RestClientResponseException ex) {

			throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}

	}

	public void listTransfers() {

		String currentUsername = "";
		String fromUsername = "";

		String toUsername = "";

		int toUserId = 0;
		
		int fromUserId = 0;

		//current logged in user
	//	currentUsername = restTemplate.exchange(BASE_URL + "/users/" + App.USER_ID + "/username", HttpMethod.GET,makeAuthEntity(), String.class).getBody();


			Transfer[] transferArray = restTemplate
					.exchange(BASE_URL + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();

			for (Transfer thisTransfer : transferArray) {

				toUserId = restTemplate.exchange(BASE_URL + "/accounts/" + thisTransfer.getAccount_to() + "/userId",
						HttpMethod.GET, makeAuthEntity(), int.class).getBody();

				toUsername = restTemplate.exchange(BASE_URL + "/users/" + toUserId + "/username", HttpMethod.GET,
						makeAuthEntity(), String.class).getBody();
				
				// EXCHANGE FROM_ACCOUNT FOR FROM USERNAME
			if (App.USER_NAME.equals(toUsername)) {
				
				fromUserId = restTemplate.exchange(BASE_URL + "/accounts/" + thisTransfer.getAccount_from() + "/userId",
						HttpMethod.GET, makeAuthEntity(), int.class).getBody();

				fromUsername = restTemplate.exchange(BASE_URL + "/users/" + fromUserId + "/username", HttpMethod.GET,
						makeAuthEntity(), String.class).getBody();
				
				
				System.out.println(
						thisTransfer.getTransfer_id() + "\t\tFrom: " + fromUsername + "\t\t\t" + thisTransfer.getAmount());

			}
		} 
				

			Transfer[] transferArray2 = restTemplate.exchange(BASE_URL + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();

			for (Transfer thisTransfer : transferArray2) {

				fromUserId = restTemplate.exchange(BASE_URL + "/accounts/" + thisTransfer.getAccount_from() + "/userId",
						HttpMethod.GET, makeAuthEntity(), int.class).getBody();

				fromUsername = restTemplate.exchange(BASE_URL + "/users/" + fromUserId + "/username", HttpMethod.GET,
						makeAuthEntity(), String.class).getBody();
				
				if (App.USER_NAME.equals(fromUsername)) {
					
					toUserId = restTemplate.exchange(BASE_URL + "/accounts/" + thisTransfer.getAccount_to() + "/userId",
							HttpMethod.GET, makeAuthEntity(), int.class).getBody();

					toUsername = restTemplate.exchange(BASE_URL + "/users/" + toUserId + "/username", HttpMethod.GET,
							makeAuthEntity(), String.class).getBody();
					
					// EXCHANGE TO_ACCOUNT FOR USERNAME
					System.out.println(thisTransfer.getTransfer_id() + "\t\tTo: " + toUsername + "\t\t\t"
							+ thisTransfer.getAmount());
				
			}
		}

		////
		//// }
		//// } catch (RestClientResponseException ex) {
		//// throw new TenmoServiceException(ex.getRawStatusCode() + " : " +
		//// ex.getResponseBodyAsString());
		//

	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(App.AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;

	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(App.AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
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
