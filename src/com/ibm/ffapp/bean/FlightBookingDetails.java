package com.ibm.ffapp.bean;

import org.bson.Document;

import com.ibm.utils.MongoSerializable;

/**
 * Serialization object to save flight booking details.
 * 
 * @author SUDDUTT1
 *
 */
public class FlightBookingDetails extends MongoSerializable {

	
	
	/**
	 * Keeps the complier happy
	 */
	private static final long serialVersionUID = 7640762425535820329L;

	@Override
	public void buildInstance(Document doc) {
		// TODO Auto-generated method stub
		setInternalFields(doc);
	}

}
