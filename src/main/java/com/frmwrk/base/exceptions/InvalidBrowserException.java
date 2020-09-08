package com.frmwrk.base.exceptions;

@SuppressWarnings("serial")
public class InvalidBrowserException extends Exception
{

	/**
	 * This method provides customized browser exception messages
	 */	
	public InvalidBrowserException(String message)
	  {
	    super(message);
	  }

}
