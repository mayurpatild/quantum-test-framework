package com.frmwrk.base.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.frmwrk.base.exceptions.DataSheetException;
import com.frmwrk.base.exceptions.InvalidBrowserException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import com.frmwrk.base.utils.TestLogger;
import org.slf4j.Logger;

public class DataSheet {

	static Logger tLog = TestLogger.createLogger();
	/**
	 * @param destFile
	 *            -This argument is for passing the location of the input data
	 *            sheet
	 * @param sheetname
	 *            -This argument is for passing the sheet name in the input data
	 *            sheet
	 * @return-This method checks for the headings in the sheet and returns true
	 *              if the headings are present
	 * @throws BiffException
	 * @throws IOException
	 */
	public boolean validateHeading(String destFile, String sheetname)
			throws BiffException, IOException {
		FileInputStream input = new FileInputStream(destFile);
		Workbook wb = Workbook.getWorkbook(input);

		Sheet sheet = wb.getSheet(sheetname);

		if ((sheet.getCell(0, 0).getContents().equalsIgnoreCase("BROWSER"))
				&& (sheet.getCell(1, 0).getContents()
						.equalsIgnoreCase("EXECUTION STATUS"))) {
			return true;

		}
		input.close();
		return false;
	
	}


	/**
	 * @param destFile
	 *            -This argument is for passing the location of the input data
	 *            sheet
	 * @param sheetname
	 *            -This argument is for passing the sheet name in the input data
	 *            sheet
	 * @return-This method returns column count in the data sheet
	 * @throws BiffException
	 * @throws IOException
	 * @throws DataSheetException
	 * @throws FileNotFoundException
	 */
	public int getColumnCount(String destFile, String sheetname)
			throws BiffException, IOException, DataSheetException,
			FileNotFoundException

			{
		int column = 0;
		try {

			FileInputStream input = new FileInputStream(destFile);

			Workbook wb = Workbook.getWorkbook(input);

			Sheet sheet = wb.getSheet(sheetname);

			column = sheet.getColumns();


			if (column != 0) {
				return column;
			} else {
				//logger.error("The input data sheet is blank");
				throw new DataSheetException("The input data sheet is blank");


			}
		
		} catch (FileNotFoundException fe) {
			//logger.error("Please provide a valid sheet path:" + destFile + " "
			//		+ "can not be found");
			throw new DataSheetException("Please provide a valid sheet path:" + destFile + " "
					+ "can not be found");
		} 
		catch(NullPointerException e){
			//logger.error("No sheet found with the class name "+sheetname);
			throw new DataSheetException("No sheet found with the class name "+sheetname);
		}

			}

	/**
	 * @param destFile
	 *            -This argument is for passing the location of the input data
	 *            sheet
	 * @param sheetname
	 *            -This argument is for passing the sheet name in the input data
	 *            sheet
	 * @return-This method returns the row count in the sheet
	 * @throws BiffException
	 * @throws IOException
	 */
	public int getRowCount(String destFile, String sheetname)
			throws BiffException, IOException {
		FileInputStream input = new FileInputStream(destFile);
		Workbook wb = Workbook.getWorkbook(input);

		Sheet sheet = wb.getSheet(sheetname);
		int row = sheet.getRows();
		input.close();
		return row;

	}

	/**
	 * @param destFile
	 *            -This argument is for passing the location of the input data
	 *            sheet
	 * @param sheetname
	 *            -This argument is for passing the sheet name in the input data
	 *            sheet
	 * @return -This method returns the valid rows considering execution status
	 * @throws BiffException
	 * @throws IOException
	 * @throws DataSheetException
	 */

	public int getValidRows(String destFile, String sheetname)
			throws BiffException, IOException, DataSheetException {
		FileInputStream input = new FileInputStream(destFile);
		Workbook wb = Workbook.getWorkbook(input);
		Sheet sheet = wb.getSheet(sheetname);
		//int columns = getColumnCount(destFile, sheetname);
		int rows = getRowCount(destFile, sheetname);
		int count = 0;
		for (int row = 1; row < rows; row++) {
			if (sheet.getCell(0, row).getContents().equalsIgnoreCase("Y")) {
				count++;
			}
		}
        input.close();
		return count;
	}

	/**
	 * @param destFile
	 *            -This argument is for passing the location of the input data
	 *            sheet
	 * @param sheetname
	 *            -This argument is for passing the sheet name in the input data
	 *            sheet
	 * @return -Returns data object array which reads rows from the input data
	 *         sheet
	 * @throws BiffException
	 * @throws InvalidBrowserException
	 * @throws IOException
	 * @throws DataSheetException
	 */
	public String[][] readFromDriverSheet(String destFile, String sheetname)
			throws BiffException, IOException, 
			DataSheetException {
		//System.out.println(destFile);
		//System.out.println(sheetname);
		String[][] dataObjectArray=null;
		int columns = getColumnCount(destFile, sheetname);
		int rows = getRowCount(destFile, sheetname);
		@SuppressWarnings("unused")
		int ActualRowCount = 0;
		int dataObjectArraySize = getValidRows(destFile, sheetname);
		dataObjectArray = new String[dataObjectArraySize][columns - 1];

		int index = 0;
		boolean headingStatus = true;
		FileInputStream input = new FileInputStream(destFile);
		Workbook wb = Workbook.getWorkbook(input);
		Sheet sheet = wb.getSheet(sheetname);
	
		// Validating for the heading status 
			if (headingStatus) {
				for (int row = 1; row < rows; row++) {
					String executionStatus=sheet.getCell(0, row).getContents();
					 //Checking if execution status is 'Y' 
					if (executionStatus.trim().equalsIgnoreCase("Y")) {
						ActualRowCount++;
						for (int col = 1; col < columns; col++) {
							dataObjectArray[index][col - 1] = sheet.getCell(col,row).getContents();
						}
						index++;
					}
				}
			} else {
				//logger.error("The sheet headings are invalid:The headings should be Browser and Execution Status");
				throw new DataSheetException(
					"The sheet headings are invalid:The headings should be Browser and Execution Status");
			}
		 
		input.close();
		return dataObjectArray;
	}


	/**
	 * @param destFile
	 *            -This argument is for passing the location of the input data
	 *            sheet
	 * @param sheetname
	 *            -This argument is for passing the sheet name in the input data
	 *            sheet
	 * @param ScenarioName
	 *            -This argument is for passing the scenario name along with the iteration number in the input data
	 *            sheet
	 * @param Parm
	 *            -This argument is for passing the parameter sequence number in the input data
	 *            sheet
	 * @return -Returns the parameter which reads rows from the input data
	 *         sheet
	 * @throws BiffException
	 * @throws InvalidBrowserException
	 * @throws IOException
	 * @throws DataSheetException
	 */
	public static String readTestDataSheet(String destFile, String sheetname, String ScenarioName, String Parm)
			throws BiffException, IOException, 
			DataSheetException {
		FileInputStream input = new FileInputStream(destFile);
		Workbook wb = Workbook.getWorkbook(input);
		Sheet sheet = wb.getSheet(sheetname);
		int row = sheet.findCell(ScenarioName).getRow();
		String[] ParmArray = Parm.split("-");
		int ParmCol = Integer.parseInt(ParmArray[1]);
		String ParmValue = sheet.getCell(ParmCol+1,row).getContents(); 
		input.close();
		return ParmValue;
	}


}