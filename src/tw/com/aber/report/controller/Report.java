package tw.com.aber.report.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import tw.com.aber.egs.controller.EgsApi;

public class Report extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String sp_get_ship_sf_delivery_new_no = "call sp_delivery_report(?,?,?,?)";
	private static final Logger logger = LogManager.getLogger(Report.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("group_id") == null) {
			return;
		}
		HashMap<String, Object> hm = null;

		String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		final String reportSourcePath = ("" + this.getClass().getResource("/")).substring(5)
				.replace("VirtualBusiness/WEB-INF/classes/", "VirtualBusiness/WEB-INF/");
		final String reportGeneratePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/report";
		new File(reportGeneratePath).mkdir();
		try {
			if (request.getParameter("dis_date") != null) {
				String disDate = request.getParameter("dis_date");
				logger.debug("dis_date:"+disDate);
				final String reportName = "rptPickReport";
				final String reportCName = URLEncoder.encode("揀貨單", "UTF-8");;
				
				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				hm.put("p_dis_date", disDate);
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				writeToClient(request, response, reportCName, pdfFileName);
			} else if (request.getParameter("sale_id") != null) {
				String saleId = request.getParameter("sale_id");
				logger.debug("sale_id:"+saleId);
				String reportName = "rptDistributeReport";
				final String reportCName = URLEncoder.encode("出貨單", "UTF-8");;
				
				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				hm.put("p_sale_id", saleId);
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				writeToClient(request, response, reportCName, pdfFileName);
			} else if (request.getParameter("pick_id") != null) {
				String pickId = request.getParameter("pick_id");
				logger.debug("pick_id:"+pickId);
				String reportName = "rptPick";
				final String reportCName = URLEncoder.encode("揀貨單", "UTF-8");;
				String reportDetailName = "rptPickDetail";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String jrxmlFileDetailName = reportSourcePath + "/" + reportDetailName + ".jrxml";
				String jasperFileDetailName = reportGeneratePath + "/" + reportDetailName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName, jasperFileDetailName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				hm.put("p_pick_id", pickId);
				
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				writeToClient(request, response, reportCName, pdfFileName);
			} else if (request.getParameter("pick_no") != null) {
				String pickNo = request.getParameter("pick_no");
				logger.debug("pick_no:"+pickNo);
				final String reportName = "rptShip";
				final String reportCName = URLEncoder.encode("出貨單", "UTF-8");;
				String reportDetailName = "rptShipDetail";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String jrxmlFileDetailName = reportSourcePath + "/" + reportDetailName + ".jrxml";
				String jasperFileDetailName = reportGeneratePath + "/" + reportDetailName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName, jasperFileDetailName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id",request.getSession().getAttribute("group_id"));
				hm.put("p_pick_no", pickNo);
				
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				writeToClient(request, response, reportCName, pdfFileName);
			}else if (request.getParameter("date") != null) {
				String date = request.getParameter("date");
				logger.debug("date:"+date);
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
					pstmt = con.prepareStatement(
							"select count(sale_id) n from tb_sale where dis_date = ? and group_id = ?");
					pstmt.setString(1, date);
					pstmt.setString(2, request.getSession().getAttribute("group_id").toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						response.getWriter().write(rs.getString("n"));
					}
				} catch (SQLException se) {
					System.out.println("ERROR WITH: " + se);
				} catch (ClassNotFoundException cnfe) {
					throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				}
			} else if ("ship_report".equals(request.getParameter("type"))) {
				logger.debug("type:"+request.getParameter("type"));
				String address = request.getParameter("address");
				String order_no = request.getParameter("order_no");
				String modeltype = request.getParameter("modeltype");
				String reportName ="";
				String reportCName = "";

				EgsApi egsApi = new EgsApi();
				String suda7 = egsApi.getQuerySuda7(address);
				EgsApi api = new EgsApi();
				String eGSNum = api.getEGSNum();
				
				logger.debug("address:"+address);
				logger.debug("suda7:"+suda7);
				logger.debug("eGSNum:"+eGSNum);
				logger.debug("group_id:"+request.getSession().getAttribute("group_id").toString());
				logger.debug("order_no:"+order_no);
				logger.debug("modeltype:"+modeltype);

				if("a4_2".equals(modeltype)){
					reportName = "shipReportA4_2";
					reportCName = URLEncoder.encode("黑貓二模", "UTF-8");
				}else if("a4_3".equals(modeltype)){
					reportName = "shipReportA4_3";
					reportCName = URLEncoder.encode("黑貓模", "UTF-8");
				}else{
					return;
				}

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id").toString());
				hm.put("p_order_no", order_no);
				hm.put("p_suda7", suda7);
				hm.put("p_egs_num", eGSNum);
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
				
				writeToClient(request, response, reportCName, pdfFileName);
			} else if ("reportEzcat".equals(request.getParameter("type"))) {
				logger.debug("type:"+request.getParameter("type"));
				String track_list = request.getParameter("track_list");
				String modeltype = request.getParameter("modeltype");
				String reportName ="";
				String reportCName = "";
				
				logger.debug("group_id:"+request.getSession().getAttribute("group_id").toString());
				logger.debug("track_list:"+track_list);
				logger.debug("modeltype:"+modeltype);

				if("ezcat".equals(modeltype)){
					reportName = "shipReport_ezcat";
					reportCName = URLEncoder.encode("黑貓", "UTF-8");
				}else if("ezcat_a4_2".equals(modeltype)){
					reportName = "shipReport_ezcat_A4_2";
					reportCName = URLEncoder.encode("黑貓二模", "UTF-8");
				}else if("ezcat_a4_2_pick".equals(modeltype)){
					reportName = "shipReport_ezcat_A4_2_pick";
					reportCName = URLEncoder.encode("黑貓二模含揀貨單", "UTF-8");
				}else{
					return;
				}

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id").toString());
				hm.put("p_tracking_number_list", track_list);
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
				
				writeToClient(request, response, reportCName, pdfFileName);
			} else if ("rptInvManual".equals(request.getParameter("action"))){
				logger.debug("action:"+request.getParameter("action"));
				
				final String reportName = "rptInvoice";
				final String reportCName = URLEncoder.encode("B2C發票", "UTF-8");
				String reportDetailName = "rptInvoiceDetail";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String jrxmlFileDetailName = reportSourcePath + "/" + reportDetailName + ".jrxml";
				String jasperFileDetailName = reportGeneratePath + "/" + reportDetailName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportCName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";
				
				String ids=request.getParameter("ids");
				String group_id=(String) request.getSession().getAttribute("group_id");

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName, jasperFileDetailName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id",group_id);
				hm.put("p_inv_manual_ids",ids);
				logger.debug("ids:"+ids);
				
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				writeToClient(request, response, reportCName, pdfFileName);
			} else if ("rptSfShip".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptSfShip";
				final String reportCName = URLEncoder.encode("順豐整合", "UTF-8");
				String reportDetailName1 = "rptShipDetailForSF";
				String reportDetailName2 = "rptShipSfDetail";
				String reportDetailName3 = "rptShipSfStatus";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String jrxmlFileDetailName1 = reportSourcePath + "/" + reportDetailName1 + ".jrxml";
				String jasperFileDetailName1 = reportGeneratePath + "/" + reportDetailName1 + ".jasper";
				String jrxmlFileDetailName2 = reportSourcePath + "/" + reportDetailName2 + ".jrxml";
				String jasperFileDetailName2 = reportGeneratePath + "/" + reportDetailName2 + ".jasper";
				String jrxmlFileDetailName3 = reportSourcePath + "/" + reportDetailName3 + ".jrxml";
				String jasperFileDetailName3 = reportGeneratePath + "/" + reportDetailName3 + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf";

				String order_no = request.getParameter("order_no");
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				String group_id = (String) request.getSession().getAttribute("group_id");

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName1, jasperFileDetailName1);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName2, jasperFileDetailName2);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName3, jasperFileDetailName3);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_order_no", order_no);
				hm.put("p_start_date", start_date);
				hm.put("p_end_date", end_date);

				logger.debug("order_no:" + order_no);
				logger.debug("p_group_id:" + group_id);
				logger.debug("p_start_date:" + start_date);
				logger.debug("p_end_date:" + end_date);

				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				writeToClient(request, response, reportCName, pdfFileName);
			}

		} catch (Exception e) {
			logger.error("Exception:" + e);
		}
	}
	
	private static String genDateFormat(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	private static void writeToClient(HttpServletRequest request, HttpServletResponse response, String reportCName, String pdfFileName) throws IOException{
		String browserType = request.getHeader("User-Agent");
		logger.debug("User-Agent:" + browserType);
		response.setContentType("APPLICATION/PDF; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		if (browserType.contains("IE")||browserType.contains("Chrome")){
			response.setHeader("Content-Disposition", "inline;Filename=\"" + reportCName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf" + "\"");
		} else if(browserType.contains("Firefox")){
			response.setHeader("Content-Disposition", "inline;Filename*=UTF-8''" + reportCName + "_" + genDateFormat("yyyyMMddHHmmss") + ".pdf" + "");
		}

		File file = new File(pdfFileName);
		FileInputStream fileIn = new FileInputStream(file);
		ServletOutputStream out = response.getOutputStream();
		byte[] outputByte = new byte[4096];
		while (fileIn.read(outputByte, 0, 4096) != -1) {
			out.write(outputByte, 0, 4096);
		}
		fileIn.close();
		out.flush();
		out.close();
	}
}
