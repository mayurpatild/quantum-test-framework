package com.frmwrk.base.utils;

import java.io.IOException;
//import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.Calendar;

public class TestLogger {

	private static Logger _logger;
	private static final String fileName = "ExecutionLog";
	private static final String logProperttFilePath = "src/main/resources/log4j-h.properties";
	
	  /*static {
		    *//**
		     * This is the static block which appends the log file name with the
		     * timestamp to make it unique
		     *//*
		    try {
		        //String dateTime = sdf.format(cal.getTime()); // For date in log file name uncomment this code
		        //String FileName = fileName + "-" + dateTime + ".log"; // For date in log file name uncomment this code
		        //String FileName = fileName + "-" + dateTime + ".html"; // For date in log file name uncomment this code
		        String FileName = fileName +  ".html";
		        File file = new File("logs/" + FileName);

		        if (file.createNewFile()) {
		            Properties props = new Properties();
		            props.load(new FileInputStream(logProperttFilePath));
		            //props.setProperty("log4j.appender.FA.File", "logs/" + FileName);
		            props.setProperty("log4j.appender.HTML.File", "logs/" + FileName);
		            LogManager.resetConfiguration();
		            PropertyConfigurator.configure(props);
		            //System.out.println("Property log4j.appender.FA.File = logs/" + FileName);
		            System.out.println("Property log4j.appender.HTML.File = logs/" + FileName);
		        } else {
		            Properties props = new Properties();
		            props.load(new FileInputStream(logProperttFilePath));
		            //props.setProperty("log4j.appender.FA.File", "logs/" + FileName);
		            props.setProperty("log4j.appender.HTML.File", "logs/" + FileName);
		            LogManager.resetConfiguration();
		            PropertyConfigurator.configure(props);
		            //System.out.println("Property log4j.appender.FA.File = logs/" + FileName);
		            System.out.println("Property log4j.appender.HTML.File = logs/" + FileName);
		        }
		    } catch (IOException ex) {
		        ex.printStackTrace();
		        System.out.print("IO Exception in static method of Logger Class. "
		                + ex.getMessage());
		        System.exit(-1);
		    }

		}*/
	  
 
		/**
		 * This method creates instance of the Logger class coming from log4j jar by
		 * implementing a singelton
		 * 
		 * @return _logger - new instance if no instance exist else an existing
		 *         instance if the method is invoked previously
		 */
		public static org.slf4j.Logger createLogger() {
		    if (_logger == null) {
		        _logger = LoggerFactory.getLogger(TestLogger.class);
		        _logger.info(logProperttFilePath);
		        return _logger;
		    } else
		        return _logger;
		}

}

