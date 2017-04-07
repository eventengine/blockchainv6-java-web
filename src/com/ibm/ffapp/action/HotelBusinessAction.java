package com.ibm.ffapp.action;

import static com.ibm.utils.CommonUtil.serializeThowable;
import static com.ibm.utils.CommonUtil.toJson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.app.web.frmwk.WebActionHandler;
import com.ibm.app.web.frmwk.annotations.RequestMapping;
import com.ibm.app.web.frmwk.bean.ModelAndView;
import com.ibm.app.web.frmwk.bean.ViewType;
import com.ibm.ffapp.bean.HotelBookingDetails;
import com.ibm.ffapp.bean.ServiceResponse;
import com.ibm.ffapp.bean.TransferObject;
import com.ibm.ffapp.dao.LoyaltyDataDAO;
import com.ibm.ffapp.hldao.ApplicationConstants;
import com.ibm.ffapp.hldao.LoyaltyDataHLDAO;
import com.ibm.hyperledger.client.HyperLedgerResponse;
import com.ibm.utils.CommonUtil;
import com.ibm.utils.RandomDataGenUtil;

public class HotelBusinessAction implements WebActionHandler {

	private static final Logger _LOGGER = Logger.getLogger(HotelBusinessAction.class.getName());

	/**
	 * Returns the airline booking details for a given user.
	 * @param request
	 * @param response
	 * @return ModelAndView(ViewType.AJAX_VIEW);
	 */
	@RequestMapping("viewHotelBookingsUser.wss")
	public ModelAndView viewHotelBookingsUser(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String userId = request.getParameter("userId");
			_LOGGER.log(Level.INFO, "userId received " + userId);

			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Bookings list",
					LoyaltyDataDAO.getHotelBookingsByUser(userId));

		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in viewHotelBookingsUser.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION,
					"Exception thrown" + ex.getMessage());
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}
	
	/**
	 * Search rooms using some mock item
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("searchRooms.wss")
	public ModelAndView searchRooms(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {
			String location = request.getParameter("location");
			String startDay = request.getParameter("checkinDate");
			String endDay = request.getParameter("checkoutDate");
			int guests = Integer.parseInt(request.getParameter("guestCount"));

			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Reservation Info",
					getHotelReservationInfo(location, guests, days(startDay, endDay)));

		} catch (Throwable ex) {
			_LOGGER.log(Level.WARNING, "Failed in searchRooms.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION, "Exception thrown");
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
	@RequestMapping("makePaymentHotel.wss")
	public ModelAndView makePaymentAirlines(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String postBody = CommonUtil.getPostBody(request);
			_LOGGER.log(Level.INFO, "PostBody Received for payment " + postBody);
			HotelBookingDetails paymentInfo = CommonUtil.fromJson(postBody, HotelBookingDetails.class);

			String loaltyPoints = paymentInfo.getString("loaltyPoints");
			String redeemPoints = paymentInfo.getString("redemPoints");
			String ffid = paymentInfo.getString("ffid");
			String trxnId = "HPC" + System.currentTimeMillis();
			boolean saveDetails = false;
			if(ffid!=null){
			HyperLedgerResponse hlResponse = LoyaltyDataHLDAO.addAndReedemLoyaltyPoints(trxnId, ffid, loaltyPoints,
					redeemPoints, "Hotel booking ref " + trxnId, ApplicationConstants.BE_HOTEL);
				saveDetails = (hlResponse.isOk()?true:false);
			}
			else
			{
				saveDetails = true;
			}
			if (saveDetails) {
				paymentInfo.put("paymentConfNumber", trxnId);
				paymentInfo.put("ts", CommonUtil.getTimeStamp());
				if(LoyaltyDataDAO.saveHotelBookingDeatils(paymentInfo))
				{
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Payment confirmed", paymentInfo);
				}
				else
				{
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "Unable to save details",
							null);
				}
			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "Hyperledger transaction failed",
						null);
			}
		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in makePaymentHotel.wss ", ex);
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
	@RequestMapping("getLoyalityPointForHotel.wss")
	public ModelAndView getRedemableLoyalityPoint(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {

			String ffid = request.getParameter("ffid");
			_LOGGER.log(Level.INFO, "FFID received " + ffid);
			HyperLedgerResponse hlResponse = LoyaltyDataHLDAO.retriveTotalLoyaltyPoints(ffid,
					ApplicationConstants.BE_HOTEL);
			if (hlResponse.isOk()) {
				TransferObject obj = CommonUtil.fromJson(hlResponse.getMessage(), TransferObject.class);

				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Available points", obj);
			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "No records found", null);
			}

		} catch (Exception ex) {
			_LOGGER.log(Level.WARNING, "Failed in getLoyalityPointForHotel.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION,
					"Exception thrown" + ex.getMessage());
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	private int days(String startDay, String endDay) {
		return 3;
	}

	private List<TransferObject> getHotelReservationInfo(String location, int noOfGuests, int days) {
		List<TransferObject> roomsDetails = new ArrayList<>();
		String[] roomTypes = new String[] { "Delux", "Executive", "Suite" };
		int count=1;
		for (String roomType : roomTypes) {
			TransferObject reservationInfo = new TransferObject();
			long price = Long.parseLong(RandomDataGenUtil.pickupFromList(new String[] { "2000", "2500", "3000" }))
					* noOfGuests*count;
			reservationInfo.put("propName", "Hotel ABC International," + location);
			reservationInfo.put("roomType", roomType);
			reservationInfo.put("pricePerDay", String.valueOf(price));
			reservationInfo.put("total", price * days);
			reservationInfo.put("plan", RandomDataGenUtil
					.pickupFromList(new String[] { "Continental Plan", "European Plan", "American Plan" }));
			reservationInfo.put("loaltyPoints", (price * days)/75);
			roomsDetails.add(reservationInfo);

		}
		return roomsDetails;
	}

}
