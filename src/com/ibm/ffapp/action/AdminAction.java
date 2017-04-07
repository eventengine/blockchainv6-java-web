package com.ibm.ffapp.action;

import static com.ibm.utils.CommonUtil.serializeThowable;
import static com.ibm.utils.CommonUtil.toJson;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.app.web.frmwk.WebActionHandler;
import com.ibm.app.web.frmwk.annotations.RequestMapping;
import com.ibm.app.web.frmwk.bean.ModelAndView;
import com.ibm.app.web.frmwk.bean.ViewType;
import com.ibm.ffapp.bean.ServiceResponse;
import com.ibm.ffapp.dao.LoyaltyDataDAO;
import com.ibm.ffapp.hldao.LoyaltyDataHLDAO;
import com.ibm.hyperledger.client.HyperLedgerResponse;

public class AdminAction implements WebActionHandler {
	private static final Logger _LOGGER = Logger.getLogger(AdminAction.class.getName());

	@RequestMapping("getAllUsersAdmin.wss")
	public ModelAndView getAllUsersAdmin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "List of users",
					LoyaltyDataDAO.getAllUsers(""));

		} catch (Throwable ex) {
			_LOGGER.log(Level.WARNING, "Failed in login.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION, "Exception thrown");
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

	@RequestMapping("addLoyaltityPointsAdmin.wss")
	public ModelAndView addLoyaltityPointsAdmin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {
			String ffid = request.getParameter("ffid");
			String points = request.getParameter("points");
			String trxnId = "ADMIN_" + System.currentTimeMillis();
			String businessEntity = request.getParameter("businessEntity");
			HyperLedgerResponse hlResponse = LoyaltyDataHLDAO.addLoyaltyPoint(ffid, points, trxnId, "Added by admin",
					businessEntity);
			if (hlResponse.isOk()) {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "Addition successful ", trxnId);
			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_ERROR,
						"Points addition failed " + hlResponse.getMessage(), null);
			}
		} catch (Throwable ex) {
			_LOGGER.log(Level.WARNING, "Failed in addLoyaltityPointsAdmin.wss ", ex);
			serviceResponse = new ServiceResponse(ServiceResponse.STATUS_EXCEPTION, "Exception thrown");
			serviceResponse.setErrorLog(serializeThowable(ex));
		} finally {
			mvObj.setView(toJson(serviceResponse));
		}
		return mvObj;
	}

}
