package tw.com.aber.basicdataimport.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
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

public class BasicDataImport extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(BasicDataImport.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		transfer(request, response);

		String action = request.getParameter("action");

		String[] actions = { "download" };

		int key = Arrays.asList(actions).indexOf(action);
		
		logger.debug("action: " + action);

		logger.debug("key: " + key);

		switch (key) {

		case 0: {
			String type = request.getParameter("type");

			String file_path = "/data/vbBasicData/template/" + type + ".xls";

			try {
				FileInputStream fileInput = new FileInputStream(file_path);
				int i = fileInput.available();
				byte[] content = new byte[i];

				fileInput.read(content);
				response.setContentType("application/octet-stream");
				String downloadName = type + ".xls";
				logger.debug("\ndownload file path: {}\ndownload file name: {}", file_path, downloadName);
				response.setHeader("Content-Disposition",
						"attachment;filename=".concat(java.net.URLEncoder.encode(downloadName, "UTF-8")));

				OutputStream output = response.getOutputStream();
				output.write(content);
				output.flush();
				fileInput.close();
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(
						"<html><head><title>one white html</title><meta charset='UTF-8'></head><body style='text-align:center;font-size:48px;color:red;'><br>找不到檔案</body></html>");
			}
			break;
		}
		default: {

			break;
		}
		}
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

			String type = "", folderName = "";

			try {
				List<?> fileItems = upload.parseRequest(request);
				Iterator<?> i = fileItems.iterator();
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (fi.getFieldName().equals("type"))
						type = fi.getString();
					if (fi.getFieldName().equals("folderName"))
						folderName = fi.getString();
				}
				logger.debug("\n\ntype:{}\nfolderName:{}\n\n", type, folderName);
				// Requires a new iterator
				i = fileItems.iterator();
				while (i.hasNext()) {

					FileItem fi = (FileItem) i.next();

					if (!fi.isFormField() && !fi.getFieldName().equals("action") && !fi.getFieldName().equals("type")
							&& !fi.getFieldName().equals("folderName")) {

						String fileName = FilenameUtils.getName(fi.getName());
						String ext = FilenameUtils.getExtension(fileName);
						String _uid = UUID.randomUUID().toString();

						savePath = getServletConfig().getServletContext().getInitParameter("basicImport") + type + "/"
								+ group_id;

						fullPath = savePath + "/" + _uid + "." + ext;

						File file = null;
						file = new File(savePath);
						if (!file.exists()) {
							file.mkdirs();
						}
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

					} else if (!fi.getFieldName().equals("action") && !fi.getFieldName().equals("type")
							&& !fi.getFieldName().equals("folderName")) {

						String fieldName = fi.getFieldName();
						String _uid = UUID.randomUUID().toString();

						savePath = getServletConfig().getServletContext().getInitParameter("basicImport") + type + "/"
								+ group_id;
						String ext = FilenameUtils.getExtension(fieldName);
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
						+ "/import/urls=" + new String(Base64.encodeBase64String((fullPath).getBytes())) + "&UsID="
						+ new String(Base64.encodeBase64String(user_id.getBytes())) + "&lgID="
						+ new String(Base64.encodeBase64String("26".getBytes())) + "&aaID="
						+ new String(Base64.encodeBase64String(group_id.getBytes()));
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

	protected String putFile(HttpServletRequest request, HttpServletResponse response, String type, String filename)
			throws ServletException, IOException {
		String conString = "", ret = "";
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		String _uid = UUID.randomUUID().toString();

		String savePath = getServletConfig().getServletContext().getInitParameter("basicImport") + type + "/"
				+ group_id;
		String ext = FilenameUtils.getExtension(filename);
		String fullPath = savePath + "/" + _uid + "." + ext;
		File file = null;
		file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			String file_over = getServletConfig().getServletContext().getInitParameter(type) + "/fail";
			factory.setRepository(new File(file_over));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);
			try {
				List<?> fileItems = upload.parseRequest(request);
				Iterator<?> i = fileItems.iterator();
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (!fi.isFormField()) {

						conString = getServletConfig().getServletContext().getInitParameter("pythonwebservice")
								+ "/import/urls=" + new String(Base64.encodeBase64String((fullPath).getBytes()))
								+ "&UsID=" + new String(Base64.encodeBase64String(user_id.getBytes())) + "&lgID="
								+ new String(Base64.encodeBase64String("26".getBytes())) + "&aaID="
								+ new String(Base64.encodeBase64String(group_id.getBytes()));
						logger.debug("\nfullPath: {}\nconString : {}", fullPath, conString);
						file = new File(fullPath);
						fi.write(file);
					}
				}
			} catch (Exception ex) {
				ret = "E_write_File:" + ex.toString();
				return ret;
			}
		} else {
			ret = "E_No one found.";
			return ret;
		}
		if (ret.length() > 3) {
			return ret;
		}
		return conString;
	}

	protected String webService(HttpServletRequest request, HttpServletResponse response, String conString)
			throws ServletException, IOException {

		String ret = "false";
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(conString);
		try {
			client.executeMethod(method);
		} catch (Exception e) {
			ret = e.toString();
			ret = "Error of call webservice:" + ret;
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
}

class Webserviceoutput {
	String info;
	String success;
}
