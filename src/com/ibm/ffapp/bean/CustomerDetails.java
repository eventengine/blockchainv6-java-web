package com.ibm.ffapp.bean;

public class CustomerDetails {

	private String ffid;
	private String emailid;
	private String userid;
	private String dob;
	private String availablePoints;
	private String password;
	
	public CustomerDetails()
	{
		super();
	}

	/**
	 * @return the ffid
	 */
	public String getFfid() {
		return ffid;
	}

	/**
	 * @param ffid the ffid to set
	 */
	public void setFfid(String ffid) {
		this.ffid = ffid;
	}

	/**
	 * @return the emailid
	 */
	public String getEmailid() {
		return emailid;
	}

	/**
	 * @param emailid the emailid to set
	 */
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	/**
	 * @return the availablePoints
	 */
	public String getAvailablePoints() {
		return availablePoints;
	}

	/**
	 * @param availablePoints the availablePoints to set
	 */
	public void setAvailablePoints(String availablePoints) {
		this.availablePoints = availablePoints;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}
