package com.ibm.ffapp.hldao;

import static com.ibm.ffapp.hldao.ApplicationConstants.APP_PROPS_BUNDLE;
import static com.ibm.ffapp.hldao.ApplicationConstants.BE_AIRLINE;
import static com.ibm.ffapp.hldao.ApplicationConstants.BE_CARSERVICE;
import static com.ibm.ffapp.hldao.ApplicationConstants.BE_HOTEL;
import static com.ibm.ffapp.hldao.ApplicationConstants.BE_RETAIL;
import static com.ibm.ffapp.hldao.ApplicationConstants.HL_CHAIN_CODE_ID;
import static com.ibm.ffapp.hldao.ApplicationConstants.HL_URL;
import static com.ibm.ffapp.hldao.ApplicationConstants.HL_USER_CONTEXT;
import static com.ibm.ffapp.hldao.ApplicationConstants.HL_USER_ID;
import static com.ibm.ffapp.hldao.ApplicationConstants.HL_USER_SECRET;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.ffapp.bean.UserDetails;
import com.ibm.hyperledger.client.HyperLedgerRequest;
import com.ibm.hyperledger.client.HyperLedgerResponse;
import com.ibm.hyperledger.client.HyperledgerClient;
import com.ibm.utils.CommonUtil;
import com.ibm.utils.PropertyManager;

public class LoyaltyDataHLDAO {
	private static final Logger _LOGGER = Logger.getLogger(LoyaltyDataHLDAO.class.getName());

	private static Map<String, Map<String, String>> _credentialMap = null;
	private static String _hyperLedgerUrl_AIRLINE = null;
	private static String _hyperLedgerUrl_CARSERVICE = null;
	private static String _hyperLedgerUrl_HOTEL = null;
	private static String _hyperLedgerUrl_RETAIL = null;

	private static String _chainCode = null;

	/**
	 * Simultaneously adding and redeem loyalty points
	 * 
	 * @param trxnId
	 *            String
	 * @param ffid
	 *            FFID
	 * @param addPoints
	 *            Points to add
	 * @param redeemPoints
	 *            Points to recover
	 * @param businessEntity
	 *            Business entity
	 * @return
	 */
	public static HyperLedgerResponse addAndReedemLoyaltyPoints(String trxnId, String ffid, String addPoints,
			String redeemPoints, String remarks,String businessEntity) {
		HyperLedgerResponse response = null;
		try {
			HyperledgerClient client = getClient(businessEntity);
			if (client.register()) {
				HyperLedgerRequest redeemRequest = buildRedeemLoyaltyPointsRequest(trxnId+"_D", ffid, redeemPoints,
						getUserId(businessEntity));
				response = client.invokeMethod(redeemRequest);
				if (response.isOk()) {
					//delay(5000);
					HyperLedgerRequest addPointsRequest = buildAddLoyaltyPointsRequest(trxnId+"_A", ffid, addPoints,
							remarks,getUserId(businessEntity));
					response = client.invokeMethod(addPointsRequest);
				} else {
					response = new HyperLedgerResponse(false);
					response.setMessage("Point redeemption failed");
				}

			} else {
				response = new HyperLedgerResponse(false);
				response.setMessage("User registration failed");
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "HL User registration failed with ", ex);
			response = new HyperLedgerResponse(false);
			response.setMessage("Exception in User registration:" + ex.getMessage());
		}
		return response;
	}
	private static void delay(long millis){
		try{
			Thread.sleep(millis);
		}catch(Exception ex){
			
		}
	}
	/**
	 * Retrieves total loyalty points of the input ffid
	 * 
	 * @param ffid
	 *            FFID
	 * @param businessEntity
	 *            Business entity
	 * @return HyperLedgerResponse
	 */
	public static HyperLedgerResponse retriveTotalLoyaltyPoints(String ffid, String businessEntity) {
		HyperLedgerResponse response = null;
		try {
			HyperledgerClient client = getClient(businessEntity);
			if (client.register()) {
				HyperLedgerRequest claimRequest = buildGetToatlLoyaltityPointsUser(ffid, getUserId(businessEntity));
				response = client.invokeMethod(claimRequest);
			} else {
				response = new HyperLedgerResponse(false);
				response.setMessage("User registration failed");
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "HL User registration failed with ", ex);
			response = new HyperLedgerResponse(false);
			response.setMessage("Exception in User registration:" + ex.getMessage());
		}
		return response;
	}

	/**
	 * Returns all the transaction of a user.
	 * 
	 * @param ffid
	 *            FFID
	 * @return
	 */
	public static HyperLedgerResponse retrieveAllLoyaltyTrxans(String ffid) {
		HyperLedgerResponse response = null;
		try {
			HyperledgerClient client = getClient(ApplicationConstants.BE_AIRLINE);
			if (client.register()) {
				HyperLedgerRequest claimRequest = buildGetAllTransactionsForUser(ffid,
						getUserId(ApplicationConstants.BE_AIRLINE));
				response = client.invokeMethod(claimRequest);
			} else {
				response = new HyperLedgerResponse(false);
				response.setMessage("User registration failed");
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "HL User registration failed with ", ex);
			response = new HyperLedgerResponse(false);
			response.setMessage("Exception in User registration:" + ex.getMessage());
		}
		return response;
	}

