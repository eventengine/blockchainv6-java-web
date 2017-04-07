package com.ibm.ffapp.hldao;

public interface ApplicationConstants {
	String APP_PROPS_BUNDLE = "APP_PROPS";
	String APP_USER = "app.user";
	String APP_USER_ROLE = "app.user.role";
	String APP_LOGIN_AUTH_TOKEN = "auth_token";
	String APP_ACTION_RESPONSE="actionResponse";
	int ACTION_SUCESS = 0;
	int ACTION_ERROR = 1;
	int ACTION_EXCEPTION = 2;
	int ACTION_INVALID_INPUT =3;
	
	String BE_AIRLINE="AIRLINE";
	String BE_CARSERVICE="CARSERVICE";
	String BE_HOTEL="HOTEL";
	String BE_RETAIL="RETAIL";
	
	String HL_URL = "hyperledger.server.url";
	String HL_CHAIN_CODE_ID = "hyperledger.chaincode.id";
	String HL_USER_CONTEXT ="hyperledger.user.context";
	String HL_USER_ID ="hyperledger.user.id";
	String HL_USER_SECRET ="hyperledger.user.secret";
}
