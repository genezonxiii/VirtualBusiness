package tw.com.aber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class upload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(upload.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		logger.debug("Action:" + action);
		if ("select_platform_kind".equals(action)) {
			UploadService service = new UploadService();
			String jsonStrList = service.getPlatformJson();
			response.getWriter().write(jsonStrList);
		} else if ("select_way_of_platform".equals(action)) {
			UploadService service = new UploadService();
			String jsonStrList = service.getPlatformWayJson();
			response.getWriter().write(jsonStrList);
		} else {
			transfer(request, response);
		}

	}

	class UploadDao {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_select_upload_throwfile_platform = "call sp_select_upload_throwfile_platform ()";
		private static final String sp_select_upload_throwfile_platform_way = "call sp_select_upload_throwfile_platform_way ()";
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
				pstmt = con.prepareStatement(sp_select_upload_throwfile_platform);
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
				pstmt = con.prepareStatement(sp_select_upload_throwfile_platform_way);

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
		private UploadDao dao;

		public UploadService() {
			dao = new UploadDao();
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
				if ("true".equals(jsonobj.success)) {
					ret = content;
				} else {
					ret = "false";
				}
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

		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;

		if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);

			String platform = "", deliveryMethod = "";

			try {
				List<?> fileItems = upload.parseRequest(request);
				Iterator<?> i = fileItems.iterator();
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (fi.getFieldName().equals("platform"))
						platform = fi.getString();
					if (fi.getFieldName().equals("deliveryMethod"))
						deliveryMethod = fi.getString();
				}
				logger.debug("\n\nplatform:{}\ndeliveryMethod:{}\n\n", platform, deliveryMethod);
				// Requires a new iterator
				i = fileItems.iterator();
				while (i.hasNext()) {

					FileItem fi = (FileItem) i.next();

					if (!fi.isFormField() && !fi.getFieldName().equals("action")
							&& !fi.getFieldName().equals("platform") && !fi.getFieldName().equals("deliveryMethod")) {

						String fileName = FilenameUtils.getName(fi.getName());
						String ext = FilenameUtils.getExtension(fileName);

	        			String filecontent_ori= fi.getString("UTF-8");
						if(filecontent_ori.contains("text/html;")||filecontent_ori.contains("<html>")){
							ext = "html";
						}
						String _uid = UUID.randomUUID().toString();

						savePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/"
								+ platform + "/" + deliveryMethod + "/" + group_id;

						fullPath = savePath + "/" + _uid + "." + ext;

						File file = null;
						file = new File(savePath);
						if (!file.exists()) {
							file.mkdirs();
						}
						
						UploadService service = new UploadService();
						service.insertUpload(group_id, platform, deliveryMethod, fileName, _uid + "." + ext);
						
						logger.debug("\nfileName:{}\nsavePath:{}\nfullPath:{}", fileName, savePath, fullPath);
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

					} else if (!fi.getFieldName().equals("action") && !fi.getFieldName().equals("platform")
							&& !fi.getFieldName().equals("deliveryMethod")) {

						String fieldName = fi.getFieldName();
						String _uid = UUID.randomUUID().toString();

						savePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/"
								+ platform + "/" + deliveryMethod + "/" + group_id;

						String ext = FilenameUtils.getExtension(fieldName);

	        			String filecontent_ori= fi.getString("UTF-8");
						if(filecontent_ori.contains("text/html;")||filecontent_ori.contains("<html>")){
							ext = "html";
						}
						fullPath = savePath + "/" + _uid + "." + ext;
						File file = null;
						file = new File(savePath);
						if (!file.exists()) {
							file.mkdirs();
						}
						logger.debug("\nfi:{}", fi);
						logger.debug("\nfieldName:{}\next:{}\nfullPath:{}", fieldName, ext, fullPath);
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
				}
				conString = getServletConfig().getServletContext().getInitParameter("pythonwebservice")
						+ "/upload/urls=" + new String(Base64.encodeBase64String((fullPath).getBytes())) + "&UsID="
						+ new String(Base64.encodeBase64String(user_id.getBytes()));
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
