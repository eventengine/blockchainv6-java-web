package com.ibm.utils;

import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.app.web.frmwk.WebActionHandler;
import com.ibm.app.web.frmwk.annotations.RequestMapping;
import com.ibm.app.web.frmwk.bean.ModelAndView;
import com.ibm.app.web.frmwk.bean.ViewType;
import com.ibm.ffapp.bean.ServiceResponse;
import com.ibm.ffapp.bean.TransferObject;
import com.ibm.ffapp.bean.UserDetails;
import com.ibm.ffapp.dao.LoyaltyDataDAO;
import com.ibm.ffapp.hldao.ApplicationConstants;
import com.ibm.ffapp.hldao.LoyaltyDataHLDAO;
import com.ibm.hyperledger.client.HyperLedgerResponse;

import static com.ibm.utils.CommonUtil.*;

public class LoginAction implements WebActionHandler {

	private static final Logger _LOGGER = Logger.getLogger(LoginAction.class.getName());

	@RequestMapping("loadLogin.wss")
	public ModelAndView loadLogin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.GENERIC_NO_RENDER_VIEW);
		mvObj.setContentType("text/html");
		mvObj.setView("login.html");
		return mvObj;

	}

	@RequestMapping("login.wss")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {
			String postBody = getPostBodyFromRequest(request);
			UserDetails userProfile = fromJson(postBody, UserDetails.class);
			String role= userProfile.getString("role");
			if("USER".equals(role)){
				String userId = userProfile.getString("userId");
				UserDetails existingUser = LoyaltyDataDAO.getUserDetails(userId);
				String userPwd = CommonUtil.encodePassword(userProfile.getPwd());
				if(existingUser==null || !existingUser.getPwd().equals(userPwd)){
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "Invalid Credential", null);
				}
				else
				{
					existingUser.setPwd("XXXXX");
					existingUser.setConfpwd("XXXXX");
					TransferObject transObject = new TransferObject();
					transObject.buildInstance(existingUser);
					transObject.put("role", role);
					transObject.put("userId", existingUser.getFirstName());
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Sucess", transObject);
					request.getSession().setAttribute("auth_token", System.currentTimeMillis() + "");
					
				}
			}
			else if("password".equals(userProfile.getPwd())) {
				request.getSession().setAttribute("auth_token", System.currentTimeMillis() + "");
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Sucess", userProfile);
			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR, "Invalid Credential", null);
			}
		} catch (Throwable ex) {
			_LOGGER.log(Level.WARNING, "Failed in login.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION, "Exception thrown");
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	@RequestMapping("logout.wss")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		ModelAndView mvObj = new ModelAndView(ViewType.GENERIC_NO_RENDER_VIEW, "text/html");
		mvObj.setView("index.html");
		return mvObj;
	}

	@RequestMapping("registerUser.wss")
	public ModelAndView registerUser(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {
			String postBody = getPostBodyFromRequest(request);
			UserDetails userDetails = fromJson(postBody, UserDetails.class);
			UserDetails user = LoyaltyDataDAO.getUserDetails(userDetails.getEmail());
			if (user != null) {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_INVALID_INPUT, "Existing user", null);
			} else {
				// Generate a FFID
				String ffid = generateFFID(null, null, "LE");
				userDetails.setFfId(ffid);
				userDetails.setPwd(CommonUtil.encodePassword(userDetails.getPwd()));
				userDetails.setConfpwd(CommonUtil.encodePassword(userDetails.getConfpwd()));
				// First save data in HL
				HyperLedgerResponse hlResponse = LoyaltyDataHLDAO.registerUser(userDetails,
						ApplicationConstants.BE_AIRLINE);
				if (hlResponse.isOk()) {
					// Go and save the info in db
					UserDetails savedDetails = LoyaltyDataDAO.saveUserDetails(userDetails);
					if(savedDetails!=null){
						serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK,
								"Registration successful ", savedDetails);
					}
					else
					{
						serviceResponse = new ServiceResponse(ServiceResponse.STATUS_INVALID_INPUT,
								"Unable to save in data store", null);
					}
				} else {
					serviceResponse = new ServiceResponse(ServiceResponse.STATUS_INVALID_INPUT,
							"Unable to create registration in Hlyperledger", null);
				}

			}

		} catch (Throwable ex) {
			_LOGGER.log(Level.WARNING, "Failed in login.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION, "Exception thrown");
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	private String generateFFID(String firstName, String lastName, String seed) {
		return seed + RandomDataGenUtil.generateRandomString("ABCPQXZY01234567890", 4);
	}

	public String getPostBodyFromRequest(HttpServletRequest request) {
		StringBuilder jb = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
			reader.close();
		} catch (Exception e) {
			jb = new StringBuilder("{}");
		}
		return jb.toString();
	}

}
