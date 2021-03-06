package com.ibm.hyperledger.client;

public class HyperLedgerResponse {
	
	private boolean ok;
	private String chainCode;
	private String message;
	

	public HyperLedgerResponse(boolean ok) {
		super();
		this.ok = ok;
	}
	public HyperLedgerResponse(String okStr) {
		super();
		this.ok = (okStr!=null && okStr.trim().toUpperCase().equals("OK"));
	}
	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}
	/**
	 * @param ok the ok to set
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	/**
	 * @return the chainCode
	 */
	public String getChainCode() {
		return chainCode;
	}
	/**
	 * @param chainCode the chainCode to set
	 */
	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
