package tw.com.aber.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

@SuppressWarnings("serial")

public class shipreport extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(shipreport.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("group_id") == null) {
			response.getWriter().write("no_session!");
			return;
		}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String kind = request.getParameter("kind");
		logger.debug("Action:" + action);
		logger.debug("Kind:" + kind);
		
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id = (group_id == null || group_id.length() < 3) ? "UNKNOWN" : group_id;

		String time1 = (request.getParameter("time1") == null) ? ""
				: request.getParameter("time1").replaceAll("/", "-");
		time1 = (time1.length() < 3) ? "2016-06-01" : time1;
		String time2 = (request.getParameter("time2") == null) ? ""
				: request.getParameter("time2").replaceAll("/", "-");
		time2 = (time2.length() < 3) ? "2300-12-31" : time2;

		logger.debug("time1:" + time1);
		logger.debug("time2:" + time2);
		
		String conString = getServletConfig().getServletContext().getInitParameter("pythonwebservice")
				+ "/ship/groupid=" + new String(Base64.encodeBase64String(group_id.getBytes())) + "&strdate="
				+ new String(Base64.encodeBase64String(time1.getBytes())) + "&enddate="
				+ new String(Base64.encodeBase64String(time2.getBytes()));
		logger.debug("conString:" + conString);
		
		String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		
		String reportSourcePath = ("" + this.getClass().getResource("/")).substring(5)
				.replace("VirtualBusiness/WEB-INF/classes/", "VirtualBusiness/WEB-INF/");
		
		String reportGeneratePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/report";
		new File(reportGeneratePath).mkdir();
		
		
		String reportName = "rptShipRpt";
		
		String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
		String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
		String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";
		String xlsFileName = reportGeneratePath + "/" + reportName + ".xls";

		try {
			JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
	
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
			
			HashMap<String, Object> hm = null;
			
			hm = new HashMap<String, Object>();
			hm.put("p_group_id", request.getSession().getAttribute("group_id"));
			hm.put("p_start_date", time1);
			hm.put("p_end_date", time2);
			
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
			JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
			
			JRXlsExporter exporter = new JRXlsExporter();
	        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jprint);
	        exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, xlsFileName);

	        exporter.exportReport();

	        File file = null;
			if (kind.equals("pdf")) {
				response.setContentType("APPLICATION/PDF");
				String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
				response.setHeader("Content-Disposition", disHeader);
				file = new File(pdfFileName);
			} else if (kind.equals("xls")) {
				response.setContentType("application/vns.ms-excel");
				String disHeader = "inline;Filename=\"" + reportName + ".xls" + "\"";
				response.setHeader("Content-Disposition", disHeader);
				file = new File(xlsFileName);
			}
			
			
			FileInputStream fileIn = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			while (fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
			}
	
			fileIn.close();
			out.flush();
			out.close();
		
		} catch (JRException e) {
			logger.error("JRException:" + e.getMessage());
		} catch (SQLException e) {
			logger.error("SQLException:" + e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException:" + e.getMessage());
		}
		return;
	}
}
