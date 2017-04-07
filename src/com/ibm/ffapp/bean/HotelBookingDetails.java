package com.ibm.ffapp.bean;

import org.bson.Document;

import com.ibm.utils.MongoSerializable;

/**
 * Serialization object to save hotel booking details.
 * 
 * @author SUDDUTT1
 *
 */
public class HotelBookingDetails extends MongoSerializable {

	/**
	 * Keeps the complier happy
	 */

	private static final long serialVersionUID = -8462519390938461431L;
	
	@Override
	public void buildInstance(Document doc) {
		// TODO Auto-generated method stub
		setInternalFields(doc);
	}

}
