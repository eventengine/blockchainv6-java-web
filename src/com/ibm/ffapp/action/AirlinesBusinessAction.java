package com.ibm.ffapp.action;

import static com.ibm.utils.CommonUtil.serializeThowable;
import static com.ibm.utils.CommonUtil.toJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.app.web.frmwk.WebActionHandler;
import com.ibm.app.web.frmwk.annotations.RequestMapping;
import com.ibm.app.web.frmwk.bean.ModelAndView;
import com.ibm.app.web.frmwk.bean.ViewType;
import com.ibm.ffapp.bean.FlightBookingDetails;
import com.ibm.ffapp.bean.Itinerary;
import com.ibm.ffapp.bean.ServiceResponse;
import com.ibm.ffapp.bean.TransferObject;
import com.ibm.ffapp.dao.LoyaltyDataDAO;
import com.ibm.ffapp.hldao.ApplicationConstants;
import com.ibm.ffapp.hldao.LoyaltyDataHLDAO;
import com.ibm.hyperledger.client.HyperLedgerResponse;
import com.ibm.utils.CommonUtil;
import com.ibm.utils.RandomDataGenUtil;
import com.sun.istack.internal.logging.Logger;

public class AirlinesBusinessAction implements WebActionHandler {

	private static final Logger _LOGGER = Logger.getLogger(AirlinesBusinessAction.class);
	private static final Map<String, String> _FLIGHT_MAP = new HashMap<>();
	private static final Map<String, String> _OPERATOR_MAP = new HashMap<>();

