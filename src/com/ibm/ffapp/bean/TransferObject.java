package com.ibm.ffapp.bean;

import org.bson.Document;

import com.ibm.utils.MongoSerializable;

/**
 * Generic transfer object between layers and view. Should not be persisted.
 * 
 * @author SUDDUTT1
 *
 */
public class TransferObject extends MongoSerializable {

	/**
	 * Keeps the complier happy
	 */
	private static final long serialVersionUID = 3935191776004596127L;

	@Override
	public void buildInstance(Document doc) {
		// TODO Auto-generated method stub
		setInternalFields(doc);
	}

}
