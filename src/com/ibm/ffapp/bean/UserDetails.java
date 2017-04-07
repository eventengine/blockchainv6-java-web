package com.ibm.ffapp.bean;

import org.bson.Document;

import com.ibm.utils.MongoSerializable;

public class UserDetails extends MongoSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3924708896451789273L;

	public UserDetails()
	{
		super();
	}
	public String getPwd(){
		return (String)get("pwd");
	}
	public String getConfpwd(){
		return (String)get("confpwd");
	}
	public String getFfId(){
		return (String)get("ffId");
	}
	public String getTitle(){
		return (String)get("title");
	}
	public String getGender(){
		return (String)get("gender");
	}
	public String getFirstName(){
		return (String)get("firstName");
	}
	public String getLastName(){
		return (String)get("lastName");
	}
	public String getDob(){
		return (String)get("dob");
	}
	public String getEmail(){
		return (String)get("email");
	}
	public String getCountry(){
		return (String)get("country");
	}
	public String getAddress(){
		return (String)get("address");
	}
	public String getCity(){
		return (String)get("city");
	}
	public String getZip(){
		return (String)get("zip");
	}
	public String getCreatedBy(){
		return (String)get("createdBy");
	}
	public String getTotalPoint(){
		return (String)get("totalPoint");
	}
	public void setFfId(String ffId){
		put("ffId",ffId);
	}
	public void setTitle(String title){
		put("title",title);
	}
	public void setGender(String gender){
		put("gender",gender);
	}
	public void setFirstName(String firstName){
		put("firstName",firstName);
	}
	public void setLastName(String lastName){
		put("lastName",lastName);
	}
	public void setDob(String dob){
		put("dob",dob);
	}
	public void setEmail(String email){
		put("email",email);
	}
	public void setCountry(String country){
		put("country",country);
	}
	public void setAddress(String address){
		put("address",address);
	}
	public void setCity(String city){
		put("city",city);
	}
	public void setZip(String zip){
		put("zip",zip);
	}
	public void setCreatedBy(String createdBy){
		put("createdBy",createdBy);
	}
	public void setTotalPoint(String totalPoint){
		put("totalPoint",totalPoint);
	}
	public void setPwd(String pwd){
		put("pwd",pwd);
	}
	public void setConfpwd(String confpwd){
		put("confpwd",confpwd);
	}
	public void  buildInstance(Document doc){
		setInternalFields(doc);
	}

}
