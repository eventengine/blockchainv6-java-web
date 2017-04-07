package com.ibm.ffapp.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.utils.CommonUtil;

public class ServiceResponse {
	private static final SimpleDateFormat _TS_FMT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
	public static final int STATUS_OK=0;
	public static final int STATUS_INVALID_INPUT=1;
	public static final int STATUS_ERROR=2;
	public static final int STATUS_EXCEPTION=3;

	private int status;
	private String statusText;
	private String serverTs;
	private Object payload;
	private String errorLog;
	
	public ServiceResponse(int status, String statusText) {
		super();
		this.status = status;
		this.statusText = statusText;
		this.serverTs = _TS_FMT.format(new Date());
		
	}
	public ServiceResponse(int status, String statusText,Object payload) {
		super();
		this.status = status;
		this.statusText = statusText;
		this.payload = payload;
		this.serverTs = _TS_FMT.format(new Date());
		
	}
	
	public String toJson()
	{
		return CommonUtil.toJson(this);
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the statusText
	 */
	public String getStatusText() {
		return statusText;
	}
	/**
	 * @param statusText the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	/**
	 * @return the serverTs
	 */
	public String getServerTs() {
		return serverTs;
	}
	/**
	 * @param serverTs the serverTs to set
	 */
	public void setServerTs(String serverTs) {
		this.serverTs = serverTs;
	}
	/**
	 * @return the payload
	 */
	public Object getPayload() {
		return payload;
	}
	/**
	 * @param payload the payload to set
	 */
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	/**
	 * @return the errorLog
	 */
	public String getErrorLog() {
		return errorLog;
	}
	/**
	 * @param errorLog the errorLog to set
	 */
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	
}