	/**
	 * Add points to a loyalty user
	 * 
	 * @param ffid
	 *            FFID
	 * @param points
	 *            Number of points
	 * @param trxnId
	 *            Trxn id to be generated by the business entity
	 * @param registarer
	 *            Business entity
	 * @return
	 */
	public static HyperLedgerResponse addLoyaltyPoint(String ffid, String points, String trxnId, String remarks,
			String registarer) {
		HyperLedgerResponse response = null;
		try {
			HyperledgerClient client = getClient(registarer);
			if (client.register()) {
				HyperLedgerRequest claimRequest = buildAddLoyaltyPointsRequest(trxnId, ffid, points, remarks,
						getUserId(registarer));
				response = client.invokeMethod(claimRequest);
			} else {
				response = new HyperLedgerResponse(false);
				response.setMessage("User registration failed");
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "HL User registration failed with ", ex);
			response = new HyperLedgerResponse(false);
			response.setMessage("Exception in User registration:" + ex.getMessage());
		}
		return response;
	}

	/**
	 * Registers a user in HL
	 * 
	 * @param user
	 *            UserDetails
	 * @param registarer
	 *            Business Entity
	 * @return HyperLedgerResponse
	 */
	public static HyperLedgerResponse registerUser(UserDetails user, String registarer) {
		HyperLedgerResponse response = null;
		try {
			HyperledgerClient client = getClient(registarer);
			if (client.register()) {
				HyperLedgerRequest claimRequest = buildRegisterUserRequest(user, getUserId(registarer));
				response = client.invokeMethod(claimRequest);
			} else {
				response = new HyperLedgerResponse(false);
				response.setMessage("User registration failed");
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "HL User registration failed with ", ex);
			response = new HyperLedgerResponse(false);
			response.setMessage("Exception in User registration:" + ex.getMessage());
		}
		return response;
	}

	/**
	 * Retrieves an user from HL
	 * 
	 * @param user
	 *            UserDetails
	 * @param registarer
	 *            Business Entity
	 * @return HyperLedgerResponse
	 */
	public static HyperLedgerResponse retrieveUserDetails(String ffid, String registarer) {
		HyperLedgerResponse response = null;
		try {
			HyperledgerClient client = getClient(registarer);
			if (client.register()) {
				HyperLedgerRequest claimRequest = buildGetUserDetailsRequest(ffid, getUserId(registarer));
				response = client.invokeMethod(claimRequest);
			} else {
				response = new HyperLedgerResponse(false);
				response.setMessage("Get user details failed");
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "HL Get user details failed with ", ex);
			response = new HyperLedgerResponse(false);
			response.setMessage("Exception in Get user details:" + ex.getMessage());
		}
		return response;
	}

	private static HyperLedgerRequest buildGetUserDetailsRequest(String ffid, String userId) {
		HyperLedgerRequest request = new HyperLedgerRequest();
		request.setMethod("query");
		request.setChainCodeName(_chainCode);
		request.setCallFunction("getUser");
		request.setFunctionArgs(new String[] { ffid });

		request.setSecureContext(userId);
		return request;
	}

	/**
	 * Builds a HL request to get all the transactions by a loyalty points user
	 * 
	 * @param ffid
	 *            Loyalty user id
	 * 
	 * @return HyperLedgerRequest
	 */
	private static HyperLedgerRequest buildGetAllTransactionsForUser(String ffid, String userId) {
		HyperLedgerRequest request = new HyperLedgerRequest();
		request.setMethod("query");
		request.setChainCodeName(_chainCode);
		request.setCallFunction("getAllTransaction");
		request.setFunctionArgs(new String[] { ffid });

		request.setSecureContext(userId);
		return request;
	}

	/**
	 * Builds a request to retrieve total points for a ffid
	 * 
	 * @param ffid
	 *            FFID
	 * @param userId
	 *            Business Entity
	 * @return HyperLedgerRequest
	 */
	private static HyperLedgerRequest buildGetToatlLoyaltityPointsUser(String ffid, String userId) {
		HyperLedgerRequest request = new HyperLedgerRequest();
		request.setMethod("query");
		request.setChainCodeName(_chainCode);
		request.setCallFunction("getMile");
		request.setFunctionArgs(new String[] { ffid });

		request.setSecureContext(userId);
		return request;
	}

