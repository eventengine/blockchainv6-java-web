package com.ibm.ffapp.hldao.test;

import static com.ibm.ffapp.hldao.ApplicationConstants.BE_AIRLINE;
import static com.ibm.utils.RandomDataGenUtil.generateRandomString;
import static com.ibm.utils.RandomDataGenUtil.pickupFromList;

import com.ibm.ffapp.bean.UserDetails;
import com.ibm.ffapp.hldao.ApplicationConstants;
import com.ibm.ffapp.hldao.LoyaltyDataHLDAO;
import com.ibm.hyperledger.client.HyperLedgerResponse;
import com.ibm.utils.CommonUtil;
import com.ibm.utils.PropertyManager;

public class LoyaltyDataDAOTest {
	private static final int TIME_OUT = 10000;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		PropertyManager.initProperty(ApplicationConstants.APP_PROPS_BUNDLE, "application.properties", true);
		registerUserFlow();

	}

	private static void registerUserFlow() throws Exception{
		UserDetails userDetails = new UserDetails();
		String ffId = "TT" + generateRandomString(null, 4);
		userDetails.setFfId(ffId);
		userDetails
				.setFirstName(pickupFromList(new String[] { "Sudip", "Raja", "Suraj", "Saswat", "Prosun", "Balaji" }));
		userDetails.setLastName(pickupFromList(new String[] { "Dutta", "Ghosh", "Pal", "Gomes", "Das", "Krishnan" }));
		userDetails.setAddress(pickupFromList(new String[] { "M.G Road", "New Twon", "Station Road", "Airport Road" }));
		userDetails.setCity(pickupFromList(new String[] { "KOLKATA", "DELHI", "MUMBAI", "CHENNAI" }));
		userDetails.setCountry("IN");
		userDetails.setDob(generateRandomString("012", 2) + "/11/19" + generateRandomString("7890", 2));
		userDetails.setTitle("Mr");
		userDetails.setGender("M");
		userDetails.setZip("99999");
		userDetails.setEmail(userDetails.getFirstName().toLowerCase() + "." + userDetails.getLastName().toLowerCase()
				+ generateRandomString(null, 2) + "@gmail.com");
		HyperLedgerResponse response = LoyaltyDataHLDAO.registerUser(userDetails, BE_AIRLINE);
		System.out.println(CommonUtil.toJson(response));
		Thread.sleep(TIME_OUT);
		response = LoyaltyDataHLDAO.retrieveUserDetails(ffId, BE_AIRLINE);
		System.out.println(CommonUtil.toJson(response));
		
	}

}
