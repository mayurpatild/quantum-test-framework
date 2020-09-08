package com.frmwrk.base.exceptions;

public class DatabaseConnectivityException extends Exception{

	/**
	 * This method provides customized database connectivity exception messages
	 */		
	public DatabaseConnectivityException(String message)
	{
		super(message);
	}
}
