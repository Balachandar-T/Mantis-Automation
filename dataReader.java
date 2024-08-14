package first;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class dataReader {

	public static FileInputStream fs;
	public static HSSFWorkbook wb;
	public static HSSFSheet sheet;
	public static HSSFRow row;
	public static HSSFCell keyCell;

	public static String getData(String Path, String Apptype, String sheetname, int indextNo) throws IOException {

		fs = new FileInputStream(Path);
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheet(sheetname);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			keyCell = row.getCell((short) 0);
			HSSFCell valueCell = row.getCell((short) indextNo);
			if (keyCell.toString().trim().equalsIgnoreCase(Apptype)) {
				if (valueCell.toString().isEmpty() || valueCell.toString() == null) {
					return valueCell.toString().trim();
				} 
				else {
					return valueCell.toString().trim();
				}
			}
		}
		return "";
	}

	public static int getColumnCount(String Path, String sheetname) throws IOException {

		FileInputStream fs = new FileInputStream(Path);
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheet(sheetname);
		int lastCellNum = sheet.getRow(0).getLastCellNum();
		return lastCellNum;

	}

}