	/**
	 * Returns the airline booking details for a given user.
	 * @param request
	 * @param response
	 * @return ModelAndView(ViewType.AJAX_VIEW);
	 */
	@RequestMapping("viewAirlineBookingsUser.wss")
	public ModelAndView viewAirlineBookingsUser(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String userId = request.getParameter("userId");
			_LOGGER.log(Level.INFO, "userId received " + userId);

			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Bookings list",
					LoyaltyDataDAO.getAirlineBookingsByUser(userId));

		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in viewAirlineBookingsUser.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION,
					"Exception thrown" + ex.getMessage());
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	/**
	 * This action method performs the mock payment add/redeem loyalty points in
	 * HL
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ViewType.AJAX_VIEW
	 */
	@RequestMapping("makePaymentAirlines.wss")
	public ModelAndView makePaymentAirlines(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String postBody = CommonUtil.getPostBody(request);
			_LOGGER.log(Level.INFO, "PostBody Received for payment " + postBody);
			FlightBookingDetails bookingDeatils = CommonUtil.fromJson(postBody, FlightBookingDetails.class);

			String rewardPoints = bookingDeatils.getString("rewardPoints");
			String redeemPoints = bookingDeatils.getString("redemPoints");
			String ffid = bookingDeatils.getString("ffid");
			String trxnId = "AL" + System.currentTimeMillis();
			boolean saveData = false;
			if (ffid != null) {
				HyperLedgerResponse hlResponse = LoyaltyDataHLDAO.addAndReedemLoyaltyPoints(trxnId, ffid, rewardPoints,
						redeemPoints, "Airlines booking ref " + trxnId, ApplicationConstants.BE_AIRLINE);
				saveData = (hlResponse.isOk() ? true : false);
			} else {
				saveData = true;
			}
			if (saveData) {
				bookingDeatils.put("paymentConfNumber", trxnId);
				bookingDeatils.put("ts", CommonUtil.getTimeStamp());
				
				if (LoyaltyDataDAO.saveFlightBookingDeatils(bookingDeatils)) {
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Payment confirmed",
							bookingDeatils);
				} else {
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR,
							"Unable to save booking details", bookingDeatils);
				}
			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "Hyperledger transaction failed",
						null);
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in getRedemableLoyalityPoint.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION,
					"Exception thrown" + ex.getMessage());
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	/**
	 * Returns the loyalty points available for redemption for a user. Input to
	 * this transaction is ffid
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getRedemableLoyalityPoint.wss")
	public ModelAndView getRedemableLoyalityPoint(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String ffid = request.getParameter("ffid");
			_LOGGER.log(Level.INFO, "FFID received " + ffid);
			HyperLedgerResponse hlResponse = LoyaltyDataHLDAO.retriveTotalLoyaltyPoints(ffid,
					ApplicationConstants.BE_AIRLINE);
			if (hlResponse.isOk()) {
				TransferObject obj = CommonUtil.fromJson(hlResponse.getMessage(), TransferObject.class);

				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Customer", obj);
			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "No records found", null);
			}

		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in getRedemableLoyalityPoint.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION,
					"Exception thrown" + ex.getMessage());
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	@RequestMapping("searchFlights.wss")
	public ModelAndView searchFlights(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String postBody = CommonUtil.getPostBody(request);
			_LOGGER.log(Level.INFO, "PostBody Received " + postBody);
			Itinerary srchItr = CommonUtil.fromJson(postBody, Itinerary.class);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Filghts", searchFlightsMock(srchItr));

		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in searchFlights.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION,
					"Exception thrown" + ex.getMessage());
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	private List<Itinerary> searchFlightsMock(Itinerary srchItr) {
		final String[] operatorList = { "Lufthansa", "Dragon Air", "Singapore Airlines", "AirIndia", "AirAsia",
				"IndiGo" };
		int maxResults = RandomDataGenUtil.getRandomNumberBelow(4) + 1;
		List<Itinerary> searchResults = new ArrayList<>();
		int passengerCount = getInt(srchItr.getAdultPassengerCount());

		for (int index = 0; index < maxResults; index++) {
			int price = (RandomDataGenUtil.getRandomNumberBelow(10) + 1) * 1500 * passengerCount;
			int rewards = price / 100;
			Itinerary itenary = new Itinerary();
			itenary.setOriginAirport(srchItr.getOriginAirport());
			itenary.setDestAirport(srchItr.getDestAirport());
			itenary.setAdultPassengerCount(passengerCount + "");
			itenary.setPrice(price + "");
			itenary.setRewardPoints(rewards + "");
			itenary.setTravelDate(srchItr.getTravelDate());
			itenary.setOperator(RandomDataGenUtil.pickupFromList(operatorList));
			itenary.setDepartureTime(RandomDataGenUtil.getRandomTimeOfDay());
			itenary.setFlightNumber(
					getFlightNumber(itenary.getOriginAirport(), itenary.getDestAirport(), itenary.getOperator()));
			searchResults.add(itenary);
		}
		return searchResults;
	}

	private String getFlightNumber(String origin, String dest, String operator) {
		if (_FLIGHT_MAP.size() == 0) {
			loadFlightMap();
		}
		return _FLIGHT_MAP.get(_OPERATOR_MAP.get(operator) + "_" + origin + "_" + dest);
	}

	private void loadFlightMap() {
		_OPERATOR_MAP.put("Lufthansa", "LH");
		_OPERATOR_MAP.put("Dragon Air", "KA");
		_OPERATOR_MAP.put("Singapore Airlines", "SQ");
		_OPERATOR_MAP.put("AirIndia", "AI");
		_OPERATOR_MAP.put("AirAsia", "I5");
		_OPERATOR_MAP.put("IndiGo", "6E");

		_FLIGHT_MAP.put("LH_BOM_CCU", "LH198");
		_FLIGHT_MAP.put("LH_BOM_MAA", "LH190");
		_FLIGHT_MAP.put("LH_BOM_DEL", "LH182");
		_FLIGHT_MAP.put("LH_CCU_BOM", "LH168");
		_FLIGHT_MAP.put("LH_CCU_MAA", "LH191");
		_FLIGHT_MAP.put("LH_CCU_DEL", "LH169");
		_FLIGHT_MAP.put("LH_MAA_CCU", "LH175");
		_FLIGHT_MAP.put("LH_MAA_DEL", "LH171");
		_FLIGHT_MAP.put("LH_MAA_BOM", "LH178");
		_FLIGHT_MAP.put("LH_DEL_CCU", "LH129");
		_FLIGHT_MAP.put("LH_DEL_BOM", "LH176");
		_FLIGHT_MAP.put("LH_DEL_MAA", "LH125");
		_FLIGHT_MAP.put("KA_BOM_CCU", "KA323");
		_FLIGHT_MAP.put("KA_BOM_MAA", "KA400");
		_FLIGHT_MAP.put("KA_BOM_DEL", "KA306");
		_FLIGHT_MAP.put("KA_CCU_BOM", "KA375");
		_FLIGHT_MAP.put("KA_CCU_MAA", "KA365");
		_FLIGHT_MAP.put("KA_CCU_DEL", "KA397");
		_FLIGHT_MAP.put("KA_MAA_CCU", "KA319");
		_FLIGHT_MAP.put("KA_MAA_DEL", "KA376");
		_FLIGHT_MAP.put("KA_MAA_BOM", "KA372");
		_FLIGHT_MAP.put("KA_DEL_CCU", "KA313");
		_FLIGHT_MAP.put("KA_DEL_BOM", "KA400");
		_FLIGHT_MAP.put("KA_DEL_MAA", "KA397");
		_FLIGHT_MAP.put("SQ_BOM_CCU", "SQ432");
		_FLIGHT_MAP.put("SQ_BOM_MAA", "SQ433");
		_FLIGHT_MAP.put("SQ_BOM_DEL", "SQ424");
		_FLIGHT_MAP.put("SQ_CCU_BOM", "SQ440");
		_FLIGHT_MAP.put("SQ_CCU_MAA", "SQ422");
		_FLIGHT_MAP.put("SQ_CCU_DEL", "SQ415");
		_FLIGHT_MAP.put("SQ_MAA_CCU", "SQ420");
		_FLIGHT_MAP.put("SQ_MAA_DEL", "SQ439");
		_FLIGHT_MAP.put("SQ_MAA_BOM", "SQ440");
		_FLIGHT_MAP.put("SQ_DEL_CCU", "SQ431");
		_FLIGHT_MAP.put("SQ_DEL_BOM", "SQ423");
		_FLIGHT_MAP.put("SQ_DEL_MAA", "SQ426");
		_FLIGHT_MAP.put("AI_BOM_CCU", "AI910");
		_FLIGHT_MAP.put("AI_BOM_MAA", "AI920");
		_FLIGHT_MAP.put("AI_BOM_DEL", "AI947");
		_FLIGHT_MAP.put("AI_CCU_BOM", "AI925");
		_FLIGHT_MAP.put("AI_CCU_MAA", "AI925");
		_FLIGHT_MAP.put("AI_CCU_DEL", "AI940");
		_FLIGHT_MAP.put("AI_MAA_CCU", "AI922");
		_FLIGHT_MAP.put("AI_MAA_DEL", "AI941");
		_FLIGHT_MAP.put("AI_MAA_BOM", "AI933");
		_FLIGHT_MAP.put("AI_DEL_CCU", "AI950");
		_FLIGHT_MAP.put("AI_DEL_BOM", "AI910");
		_FLIGHT_MAP.put("AI_DEL_MAA", "AI913");
		_FLIGHT_MAP.put("I5_BOM_CCU", "I5806");
		_FLIGHT_MAP.put("I5_BOM_MAA", "I5846");
		_FLIGHT_MAP.put("I5_BOM_DEL", "I5863");
		_FLIGHT_MAP.put("I5_CCU_BOM", "I5856");
		_FLIGHT_MAP.put("I5_CCU_MAA", "I5899");
		_FLIGHT_MAP.put("I5_CCU_DEL", "I5922");
		_FLIGHT_MAP.put("I5_MAA_CCU", "I5900");
		_FLIGHT_MAP.put("I5_MAA_DEL", "I5869");
		_FLIGHT_MAP.put("I5_MAA_BOM", "I5835");
		_FLIGHT_MAP.put("I5_DEL_CCU", "I5853");
		_FLIGHT_MAP.put("I5_DEL_BOM", "I5902");
		_FLIGHT_MAP.put("I5_DEL_MAA", "I5868");
		_FLIGHT_MAP.put("6E_BOM_CCU", "6E426");
		_FLIGHT_MAP.put("6E_BOM_MAA", "6E559");
		_FLIGHT_MAP.put("6E_BOM_DEL", "6E472");
		_FLIGHT_MAP.put("6E_CCU_BOM", "6E510");
		_FLIGHT_MAP.put("6E_CCU_MAA", "6E521");
		_FLIGHT_MAP.put("6E_CCU_DEL", "6E559");
		_FLIGHT_MAP.put("6E_MAA_CCU", "6E457");
		_FLIGHT_MAP.put("6E_MAA_DEL", "6E510");
		_FLIGHT_MAP.put("6E_MAA_BOM", "6E524");
		_FLIGHT_MAP.put("6E_DEL_CCU", "6E481");
		_FLIGHT_MAP.put("6E_DEL_BOM", "6E485");
		_FLIGHT_MAP.put("6E_DEL_MAA", "6E529");

	}

	private int getInt(String str) {
		int value = 0;
		try {
			value = Integer.parseInt(str.trim());
		} catch (Exception ex) {

		}
		return value;
	}
}
