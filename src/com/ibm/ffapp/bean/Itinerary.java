package com.ibm.ffapp.bean;

public class Itinerary {

	private String originAirport;
	private String destAirport;
	private String travelDate;
	private String adultPassengerCount;
	private String rewardPoints;
	private String departureTime;
	private String price;
	private String operator;
	private String flightNumber;
	
	/**
	 * Constructor
	 */
	public Itinerary(){
		super();
	}

	/**
	 * @return the originAirport
	 */
	public String getOriginAirport() {
		return originAirport;
	}

	/**
	 * @param originAirport the originAirport to set
	 */
	public void setOriginAirport(String originAirport) {
		this.originAirport = originAirport;
	}

	/**
	 * @return the destAirport
	 */
	public String getDestAirport() {
		return destAirport;
	}

	/**
	 * @param destAirport the destAirport to set
	 */
	public void setDestAirport(String destAirport) {
		this.destAirport = destAirport;
	}

	/**
	 * @return the travelDate
	 */
	public String getTravelDate() {
		return travelDate;
	}

	/**
	 * @param travelDate the travelDate to set
	 */
	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	/**
	 * @return the adultPassengerCount
	 */
	public String getAdultPassengerCount() {
		return adultPassengerCount;
	}

	/**
	 * @param adultPassengerCount the adultPassengerCount to set
	 */
	public void setAdultPassengerCount(String adultPassengerCount) {
		this.adultPassengerCount = adultPassengerCount;
	}

	/**
	 * @return the rewardPoints
	 */
	public String getRewardPoints() {
		return rewardPoints;
	}

	/**
	 * @param rewardPoints the rewardPoints to set
	 */
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	/**
	 * @return the departureTime
	 */
	public String getDepartureTime() {
		return departureTime;
	}

	/**
	 * @param departureTime the departureTime to set
	 */
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * @return the flightNumber
	 */
	public String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * @param flightNumber the flightNumber to set
	 */
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
}
