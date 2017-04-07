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
import com.ibm.ffapp.bean.TransferObject;
import com.ibm.ffapp.hldao.LoyaltyDataHLDAO;
import com.ibm.hyperledger.client.HyperLedgerResponse;
import com.ibm.utils.CommonUtil;

public class EndUserAction implements WebActionHandler {
	private static final Logger _LOGGER = Logger.getLogger(EndUserAction.class.getName());

	/**
	 * This method returns all the transactions of a user. 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView ViewType.AJAX_VIEW
	 */
	@RequestMapping("getAllTransactionUser.wss")
	public ModelAndView getAllUsersAdmin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mvObj = new ModelAndView(ViewType.AJAX_VIEW);
		ServiceResponse serviceResponse = null;
		try {
			String ffid = request.getParameter("ffid");
			HyperLedgerResponse hlResp = LoyaltyDataHLDAO.retrieveAllLoyaltyTrxans(ffid);
			if (hlResp.isOk()) {
				TransferObject[] tObj = CommonUtil.fromJson(hlResp.getMessage(), TransferObject[].class);
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "List of users", tObj);

			} else {
				serviceResponse = new ServiceResponse(ServiceResponse.STATUS_OK, "No Traxns", new Object[1]);
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
}
