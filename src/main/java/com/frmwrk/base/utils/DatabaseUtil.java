/**
 * 
 */
package com.frmwrk.base.utils;

/**
 * @author Mayur Patil
 * This is the utility class for all Database related functionalities
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.frmwrk.base.exceptions.*;
import com.frmwrk.base.utils.TestLogger;

import org.slf4j.Logger;

public class DatabaseUtil {
	static Logger tLog = TestLogger.createLogger();
	private Connection connection = null;
	private ResultSet resultSet = null;
	private PreparedStatement preparedStatement = null;
	private String userName = "";
	private String password = "";
	private String driverClassName = "";
	private String dbType = "";
	private String dbServerIP = "";
	
	public DatabaseUtil() {
		super();

	}

	/**
	 * @param userName
	 *            -Database username
	 * @param password
	 *            -Database password
	 * @param dbName
	 *            - Name of database
	 * @param dbType
	 *            - Database type like MySQL, Oracle
	 */
	public DatabaseUtil(String dbType, String driverClassName, String userName,
			String password, String dbIP) {
		this.userName = userName;
		this.password = password;
		this.driverClassName = driverClassName;
		this.dbType = dbType;
		this.dbServerIP=dbIP;
	}


	/**
	 * This method closes the Database connection
	 * 
	 * @throws SQLException
	 */
	public void closeDatabaseConnectivity() throws SQLException {

		if(!connection.isClosed()) {
			connection.close();
		}
		tLog.info(dbType + " database connection is closed");
	}

	/**
	 * This method creates a connection with database
	 * 
	 * @return Connection- connection to database
	 * @throws DatabaseConnectivityException
	 */
	public Connection getConnection() throws DatabaseConnectivityException {
		try {
			if(dbServerIP.equalsIgnoreCase("")){
				tLog.error("No database specified.");
				throw new DatabaseConnectivityException("No database specified.");
			}
			else{
				Class.forName(driverClassName);
				connection = DriverManager.getConnection(dbServerIP,userName, password);
				}
		} catch (ClassNotFoundException e) {
			tLog.error("Unable to find " + dbType + " class driver");
			throw new DatabaseConnectivityException("Unable to find " + dbType
					+ " class driver");
		} catch (SQLException e) {
			tLog.error("Connection to " + dbType
					+ " database failed due to SQLException");
			throw new DatabaseConnectivityException("Connection to " + dbType
					+ " database failed due to SQLException");
		}

		if (connection.equals(null)) {
			tLog.error("Unable to establish " + dbType
					+ " database connection in " + dbServerIP + " using username: "
					+ userName + " and password:  " + password);
			throw new DatabaseConnectivityException("Unable to establish "
					+ dbType + " database connection in " + dbServerIP
					+ " using username: " + userName + " and password:  "
					+ password);
		} else {
			tLog.info("Connected to " + dbType + " database" + " : " + dbServerIP);
		}

		return connection;
	}

	/**
	 * <pre>
	 * This method will retrieve data from database table specified in the sql query and return the data
	 * as a String array. It first stores the data retrieved in a list and then uses the column count to
	 * dynamically decide the return String array size and copies the data from the list into the array.
	 * </pre>
	 * @param tableName
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	//public Object[][] getDataFromDatabase(String tableName, String query)
	public String[][] getDataFromDatabase(String tableName, String query)
			throws SQLException {
		int index = 0;
		int columnCount = 0;
		ResultSetMetaData rsmd;
		String dbData[][];
		try{
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			rsmd = resultSet.getMetaData();
			columnCount = rsmd.getColumnCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			tLog.error("Unable to retreive data from "+tableName+"\n Possible reasons are invalid sql query");
			throw new DataSheetException("Unable to retreive data from "+tableName+"\n Possible reasons are invalid sql query");
		}
		if (resultSet.getFetchSize() == 0 ) {
			tLog.error("No test data in database matching the sqlquery "
					+ query + " in " + tableName);
			throw new SQLException(
					"No test data in database matching the sqlquery: "
							+ query + " in " + tableName);
		} else {
			System.out.println(resultSet.getFetchSize());
			List<String> colList = new ArrayList<String>();
			try{
				while (resultSet.next()) {
					for (int i = 1; i <= columnCount; i++) {
						String colName = rsmd.getColumnName(i);
						System.out.println(colName);
						colList.add(resultSet.getString(colName));
					}	
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbData = new String[colList.size()/columnCount][columnCount];
			for (int j=0;j<=colList.size() - 1;j++) {
				for (int i = 0; i <= columnCount - 1; i++) {
					dbData[index][i] = colList.get(i);
					j++;
				}
				index ++;
			}
		}
		resultSet.close();
		return dbData;
	}
}
