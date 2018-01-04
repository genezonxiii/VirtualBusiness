package tw.com.aber.pick.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.service.PickService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.PickDetailVO;
import tw.com.aber.vo.PickVO;

public class Pick extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Pick.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Util util =new Util();
		util.ConfirmLoginAgain(request, response);

		String groupId = (String)request.getSession().getAttribute("group_id");
		String action = request.getParameter("action");
		String result = null;
		Gson gson = null;
		PickService pickService = new PickService();
		
		logger.debug("Action:".concat(action));
		try {
			if ("searchPickByPickTimeDate".equals(action)) {
				List<PickVO> pickVOList = new ArrayList<PickVO>();
				String startStr = request.getParameter("startDate");
				String endStr = request.getParameter("endDate");
				
				logger.debug("startDate:".concat(startStr));
				logger.debug("endDate:".concat(endStr));

				java.util.Date date = null;
				Date startDate = null, endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf.parse(startStr);
					startDate = new java.sql.Date(date.getTime());
					date = sdf.parse(endStr);
					endDate = new java.sql.Date(date.getTime());
				} catch (ParseException e) {
					logger.error("search date convert :".concat(e.getMessage()));
				}
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				pickVOList = pickService.searchPickByPickTimeDate(groupId, startDate, endDate);
				result = gson.toJson(pickVOList);
				
				logger.debug("result:" +result);
				
				response.getWriter().write(result);
			} else if("searchPickByOrderNo".equals(action)){

				List<PickVO> pickVOList = new ArrayList<PickVO>();
				String orderNo = request.getParameter("orderNo");

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				pickVOList = pickService.searchPickByOrderNo(groupId, orderNo);
				result = gson.toJson(pickVOList);
				
				logger.debug("result:" +result);
				
				response.getWriter().write(result);
			} else if("getDetail".equals(action)){
				List<PickDetailVO> pickDetailVOList = new ArrayList<PickDetailVO>();
				String pick_id = request.getParameter("pick_id");
				
				logger.debug("pick_id:" + pick_id);
				
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				pickDetailVOList = pickService.searchPickDetailByPickId(groupId, pick_id);
				result = gson.toJson(pickDetailVOList);
				
				logger.debug("result:" +result);
				
				response.getWriter().write(result);
			}
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}	
	}
	
}
