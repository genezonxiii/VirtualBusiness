package tw.com.aber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class groupbuying extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(groupbuying.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");
		if ("select_platform_kind".equals(action)) {
			UploadService service = new UploadService();
			String jsonStrList = service.getPlatformJson();
			response.getWriter().write(jsonStrList);
		} else if ("select_way_of_platform".equals(action)) {
			UploadService service = new UploadService();
			String jsonStrList = service.getPlatformWayJson();
			response.getWriter().write(jsonStrList);
		} else if ("download".equals(action)) {
			String filePath = request.getParameter("file_path");
			String fileName = request.getParameter("file_name");
			FileInputStream fileInput = null;
			OutputStream output = null;
			try {
				fileInput = new FileInputStream(filePath);
				int i = fileInput.available();
				byte[] content = new byte[i];

				String[] extArr = filePath.split("\\.");
				String ext = ".";
				if (filePath.length() > 1)
					ext += extArr[extArr.length - 1];

				fileInput.read(content);
				response.setContentType("application/octet-stream");

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-轉檔-");
				String today = sdf.format(new Date());
				String downloadName = today + fileName + ext;

				logger.debug("\ndownload file path: {}\ndownload file name: {}", filePath, downloadName);
				response.setHeader("Content-Disposition",
						"attachment;filename=".concat(java.net.URLEncoder.encode(downloadName, "UTF-8")));

				output = response.getOutputStream();
				output.write(content);
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(
						"<html><head><title>one white html</title><meta charset='UTF-8'></head><body style='text-align:center;font-size:48px;color:red;'><br>找不到檔案</body></html>");
			} finally {
				output.flush();
				fileInput.close();
				output.close();
			}
		} else {
			transfer(request, response);
		}

	}

	class GroupbuyingDao {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_select_groupbuying_throwfile_platform = "call sp_select_groupbuying_throwfile_platform ()";
		private static final String sp_select_groupbuying_throwfile_platform_way = "call sp_select_groupbuying_throwfile_platform_way ()";
		private static final String sp_insert_upload = "call sp_insert_upload(?,?,?,?,?)";

		public List<Throwfile> searchPlatformDB() {

			List<Throwfile> list = new ArrayList<Throwfile>();
			Throwfile throwfile = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_groupbuying_throwfile_platform);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					throwfile = new Throwfile();
					throwfile.setThrowfile_name(rs.getString("throwfile_name"));
					throwfile.setThrowfile_id(rs.getString("throwfile_id"));
					throwfile.setThrowfile_platform(rs.getString("throwfile_platform"));
					throwfile.setThrowfile_type(rs.getString("throwfile_type"));
					throwfile.setMemo(rs.getString("memo"));
					throwfile.setIcon(rs.getString("icon"));
					throwfile.setReversed(rs.getString("reversed"));
					throwfile.setThrowfile_fileextension(rs.getString("throwfile_fileextension"));
					list.add(throwfile); // Store the row in the list
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				// Clean up JDBC resources
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}

		public List<Throwfile> searchPlatformWayDB() {

			List<Throwfile> list = new ArrayList<Throwfile>();
			Throwfile throwfile = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_groupbuying_throwfile_platform_way);

				// pstmt.setString(1, platform);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					throwfile = new Throwfile();
					throwfile.setThrowfile_name(rs.getString("throwfile_name"));
					throwfile.setThrowfile_platform(rs.getString("throwfile_platform"));
					throwfile.setThrowfile_type(rs.getString("throwfile_type"));
					throwfile.setThrowfile_fileextension(rs.getString("throwfile_fileextension"));
					list.add(throwfile); // Store the row in the list
				}

				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				// Clean up JDBC resources
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}

		public void insertUpload(String groupId, String orderSource, String delivery, String oriName, String uuidName) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_upload);

				cs.setString(1, groupId);
				cs.setString(2, orderSource);
				cs.setString(3, delivery);
				cs.setString(4, oriName);
				cs.setString(5, uuidName);

				cs.execute();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				// Clean up JDBC resources
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
	}

	class UploadService {
		private GroupbuyingDao dao;

		public UploadService() {
			dao = new GroupbuyingDao();
		}

		public String getPlatformJson() {
			String jsonStrList = "";
			Gson gson = null;
			List<Throwfile> list = null;
			try {
				gson = new Gson();
				list = dao.searchPlatformDB();
				jsonStrList = gson.toJson(list);
			} catch (Exception e) {
				logger.debug("getPlatformJson erroe: " + e.getMessage());
			}
			return jsonStrList;
		}

		public String getPlatformWayJson() {
			String jsonStrList = "";
			Gson gson = null;
			List<Throwfile> list = null;
			try {
				gson = new Gson();
				list = dao.searchPlatformWayDB();
				jsonStrList = gson.toJson(list);
			} catch (Exception e) {
				logger.debug("getPlatformWayJson erroe: " + e.getMessage());
			}
			return jsonStrList;
		}		

		public void insertUpload(String groupId, String orderSource, String delivery, String oriName, String uuidName) {
			try {
				dao.insertUpload(groupId, orderSource, delivery, oriName, uuidName);
			} catch (Exception e) {
				logger.debug("insertUpload error: " + e.getMessage());
			}
		}
	}

	protected String webService(HttpServletRequest request, HttpServletResponse response, String conString)
			throws ServletException, IOException {

		String ret = "false";
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(conString);
		try {
			client.executeMethod(method);
		} catch (Exception e) {
			logger.debug("Error of call webservice:" + e.getMessage());
			ret = "false";
		}
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			String content = writer.toString();
			int isJson = 0;
			Webserviceoutput jsonobj = new Webserviceoutput();
			Gson gson = new Gson();
			try {
				jsonobj = gson.fromJson(content, Webserviceoutput.class);
				isJson = 1;
			} catch (com.google.gson.JsonSyntaxException ex) {
				isJson = 0;
			}

			logger.debug("isJson: " + isJson);
			if (isJson == 1) {
				ret = content;
			} else {
				if (content.length() > 100) {
					content = content.substring(0, 90) + "....";
				}
				logger.debug("Error_Connection: get " + content + " on: " + conString);
				ret = "false";
			}
		} catch (Exception e) {
			logger.debug("Error of call webservice content:" + e.toString());
			ret = "false";
		}
		method.releaseConnection();
		return ret;
	}

	protected String transfer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String conString = "", ret = "", fullPath = "";
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		String savePath = "";
		String contentType = request.getContentType();
		String _uid = UUID.randomUUID().toString();

		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;

		if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);

			String platform = "", deliveryMethod = "", tdpl = "", pdcd = "";

			try {
				List<?> fileItems = upload.parseRequest(request);
				Iterator<?> i = fileItems.iterator();
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (fi.getFieldName().equals("platform"))
						platform = fi.getString();
					if (fi.getFieldName().equals("deliveryMethod"))
						deliveryMethod = fi.getString();
					if (fi.getFieldName().equals("tdpl"))
						tdpl = fi.getString();
					if (fi.getFieldName().equals("pdcd"))
						pdcd = fi.getString();
				}
				logger.debug("\n\nplatform:{}\ndeliveryMethod:{}\n\n", platform, deliveryMethod);

				savePath = getServletConfig().getServletContext().getInitParameter("groupbuypath") + "/" + platform
						+ "/" + deliveryMethod + "/" + group_id;

				File file = null;
				file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				// Requires a new iterator
				i = fileItems.iterator();
				while (i.hasNext()) {

					FileItem fi = (FileItem) i.next();

					if (!fi.isFormField() && !filter(fi.getFieldName())) {

						String fileName = FilenameUtils.getName(fi.getName());
						String ext = FilenameUtils.getExtension(fileName);

						String filecontent_ori = fi.getString("UTF-8");
						if (filecontent_ori.contains("text/html;") || filecontent_ori.contains("<html>")) {
							ext = "html";
						}

						fullPath = savePath + "/" + _uid + "." + ext;
						UploadService service = new UploadService();
						service.insertUpload(group_id, platform, deliveryMethod, fileName, _uid + "." + ext);
						
						logger.debug("\nfileName:{}\nsavePath:{}\nfullPath:{}", fileName, savePath, fullPath);

						moveTheFile(fi, fullPath);

					} else if (!filter(fi.getFieldName())) {

						String fieldName = fi.getFieldName();
						String ext = FilenameUtils.getExtension(fieldName);

						String filecontent_ori = fi.getString("UTF-8");
						if (filecontent_ori.contains("text/html;") || filecontent_ori.contains("<html>")) {
							ext = "html";
						}
						fullPath = savePath + "/" + _uid + "." + ext;

						logger.debug("\nfi:{}", fi);
						logger.debug("\nfieldName:{}\next:{}\nfullPath:{}", fieldName, ext, fullPath);

						moveTheFile(fi, fullPath);
					}
				}

				conString = getConString(fullPath, user_id, tdpl, pdcd);
				logger.debug("conString : " + conString);
				ret = webService(request, response, conString);
				response.getWriter().write(ret);
			} catch (FileUploadException e) {
				logger.debug("Cannot parse multipart request");
			} catch (Exception ex) {
				logger.debug("transfer error : " + ex.toString());
			}
		}

		return conString;
	}

	// 用來判斷 FileItem 排除要利用的參數
	public boolean filter(String string) {
		boolean result = false;
		String[] filterArr = { "action", "platform", "deliveryMethod", "tdpl", "pdcd" };
		for (String tmp : filterArr) {
			if (string.equals(tmp)) {
				result = true;
			}
		}
		return result;
	}

	public void moveTheFile(FileItem fi, String fullPath) throws IOException {
		InputStream is = fi.getInputStream();
		FileOutputStream fos = new FileOutputStream(fullPath);

		int len = 0;
		byte[] buffer = new byte[1024];

		try {
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			is.close();
			fos.flush();
			fos.close();
		}
	}

	public String getConString(String fullPath, String user_id, String tdpl, String pdcd) {
		String conString = "";
		try {
			conString = getServletConfig().getServletContext().getInitParameter("pythonwebservice") + "/groupbuy/urls="
					+ new String(Base64.encodeBase64String((fullPath).getBytes())) + "&usid="
					+ new String(Base64.encodeBase64String(user_id.getBytes())) + "&tdpl="
					+ new String(Base64.encodeBase64String(tdpl.getBytes())) + "&pdcd="
					+ new String(Base64.encodeBase64String(pdcd.getBytes()));
		} catch (Exception e) {
			logger.debug("getConString error: ".concat(e.getMessage()));
		}
		return conString;
	}

	class Throwfile {
		String throwfile_id;
		String throwfile_platform;
		String throwfile_type;
		String throwfile_name;
		String throwfile_fileextension;
		String icon;
		String memo;
		String reversed;

		public String getThrowfile_id() {
			return throwfile_id;
		}

		public void setThrowfile_id(String throwfile_id) {
			this.throwfile_id = throwfile_id;
		}

		public String getThrowfile_platform() {
			return throwfile_platform;
		}

		public void setThrowfile_platform(String throwfile_platform) {
			this.throwfile_platform = throwfile_platform;
		}

		public String getThrowfile_type() {
			return throwfile_type;
		}

		public void setThrowfile_type(String throwfile_type) {
			this.throwfile_type = throwfile_type;
		}

		public String getThrowfile_name() {
			return throwfile_name;
		}

		public void setThrowfile_name(String throwfile_name) {
			this.throwfile_name = throwfile_name;
		}

		public String getThrowfile_fileextension() {
			return throwfile_fileextension;
		}

		public void setThrowfile_fileextension(String throwfile_fileextension) {
			this.throwfile_fileextension = throwfile_fileextension;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getReversed() {
			return reversed;
		}

		public void setReversed(String reversed) {
			this.reversed = reversed;
		}

	}

	class Webserviceoutput {
		String info;
		String success;
		String duplicate;
	}
}