	/**
	 * Builds a HL request to add points
	 * 
	 * @param trxnId
	 *            Trxn id to generated by the business entity
	 * @param ffid
	 *            Loyalty user id
	 * @param points
	 *            Number of points
	 * @param userId
	 *            Business Entity
	 * @return HyperLedgerRequest
	 */
	private static HyperLedgerRequest buildAddLoyaltyPointsRequest(String trxnId, String ffid, String points,
			String remarks, String userId) {
		HyperLedgerRequest request = new HyperLedgerRequest();
		request.setMethod("invoke");
		request.setChainCodeName(_chainCode);
		request.setCallFunction("addDeleteMile");
		request.setFunctionArgs(new String[] { trxnId, CommonUtil.getTimeStamp(), ffid, userId, points, "add", "",
				(remarks == null ? "Added for " + trxnId : remarks) });

		request.setSecureContext(userId);
		return request;
	}

	/**
	 * Builds a HL request to redeem loyalty points
	 * 
	 * @param trxnId
	 *            Trxn id to generated by the business entity
	 * @param ffid
	 *            Loyalty user id
	 * @param points
	 *            Number of points
	 * @param userId
	 *            Business Entity
	 * @return HyperLedgerRequest
	 */
	private static HyperLedgerRequest buildRedeemLoyaltyPointsRequest(String trxnId, String ffid, String points,
			String userId) {
		HyperLedgerRequest request = new HyperLedgerRequest();
		request.setMethod("invoke");
		request.setChainCodeName(_chainCode);
		request.setCallFunction("addDeleteMile");
		request.setFunctionArgs(new String[] { trxnId, CommonUtil.getTimeStamp(), ffid, userId, points, "delete", "",
				"Reedeemed during " + trxnId });

		request.setSecureContext(userId);
		return request;
	}

	private static HyperLedgerRequest buildRegisterUserRequest(UserDetails userDetails, String userId) {
		HyperLedgerRequest request = new HyperLedgerRequest();
		request.setMethod("invoke");
		request.setChainCodeName(_chainCode);
		request.setCallFunction("registerUser");
		request.setFunctionArgs(new String[] { userDetails.getFfId(), userDetails.getTitle(), userDetails.getGender(),
				userDetails.getFirstName(), userDetails.getLastName(), userDetails.getDob(), userDetails.getEmail(),
				userDetails.getCountry(), userDetails.getAddress(), userDetails.getCity(), userDetails.getZip(),
				userId });

		request.setSecureContext(userId);
		return request;
	}

	private static HyperledgerClient getClient(String context) {
		HyperledgerClient client = null;
		init();
		if (context.equals(ApplicationConstants.BE_AIRLINE)) {
			client = new HyperledgerClient(_hyperLedgerUrl_AIRLINE, getUserId(context),
					_credentialMap.get(context).get("secret"));
		} else if (context.equals(ApplicationConstants.BE_CARSERVICE)) {
			client = new HyperledgerClient(_hyperLedgerUrl_CARSERVICE, getUserId(context),
					_credentialMap.get(context).get("secret"));
		} else if (context.equals(ApplicationConstants.BE_HOTEL)) {
			client = new HyperledgerClient(_hyperLedgerUrl_HOTEL, getUserId(context),
					_credentialMap.get(context).get("secret"));
		} else {
			client = new HyperledgerClient(_hyperLedgerUrl_RETAIL, getUserId(context),
					_credentialMap.get(context).get("secret"));
		}

		return client;
	}

	/**
	 * Returns the context and user ids
	 * 
	 * @param context
	 * @return
	 */
	private static String getUserId(String context) {
		if (_credentialMap == null) {
			init();
		}
		return _credentialMap.get(context).get("uid");
	}

	/**
	 * Initialize properties and urls
	 */
	private static void init() {
		if (_hyperLedgerUrl_AIRLINE == null) {
			_hyperLedgerUrl_AIRLINE = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_URL + "." + BE_AIRLINE);
			_hyperLedgerUrl_CARSERVICE = PropertyManager.getStringProperty(APP_PROPS_BUNDLE,
					HL_URL + "." + BE_CARSERVICE);
			_hyperLedgerUrl_HOTEL = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_URL + "." + BE_HOTEL);
			_hyperLedgerUrl_RETAIL = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_URL + "." + BE_RETAIL);
		}
		if (_chainCode == null) {
			_chainCode = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_CHAIN_CODE_ID);
		}
		if (_credentialMap == null) {
			_credentialMap = new HashMap<String, Map<String, String>>();
			String contexts = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_USER_CONTEXT);
			String userIds = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_USER_ID);
			String secrets = PropertyManager.getStringProperty(APP_PROPS_BUNDLE, HL_USER_SECRET);
			String[] secretList = secrets.split(",");
			String[] userList = userIds.split(",");
			String[] contextList = contexts.split(",");
			for (int index = 0; index < contextList.length; index++) {
				Map<String, String> contextMap = new HashMap<>();
				contextMap.put("uid", userList[index]);
				contextMap.put("secret", secretList[index]);
				_credentialMap.put(contextList[index], contextMap);
			}

		}
	}
}
