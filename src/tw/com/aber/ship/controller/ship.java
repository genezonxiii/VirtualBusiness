package tw.com.aber.ship.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.sale.controller.sale;
import tw.com.aber.util.Util;
import tw.com.aber.vo.ShipVO;

public class ship extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(sale.class);

	private Util util = new Util();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String groupId = request.getSession().getAttribute("group_id").toString();
		String userId = request.getSession().getAttribute("user_id").toString();

		String action = request.getParameter("action");
		logger.debug("Action:".concat(action));
		try {
			if ("search".equals(action)) {

			}
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

	class ShipService {
		private ship_interface dao;

		public ShipService() {
			dao = new ShipDAO();
		}
	}

	class ShipDAO implements ship_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_select_ship_by_sale_date = "call sp_select_ship_by_sale_date (?,?,?)";

		@Override
		public List<ShipVO> searchDB(ShipVO shipVO) {
			return null;
		}
	}

}

interface ship_interface {

	public List<ShipVO> searchDB(ShipVO shipVO);

}
