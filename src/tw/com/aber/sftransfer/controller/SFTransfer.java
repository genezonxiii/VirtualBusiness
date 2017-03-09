package tw.com.aber.sftransfer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
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

public class SFTransfer extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(SFTransfer.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		logger.debug("action: " + action);

		String[] actions = { "upload", "download" };

		int key = Arrays.asList(actions).indexOf(action);

		logger.debug("key: " + key);

		switch (key) {

		case 0: {

			String type = request.getParameter("type");
			String filename = request.getParameter("filename");
			logger.debug("\ntype: {}\nfilename: {}", type, filename);

			String conString = "";
			String ret = "E";
			conString = putFile(request, response, type, filename);

			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (Exception e) {
				ret = "Sleep error";
			}
			if (conString.charAt(0) != 'E') {
				ret = webService(request, response, conString);
			} else {
				ret = conString;
			}
			ret = ((ret == null) ? "E" : ret);
			logger.debug("ret: ", ret);
			response.getWriter().write(ret);

			break;
		}
		case 1: {
			String fileName = request.getParameter("fileName");
			String ext = FilenameUtils.getExtension(fileName);
			String encode_fileName = request.getParameter("downloadName");
			String[] downloadName = encode_fileName.split("_");
			String decode_fileName = new String(Base64.decodeBase64(downloadName[1].getBytes()));
			String file_path = "/data/vbSF_output/" + decode_fileName;

			try {
				FileInputStream fileInput = new FileInputStream(file_path);
				int i = fileInput.available();
				byte[] content = new byte[i];

				fileInput.read(content);
				response.setContentType("application/octet-stream");

				String tmp = "inbound".equals(downloadName[0]) ? "入庫明細表."+ext : "出庫明細表."+ext;
				response.setHeader("Content-Disposition", "attachment;filename=".concat(java.net.URLEncoder.encode(tmp, "UTF-8")));

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

	protected String putFile(HttpServletRequest request, HttpServletResponse response, String type, String filename)
			throws ServletException, IOException {
		String conString = "", ret = "";
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		String _uid = UUID.randomUUID().toString();

		String path = "inbound".equals(type) ? "inbound" : "outbound";

		String savePath = getServletConfig().getServletContext().getInitParameter(path) + "/" + group_id;
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
			String file_over = getServletConfig().getServletContext().getInitParameter(path) + "/fail";
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
								+ "/sfexpress/urls=" + new String(Base64.encodeBase64String((fullPath).getBytes()))
								+ "&UsID=" + new String(Base64.encodeBase64String(user_id.getBytes())) + "&lgID="
								+ new String(Base64.encodeBase64String("26".getBytes())) + "&aaID="
								+ new String(Base64.encodeBase64String(group_id.getBytes()));
						logger.debug("conString : " + conString);
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
					String[] tmp = jsonobj.download.split("/");
					int j = 0;
					while (j < tmp.length) {
						j++;
					}
					j = j > 0 ? j - 1 : j;
					ret = new String(Base64.encodeBase64String((tmp[j]).getBytes()));
				} else {
					ret = "false";
				}
			} else {
				if (content.length() > 100) {
					content = content.substring(0, 90) + "....";
				}
				ret = "Error_Connection: get " + content + " on: " + conString;
			}
		} catch (Exception e) {
			ret = e.toString();
			ret = "Error of call webservice content:" + ret;
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
	String download;
	String success;
}
