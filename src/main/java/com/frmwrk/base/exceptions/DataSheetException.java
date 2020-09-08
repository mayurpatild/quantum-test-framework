package com.frmwrk.base.exceptions;

@SuppressWarnings("serial")
public class DataSheetException extends RuntimeException{

	/**
	 * This method provides customized datasheet exception messages
	 */		
	public DataSheetException(String message)
	  {
	    super(message);
	  }

}
