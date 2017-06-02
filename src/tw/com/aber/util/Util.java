package tw.com.aber.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.stock.controller.StockNew;
import tw.com.aber.vo.UserVO;

/**
 * @author Ian
 *
 */
public class Util {
	private static final Logger logger = LogManager.getLogger(Util.class);
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
	
	/**
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse

	 */
	public void ConfirmLoginAgain(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request.getSession().getAttribute("group_id") == null
					|| request.getSession().getAttribute("user_id") == null) {
				
				logger.debug("group_id,user_id is null from session");
				// 導到登入畫面
				response.sendRedirect("./login.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return  ValueService
	 */
	public ValueService getValueService(HttpServletRequest request, HttpServletResponse response) {
		ValueService valueService = (ValueService) request.getSession().getAttribute("valueService");
		try {
			if (valueService == null) {
				ConfirmLoginAgain(request, response);
				
				logger.debug("valueService Is null from session");
				logger.debug("create new valueService");
				
				UserVO userVO = new UserVO();
				userVO.setGroup_id(request.getSession().getAttribute("group_id").toString());
				userVO.setUser_id(request.getSession().getAttribute("user_id").toString());
				valueService = new ValueService(request.getSession().getServletContext(), userVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueService;
	}
}
