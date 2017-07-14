package tw.com.aber.egs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Egs extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Egs.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String action = (String) request.getParameter("action");
		logger.debug("Action:".concat(action));

		if ("query_egs_info".equals(action)) {
			EgsApi egsApi = new EgsApi();
			String result = egsApi.queryEgsInfo(action);
		} else if ("query_suda5".equals(action)) {
			String addresses = request.getParameter("addresses");
			EgsApi egsApi = new EgsApi();
			String result = egsApi.querySuda5(action, addresses);
		}

	}
}
