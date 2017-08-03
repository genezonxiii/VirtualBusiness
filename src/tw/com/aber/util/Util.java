package tw.com.aber.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	 * @param date
	 *            The string to be processed
	 * @param regular
	 *            Format string
	 * @return boolean value
	 */
	public boolean checkDateFormat(String date, String regular) {
		boolean result = false;
		try {
			Pattern p = Pattern.compile(regular);
			Matcher m = p.matcher(date);
			result = m.matches();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * @param object
	 *            The object to be processed
	 * @return Processed string
	 */
	public String null2str(Object object) {
		return object == null ? "" : object.toString().trim();
	}

	/**
	 * @param inputStr
	 *            The string to be processed
	 * @param formatStr
	 *            Prescribed format
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
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 * 
	 */
	public boolean ConfirmLoginAgain(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String groupId = (String) request.getSession().getAttribute("group_id");
		String userId = (String) request.getSession().getAttribute("user_id");
		boolean result = true;
		if (groupId == null | userId == null) {
			logger.debug("groupId: " + groupId);
			logger.debug("userId: " + userId);
			HttpSession session = request.getSession(true);
			session.setAttribute("sessionID", null);
			session.setAttribute("user_id", null);
			session.setAttribute("group_id", null);
			session.setAttribute("user_name", null);
			session.setAttribute("valueService", null);
			result = false;
		}
		return result;
		// xxx 暫且擱置
		// try {
		// if (request.getSession().getAttriRbute("group_id") == null
		// || request.getSession().getAttribute("user_id") == null) {
		//
		// logger.debug("group_id,user_id is null from session");
		// // 導到登入畫面
		// response.sendRedirect("./login.jsp");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ValueService
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
