package com.ibm.ffapp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.ibm.ffapp.bean.FlightBookingDetails;
import com.ibm.ffapp.bean.HotelBookingDetails;
import com.ibm.ffapp.bean.UserDetails;
import com.ibm.utils.MongoHelper;
import com.ibm.utils.PropertyManager;
import com.mongodb.client.MongoCollection;

/**
 * Data Access Object class for the application. Talks to Mongodb 
 * @author SUDDUTT1
 *
 */
public class LoyaltyDataDAO {

	//private static final Gson _DESERIALIZER = new GsonBuilder().setPrettyPrinting().create();
	private static final String _RECORD_COLLECTION_NAME = "ffdatav2";
	private static final Logger _LOGGER = Logger.getLogger(LoyaltyDataDAO.class.getName());
	
	/**
	 * Returns all hotel bookings done by a user
	 * @param emailid
	 * @return List<HotelBookingDetails> if users exist , null other wise
	 */
	public static List<HotelBookingDetails> getHotelBookingsByUser(String userId) {
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
			List<HotelBookingDetails> records = new ArrayList<>();
			HotelBookingDetails srchObject = new HotelBookingDetails();
			srchObject.append("userId", userId);
			
			for (Document doc : collection.find(srchObject.buildFilter())) {
				HotelBookingDetails existingRecord = new HotelBookingDetails();
				existingRecord.buildInstance(doc);
				records.add(existingRecord);
			}
			return records;
		}
		return null;
	}
	/**
	 * Returns all bookings done by a user
	 * @param emailid
	 * @return List<FlightBookingDetails> if users exist , null other wise
	 */
	public static List<FlightBookingDetails> getAirlineBookingsByUser(String userId) {
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
			List<FlightBookingDetails> records = new ArrayList<>();
			FlightBookingDetails srchObject = new FlightBookingDetails();
			srchObject.append("userId", userId);
			
			for (Document doc : collection.find(srchObject.buildFilter())) {
				FlightBookingDetails existingRecord = new FlightBookingDetails();
				existingRecord.buildInstance(doc);
				records.add(existingRecord);
			}
			return records;
		}
		return null;
	}
	/**
	 * Returns all the users registered in the system 
	 * @param emailid
	 * @return List<UserDetails> if users exist , null other wise
	 */
	public static List<UserDetails> getAllUsers(String businessEntity) {
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
			UserDetails srchObject = new UserDetails();
			List<UserDetails> usersList = new ArrayList<>();
			for (Document doc : collection.find(srchObject.buildFilter())) {
				UserDetails existingRecord = new UserDetails();
				existingRecord.buildInstance(doc);
				existingRecord.setPwd("XXXX");
				existingRecord.setConfpwd("XXXX");
				usersList.add(existingRecord);
			}
			return usersList;
		}
		return null;
	}
	
	/**
	 * Searches a user based on the email id 
	 * @param emailid
	 * @return UserDetails if user exists , null other wise
	 */
	public static UserDetails getUserDetails(String emailid) {
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
			UserDetails srchObject = new UserDetails();
			srchObject.setEmail(emailid);
			for (Document doc : collection.find(srchObject.buildFilter())) {
				UserDetails existingRecord = new UserDetails();
				existingRecord.buildInstance(doc);
				return existingRecord;
			}
		}
		return null;
	}
	/**
	 * Save flight booking details
	 * @param bookingDetails  FlightBookingDetails
	 * @return boolean true if save succeeds 
	 */
	public static boolean saveFlightBookingDeatils(FlightBookingDetails bookingDetails) {
		boolean isSucess= false;
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
				collection.insertOne(Document.parse(bookingDetails.toJson()));
				isSucess = true;
			
		}
		return isSucess;
	}
	/**
	 * Save hotel booking details
	 * @param bookingDetails  HotelBookingDetails
	 * @return boolean true if save succeeds 
	 */
	public static boolean saveHotelBookingDeatils(HotelBookingDetails bookingDetails) {
		boolean isSucess= false;
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
				collection.insertOne(Document.parse(bookingDetails.toJson()));
				isSucess = true;
			
		}
		return isSucess;
	}
	
	/**
	 * Save data for a user . No check is performed
	 * @param emailid
	 * @return UserDetails if user exists , null other wise
	 */
	public static UserDetails saveUserDetails(UserDetails usrDetails) {
		UserDetails existingRecord = null;
		MongoCollection<Document> collection = getCollection();
		if (collection != null) {
			UserDetails srchObject = new UserDetails();
			srchObject.setEmail(usrDetails.getEmail());
			for (Document doc : collection.find(srchObject.buildFilter())) {
				existingRecord = new UserDetails();
				existingRecord.buildInstance(doc);
				break;
			}
			if(existingRecord==null){
				collection.insertOne(Document.parse(usrDetails.toJson()));
				return usrDetails;
			}
		}
		return null;
	}

	/**
	 * Retrieve the collection as mentioned in the _RECORD_COLLECTION_NAME name
	 * @return MongoCollection<Document> 
	 */
	private static MongoCollection<Document> getCollection() {
		boolean isInitialized = MongoHelper.isInitialized();
		if (!isInitialized) {
			isInitialized = MongoHelper.init(PropertyManager.getProperties(MongoHelper.MONGO_PROPERTY_BUNCH));
		}
		_LOGGER.log(Level.WARNING, "Mongo helper initialiation status " + isInitialized);
		if (isInitialized) {
			MongoCollection<Document> collection = MongoHelper.getCollection(_RECORD_COLLECTION_NAME);
			return collection;

		}
		return null;
	}
}
