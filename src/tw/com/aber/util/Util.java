package tw.com.aber.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Ian
 *
 */
public class Util {

	/**
	 * @param object The object to be processed
	 * @return Processed string
	 */
	public String null2str(Object object) {
		return object == null ? "" : object.toString().trim();
	}

	/**
	 * @param inputStr The string to be processed
	 * @param formatStr Prescribed format
	 * @return The date of the formatting
	 */
	public java.sql.Date dateFormat(String inputStr, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		java.sql.Date sDate = null;
		try {
			java.util.Date uDate = sdf.parse(inputStr);
			sDate = new java.sql.Date(uDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sDate;
	}
}
