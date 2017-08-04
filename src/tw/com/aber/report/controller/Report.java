package tw.com.aber.report.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import tw.com.aber.ship.controller.ship;

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

		String reportSourcePath = ("" + this.getClass().getResource("/")).substring(5)
				.replace("VirtualBusiness/WEB-INF/classes/", "VirtualBusiness/WEB-INF/");
		if ('C' == ("" + this.getClass().getResource("/")).charAt(6)) {
			reportSourcePath = ("" + this.getClass().getResource("/")).substring(6)
					.replace("VirtualBusiness/WEB-INF/classes/", "VirtualBusiness/WEB-INF/");
		}
		String reportGeneratePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/report";
		new File(reportGeneratePath).mkdir();
		try {
			if (request.getParameter("dis_date") != null) {
				String reportName = "rptPickReport";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				hm.put("p_dis_date", request.getParameter("dis_date"));
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				response.setContentType("APPLICATION/PDF");
				String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
				response.setHeader("Content-Disposition", disHeader);

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

			} else if (request.getParameter("sale_id") != null) {
				String reportName = "rptDistributeReport";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				hm.put("p_sale_id", request.getParameter("sale_id"));
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				response.setContentType("APPLICATION/PDF");
				String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
				response.setHeader("Content-Disposition", disHeader);

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
			} else if (request.getParameter("pick_id") != null) {
				String reportName = "rptPick";
				String reportDetailName = "rptPickDetail";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String jrxmlFileDetailName = reportSourcePath + "/" + reportDetailName + ".jrxml";
				String jasperFileDetailName = reportGeneratePath + "/" + reportDetailName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName, jasperFileDetailName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				String pick_id = request.getParameter("pick_id");
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				hm.put("p_pick_id", pick_id);
				
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				response.setContentType("APPLICATION/PDF");
				String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
				response.setHeader("Content-Disposition", disHeader);

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
			} else if (request.getParameter("pick_no") != null) {
				String reportName = "rptShip";
				String reportDetailName = "rptShipDetail";

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String jrxmlFileDetailName = reportSourcePath + "/" + reportDetailName + ".jrxml";
				String jasperFileDetailName = reportGeneratePath + "/" + reportDetailName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";

				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
				JasperCompileManager.compileReportToFile(jrxmlFileDetailName, jasperFileDetailName);

				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id",request.getSession().getAttribute("group_id"));
				hm.put("p_pick_no",request.getParameter("pick_no"));
				
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);

				response.setContentType("APPLICATION/PDF");
				String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
				response.setHeader("Content-Disposition", disHeader);

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
			}else if (request.getParameter("date") != null) {
				String date = request.getParameter("date");
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
				String address = request.getParameter("address");
				String order_no = request.getParameter("order_no");
				String modeltype = request.getParameter("modeltype");
				String reportName ="";

				EgsApi egsApi = new EgsApi();
				String suda7 = egsApi.getQuerySuda7(address);
				EgsApi api = new EgsApi();
				String eGSNum = api.getEGSNum();
				
				logger.debug("address"+address);
				logger.debug("suda7"+suda7);
				logger.debug("eGSNum"+eGSNum);
				logger.debug("group_id"+request.getSession().getAttribute("group_id").toString());
				logger.debug("order_no"+order_no);
				logger.debug("modeltype"+modeltype);

				if("a4_2".equals(modeltype)){
					reportName = "shipReportA4_2";
				}else if("a4_3".equals(modeltype)){
					reportName = "shipReportA4_3";
				}else{
					return;
				}

				String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
				String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
				String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";

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
				
				response.setContentType("APPLICATION/PDF");
				String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
				response.setHeader("Content-Disposition", disHeader);

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

		} catch (Exception e) {
			System.out.print("Exception:" + e);
		}
	}

}
