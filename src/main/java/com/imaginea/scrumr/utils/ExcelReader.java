package com.imaginea.scrumr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pradeep sekhar Utility class for reading LPIDS from XLS/CSV Files
 */
public class ExcelReader {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    private Workbook workBook;

    private Sheet sheet;

    private File xlsFile;

    private HashMap<Integer, Object> row;

    /**
     * @param xlsFile
     *            to be read Constructor for setting the XLS Input File
     */
    public ExcelReader(File xlsFile) throws ScrumrException {
        setXLSFile(xlsFile);
        createWorkBookFromFile(xlsFile.toURI().getPath());
    }

    /**
     * @param inStream
     *            to be read Constructor for setting the XLS Input File
     */
    public ExcelReader(InputStream inStream) throws ScrumrException {
        createWorkBookFromFile(inStream);
    }

    /**
     * @param xlsFile
     *            Sets the XLS Input file
     */
    private void setXLSFile(File xlsFile) {
        this.xlsFile = xlsFile;
    }

    /**
     * @return the XLS Input File
     */
    public File getXLSFile() {
        return xlsFile;
    }

    public Sheet getSheet() {
        // returning the first sheet, since the import file can should one sheet
        return workBook.getSheetAt(0);
    }

    /**
     * 
     * @return HashMap of all values in a specific row
     */
    public HashMap<Integer, Object> getDataRow(int rowNumber) {
        row = new HashMap<Integer, Object>();
        Row hssfrow = sheet.getRow(rowNumber);
        if (hssfrow != null) {
            Iterator<Cell> hssfcells = hssfrow.cellIterator();
            while (hssfcells.hasNext()) {
                Cell hssfcell = hssfcells.next();
                int cellType = hssfcell.getCellType();
                switch (cellType) {
                case Cell.CELL_TYPE_BOOLEAN:
                    row.put(new Integer(hssfcell.getColumnIndex()), hssfcell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    // CellStyle style = hssfcell.getCellStyle();
                    // We pick the dates only if formatted as date properly in excel
                    if (DateUtil.isCellDateFormatted(hssfcell)) {
                        row.put(new Integer(hssfcell.getColumnIndex()), hssfcell.getDateCellValue());
                    } else {
                        row.put(new Integer(hssfcell.getColumnIndex()), hssfcell.getNumericCellValue());
                    }

                    break;
                case Cell.CELL_TYPE_STRING:
                    row.put(new Integer(hssfcell.getColumnIndex()), hssfcell.getRichStringCellValue().getString());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    // if cell left blank, sending empty string
                    row.put(new Integer(hssfcell.getColumnIndex()), "");
                    break;
                default:
                    row.put(new Integer(hssfcell.getColumnIndex()), hssfcell.getErrorCellValue());
                }

            }
        }
        return row;
    }

    /**
     * 
     * @return the number of rows in the sheet
     */
    public int getRowCount() {
        return getSheet().getPhysicalNumberOfRows();
    }

    /**
     * Creates HSSFWorkbook from a spreadsheet File
     * 
     * @param filePath
     *            of the file from which HSSFWorkbook is created
     * @throws ScrumrException
     * @throws InvalidFormatException
     */
    public void createWorkBookFromFile(String filePath) throws ScrumrException {
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            workBook = WorkbookFactory.create(inputStream);
            this.sheet = workBook.getSheetAt(0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw ScrumrException.create("Import File Upload failed " + e.getMessage(), MessageLevel.SEVERE, null);
        } catch (FileNotFoundException e) {
            throw ScrumrException.create("Import File Upload failed ", MessageLevel.SEVERE, e);
        } catch (IOException e) {
            e.printStackTrace();
            throw ScrumrException.create("Import File Upload failed " + e.getMessage(), MessageLevel.SEVERE, null);
        }

    }

    /**
     * Creates HSSFWorkbook from a spreadsheet File
     * 
     * @param inStream
     *            from which HSSFWorkbook is created
     */
    public void createWorkBookFromFile(InputStream inStream) throws ScrumrException {
        try {
            workBook = WorkbookFactory.create(inStream);
            this.sheet = workBook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            throw ScrumrException.create("Import File Upload failed ", MessageLevel.SEVERE, e);
        } catch (IOException e) {
            e.printStackTrace();
            throw ScrumrException.create("Import File Upload failed " + e.getMessage(), MessageLevel.SEVERE, null);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw ScrumrException.create("Import File Upload failed " + e.getMessage(), MessageLevel.SEVERE, null);
        }

    }

    // /**
    // * Validate the field that specifies number of data rows in the cell
    // * @param firstRow of the Spreadsheet
    // * @throws ScrumrException
    // */
    // public boolean validateNumberRowsCell(HSSFRow firstRow) throws ScrumrException
    // {
    // boolean numberOfRowsError = false;
    // HSSFCell numberOfRowsCell = firstRow.getCell((short) 3);
    //
    // if (numberOfRowsCell.equals(HSSFCell.CELL_TYPE_BLANK))
    // {
    // throw ScrumrException.create("The number of rows cell cannot be left blank",
    // MessageLevel.SEVERE);
    // }
    // if(numberOfRowsCell.getNumericCellValue() > getRowCount(sheet))
    // {
    // numberOfRowsError = true;
    // }
    // else
    // {
    // numberOfRowsError = false;
    // }
    // return numberOfRowsError;
    // }
    //
    // public boolean validateInputKeyCell(HSSFRow firstRow) throws ScrumrException
    // {
    // boolean inputKeyError = false;
    // HSSFCell inputKeyCell = firstRow.getCell((short) 2);
    //
    // if (inputKeyCell.equals(HSSFCell.CELL_TYPE_BLANK))
    // {
    // inputKeyError = true;
    // throw
    // ScrumrException.create("The input key cell cannot be left blank. If it is a new entry, enter -1.",
    // MessageLevel.SEVERE);
    // }
    //
    // int inputKey = (int) inputKeyCell.getNumericCellValue();
    // return inputKeyError;
    // }

}