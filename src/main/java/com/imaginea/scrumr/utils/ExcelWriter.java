package com.imaginea.scrumr.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;


public class ExcelWriter
{
	private int currentRowNumber = 0;
	private HSSFSheet sheet;
	private HSSFWorkbook wb;
	private HSSFRow newRow;
	private OutputStream out = null;

	/**
	 * @return the output stream
	 */
	public OutputStream getOut() {
		return out;
	}

	public ExcelWriter()
	{
     out = new ByteArrayOutputStream();
	}
	/**
	 *
	 * @param filePath
	 */
	public ExcelWriter(String filePath)
	{
		try
		{
			File resultFile = new File(filePath);
			out = new FileOutputStream(resultFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param file
	 */
	public ExcelWriter(File file)
	{
		try
		{
			file.createNewFile();
			out = new FileOutputStream(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Generates Excel Files from the Database
	 *
	 * @param dataRows which is the entity for excel data
	 * @throws ScrumrException
	 */
	public HSSFWorkbook generateExcelFiles(List<HashMap<Integer,Object>> dataRows) throws ScrumrException
	{


		if (dataRows != null && dataRows.size() > 0) {

			wb = new HSSFWorkbook();
			sheet = wb.createSheet("Datasheet");
			for (currentRowNumber = 0; currentRowNumber < dataRows.size(); currentRowNumber++)
			{
				newRow = sheet.createRow((short) currentRowNumber );
				createCells(dataRows.get(currentRowNumber), newRow);
			}
			try
			{
				wb.write(out);
				out.flush();
				out.close();
				return wb;
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
		
	}

	/**
	 * This method updates the header information in the error file generated with the total number of error rows in the uploaded file
	 * @param wBook
	 * @param headerRow
	 * @throws ScrumrException
	 */
	public void updateUploadFileHeader(HSSFWorkbook wBook, HashMap<Integer,Object> headerRow)  throws ScrumrException{
		if (headerRow != null && wBook != null) {
			sheet = wBook.getSheetAt(0); //assuming the error file is generated by the same program which creates the header with info as the first row
			newRow = sheet.getRow(0); //get the generated header row
			Integer numOfRows = sheet.getPhysicalNumberOfRows() - 2 ; // discard the header row and the attributes name row to get the number of data rows
			Cell totalRowsCell = newRow.createCell(new Integer(3)); //create a cell to store the total number of rows in the file
			totalRowsCell.setCellValue(numOfRows);
			try
			{
				wBook.write(out);
				out.flush();
				out.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	/**
	 * Creates cells in the Workbook corresponding to data from the entity
	 * @param newRow - a new row in the workbook
	 * @throws ScrumrException
	 */
	private void createCells(HashMap<Integer,Object> row, HSSFRow newRow) throws ScrumrException
	{
		for (int cellNumber = 0; cellNumber < row.size(); cellNumber++)
		{
			Object cellValue = row.get(cellNumber);
            if(cellValue!=null){
                if (cellValue instanceof String)
                {
                    newRow.createCell((short) (cellNumber)).setCellValue(cellValue.toString());
                } else if (cellValue instanceof Boolean)
                {
                    newRow.createCell((short) (cellNumber)).setCellValue((Boolean) cellValue);
                }else if (cellValue instanceof Date)
                {
                	SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
     			   	String strDateFromFile = fmt.format((Date)cellValue);
                    try {
                    	Date dt = fmt.parse(strDateFromFile);
                    	HSSFCellStyle cellStyle = wb.createCellStyle();
                    	CreationHelper createHelper = wb.getCreationHelper();
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
                        
                    	Cell dateCell = newRow.createCell((cellNumber));
                    	dateCell.setCellValue(dt); //set the date value
                    	dateCell.setCellStyle(cellStyle); //set the date format
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else if (cellValue instanceof Number)
                {
                    newRow.createCell((short) (cellNumber)).setCellValue(((Number) cellValue).doubleValue());
                } 
                else
                {
                    throw ScrumrException.create("Incompatible data type in row number " + currentRowNumber + "cell number " + cellNumber, MessageLevel.SEVERE);
                }
            }
		}
	}

	/**
	 * Prepares the HSSFWorkbook from the data
	 * @param wb - The HSSFWorkbook
	 * @param sheet - The HSSFSheet
	 */
	private void prepareWorkBookFromData(HSSFWorkbook wb, HSSFSheet sheet)
	{
		HSSFFont font = wb.createFont();
		font.setColor(HSSFFont.BOLDWEIGHT_BOLD);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		// createHeaderCellsFormatting(row, cellStyle);
	}

}
