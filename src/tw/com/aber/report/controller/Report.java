package tw.com.aber.report.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import tw.com.aber.egs.controller.EgsApi;
import tw.com.aber.util.Database;

public class Report extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String sp_get_ship_sf_delivery_new_no = "call sp_delivery_report(?,?,?,?)";
	private static final String pdf = "pdf";
	private static final String xls = "xls";
	private static final Logger logger = LogManager.getLogger(Report.class);
	
	private Connection connection;
	private String sourcePath;
	private String generatePath;
	
	private void setConnection(){
		connection = Database.getConnection();
	}
	
	private void setPath(){
		final String reportSourcePath = ("" + this.getClass().getResource("/")).substring(5)
				.replace("VirtualBusiness/WEB-INF/classes/", "VirtualBusiness/WEB-INF/");
		final String reportGeneratePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/report";
		new File(reportGeneratePath).mkdir();
		
		sourcePath = reportSourcePath;
		generatePath = reportGeneratePath;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("group_id") == null) {
			return;
		}
		
		final String group_id = (String) request.getSession().getAttribute("group_id");
		
		HashMap<String, Object> hm = null;
		setConnection();
		setPath();
		
		try {
			if (request.getParameter("dis_date") != null) {
				String disDate = request.getParameter("dis_date");
				logger.debug("dis_date:"+disDate);
				
				final String reportName = "rptPickReport";
				final String exportName = "揀貨單";

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_dis_date", disDate);
				
				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if (request.getParameter("date") != null) {
				String date = request.getParameter("date");
				logger.debug("date:"+date);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = connection.prepareStatement(
							"select count(sale_id) n from tb_sale where dis_date = ? and group_id = ?");
					pstmt.setString(1, date);
					pstmt.setString(2, group_id);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						response.getWriter().write(rs.getString("n"));
					}
				} catch (SQLException se) {
					System.out.println("ERROR WITH: " + se);
				}
			} else if (request.getParameter("sale_id") != null) {
				String saleId = request.getParameter("sale_id");
				logger.debug("sale_id:"+saleId);
				
				final String reportName = "rptDistributeReport";
				final String exportName = "出貨單";

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_sale_id", saleId);
				
				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if (request.getParameter("pick_id") != null) {
				String pickId = request.getParameter("pick_id");
				logger.debug("pick_id:"+pickId);
				
				final String reportName = "rptPick";
				final String exportName = "揀貨單";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_pick_id", pickId);				
				
				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genJaserFile(setMyFile("rptPickDetail", "", pdf));
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if (request.getParameter("pick_no") != null) {
				String pickNo = request.getParameter("pick_no");
				logger.debug("pick_no:"+pickNo);
				final String reportName = "rptShip";
				final String exportName = "出貨單";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id",group_id);
				hm.put("p_pick_no", pickNo);
				
				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genJaserFile(setMyFile("rptShipDetail", "", pdf));
				genJaserFile(setMyFile("rptShipSubDetail", "", pdf));
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("ship_report".equals(request.getParameter("type"))) {
				logger.debug("type:"+request.getParameter("type"));
				String address = request.getParameter("address");
				String order_no = request.getParameter("order_no");
				String modeltype = request.getParameter("modeltype");
				String reportName = "";
				String exportName = "";

				EgsApi egsApi = new EgsApi();
				String suda7 = egsApi.getQuerySuda7(address);
				EgsApi api = new EgsApi();
				String eGSNum = api.getEGSNum();
				
				logger.debug("address:"+address);
				logger.debug("suda7:"+suda7);
				logger.debug("eGSNum:"+eGSNum);
				logger.debug("group_id:"+group_id);
				logger.debug("order_no:"+order_no);
				logger.debug("modeltype:"+modeltype);

				if ("a4_2".equals(modeltype)) {
					reportName = "shipReportA4_2";
					exportName = "黑貓二模";
				} else if ("a4_3".equals(modeltype)) {
					reportName = "shipReportA4_3";
					exportName = "黑貓三模";
				} else {
					return;
				}

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_order_no", order_no);
				hm.put("p_suda7", suda7);
				hm.put("p_egs_num", eGSNum);
				
				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("reportEzcat".equals(request.getParameter("type"))) {
				logger.debug("type:"+request.getParameter("type"));
				String track_list = request.getParameter("track_list");
				String modeltype = request.getParameter("modeltype");
				String reportName = "";
				String exportName = "";
				
				logger.debug("group_id:"+group_id);
				logger.debug("track_list:"+track_list);
				logger.debug("modeltype:"+modeltype);

				if ("ezcat".equals(modeltype)) {
					reportName = "shipReport_ezcat";
					exportName = "黑貓";
				} else if ("ezcat_a4_2".equals(modeltype)) {
					reportName = "shipReport_ezcat_A4_2";
					exportName = "黑貓二模";
				} else if ("ezcat_a4_2_pick".equals(modeltype)) {
					reportName = "shipReport_ezcat_A4_2_pick";
					exportName = "黑貓二模含揀貨單";
				} else {
					return;
				}
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_tracking_number_list", track_list);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genJaserFile(setMyFile("shipReport_ezcat_template", "", pdf));
				genJaserFile(setMyFile("shipReportA4_2_pick_main", "", pdf));
				genJaserFile(setMyFile("shipReportA4_2_pick_delivery", "", pdf));
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("rptInvManual".equals(request.getParameter("action"))){
				logger.debug("action:"+request.getParameter("action"));
				
				final String reportName = "rptInvoice";
				final String exportName = "B2B發票";
				
				String ids=request.getParameter("ids");

				hm = new HashMap<String, Object>();
				hm.put("p_group_id",group_id);
				hm.put("p_inv_manual_ids",ids);
				logger.debug("ids:"+ids);
				
				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genJaserFile(setMyFile("rptInvoiceDetail", "", pdf));
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("rptSfShip".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptSfShip";
				final String exportName = "順豐整合";
				
				String order_no = request.getParameter("order_no");
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");

				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_order_no", order_no);
				hm.put("p_start_date", start_date);
				hm.put("p_end_date", end_date);

				logger.debug("order_no:" + order_no);
				logger.debug("p_group_id:" + group_id);
				logger.debug("p_start_date:" + start_date);
				logger.debug("p_end_date:" + end_date);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genJaserFile(setMyFile("rptShipDetailForSF", "", pdf));
				genJaserFile(setMyFile("rptShipSfDetail", "", pdf));
				genJaserFile(setMyFile("rptShipSfStatus", "", pdf));
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportProduct".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportProduct";
				final String exportName = "商品報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportProductXls".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportProductXls";
				final String exportName = "商品報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, xls);
				genJaserFile(myFile);
				genXlsFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportPackage".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportPackage";
				final String exportName = "組合商品報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportPackageXls".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportPackageXls";
				final String exportName = "組合商品報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, xls);
				genJaserFile(myFile);
				genXlsFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportPackageDetail".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportPackageDetail";
				final String exportName = "組合商品明細報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportPackageDetailXls".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportPackageDetailXls";
				final String exportName = "組合商品明細報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, xls);
				genJaserFile(myFile);
				genXlsFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportContrast".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportContrast";
				final String exportName = "商品對照報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportContrastXls".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportContrastXls";
				final String exportName = "商品對照報表";
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);

				MyFile myFile = setMyFile(reportName, exportName, xls);
				genJaserFile(myFile);
				genXlsFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportSale".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportSale";
				final String exportName = "訂單報表";
				
				String start = request.getParameter("start");
				String end = request.getParameter("end");
				logger.debug("start:" + start);
				logger.debug("end:" + end);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_start", start);
				hm.put("p_end", end);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportSaleXls".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportSaleXls";
				final String exportName = "訂單報表";

				String start = request.getParameter("start");
				String end = request.getParameter("end");
				logger.debug("start:" + start);
				logger.debug("end:" + end);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_start", start);
				hm.put("p_end", end);

				MyFile myFile = setMyFile(reportName, exportName, xls);
				genJaserFile(myFile);
				genXlsFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportSaleDetail".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportSaleDetail";
				final String exportName = "訂單明細報表";
				
				String start = request.getParameter("start");
				String end = request.getParameter("end");
				logger.debug("start:" + start);
				logger.debug("end:" + end);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_start", start);
				hm.put("p_end", end);

				MyFile myFile = setMyFile(reportName, exportName, pdf);
				genJaserFile(myFile);
				genPdfFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			} else if ("exportSaleDetailXls".equals(request.getParameter("action"))) {
				logger.debug("action:" + request.getParameter("action"));

				final String reportName = "rptExportSaleDetailXls";
				final String exportName = "訂單明細報表";

				String start = request.getParameter("start");
				String end = request.getParameter("end");
				logger.debug("start:" + start);
				logger.debug("end:" + end);
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", group_id);
				hm.put("p_start", start);
				hm.put("p_end", end);

				MyFile myFile = setMyFile(reportName, exportName, xls);
				genJaserFile(myFile);
				genXlsFile(fillReport(myFile, hm), myFile);
				writeToClient(request, response, myFile);
			}

		} catch (Exception e) {
			logger.error("Exception:" + e);
		}
	}
		
	private static String genDateFormat(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	private static void genJaserFile(MyFile myFile) throws JRException {
		JasperCompileManager.compileReportToFile(myFile.getJrxml(), myFile.getJasper());
	}
	
	private JasperPrint fillReport(MyFile myFile, HashMap<String, Object> hm) throws JRException {
		return JasperFillManager.fillReport(myFile.getJasper(), hm, connection);
	}
	
	private void genPdfFile(JasperPrint jasperPrint, MyFile myFile) throws JRException{
		JasperExportManager.exportReportToPdfFile(jasperPrint, myFile.getPdf());
	}
	
	private void genXlsFile(JasperPrint jasperPrint, MyFile myFile) throws JRException{
		JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, myFile.getXls());
        exporter.exportReport();
	}

	private MyFile setMyFile(String reportName, String exportName, String kind) throws UnsupportedEncodingException {
		MyFile myFile = new MyFile();
		myFile.setJrxml(sourcePath + "/" + reportName);
		myFile.setJasper(generatePath + "/" + reportName);
		if (kind.equals(pdf)){
			myFile.setPdf(generatePath + "/" + reportName);
		} else if (kind.equals(xls)){
			myFile.setXls(generatePath + "/" + reportName);
		}
		myFile.setExportName(exportName);
		myFile.setKind(kind);
		return myFile;
	}
	
	private static void writeToClient(HttpServletRequest request, HttpServletResponse response, MyFile myFile) throws IOException{
		String browserType = request.getHeader("User-Agent");
		logger.debug("User-Agent:" + browserType);
		
		File file = null;
		response.setCharacterEncoding("UTF-8");
		if (myFile.getKind().equals(pdf)) {
			response.setContentType("APPLICATION/PDF; charset=UTF-8");
			if (browserType.contains("IE")||browserType.contains("Chrome")){
				response.setHeader("Content-Disposition", "inline;Filename=\"" + myFile.getExportName() + "_" + genDateFormat("yyyyMMddHHmmss") + "." + pdf + "\"");
			} else if(browserType.contains("Firefox")){
				response.setHeader("Content-Disposition", "inline;Filename*=UTF-8''" + myFile.getExportName() + "_" + genDateFormat("yyyyMMddHHmmss") + "." + pdf + "");
			}
			file = new File(myFile.getPdf());
		} else if (myFile.getKind().equals(xls)) {
			response.setContentType("APPLICATION/vns.ms-excel; charset=UTF-8");
			if (browserType.contains("IE")||browserType.contains("Chrome")){
				response.setHeader("Content-Disposition", "inline;Filename=\"" + myFile.getExportName() + "_" + genDateFormat("yyyyMMddHHmmss") + "." + xls + "\"");
			} else if(browserType.contains("Firefox")){
				response.setHeader("Content-Disposition", "inline;Filename*=UTF-8''" + myFile.getExportName() + "_" + genDateFormat("yyyyMMddHHmmss") + "." + xls + "");
			}
			file = new File(myFile.getXls());
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
	}
	
	class MyFile {
		private String jrxml;
		private String jasper;
		private String pdf;
		private String xls;
		private String exportName;
		private String kind;
		
		public String getJrxml() {
			return jrxml;
		}
		public void setJrxml(String jrxml) {
			this.jrxml = jrxml.concat(".jrxml");
		}
		public String getJasper() {
			return jasper;
		}
		public void setJasper(String jasper) {
			this.jasper = jasper.concat(".jasper");
		}
		public String getPdf() {
			return pdf;
		}
		public void setPdf(String pdf) {
			this.pdf = pdf.concat("_").concat( genDateFormat("yyyyMMddHHmmss") ).concat(".pdf");
		}
		public String getXls() {
			return xls;
		}
		public void setXls(String xls) {
			this.xls = xls.concat("_").concat( genDateFormat("yyyyMMddHHmmss") ).concat(".xls");
		}
		public String getExportName() {
			return exportName;
		}
		public void setExportName(String exportName) throws UnsupportedEncodingException {
			this.exportName = URLEncoder.encode(exportName, "UTF-8");
		}
		public String getKind() {
			return kind;
		}
		public void setKind(String kind) {
			this.kind = kind;
		}
	}
}
