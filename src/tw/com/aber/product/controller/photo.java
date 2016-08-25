package tw.com.aber.product.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;

public class photo extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
			String processName =java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\n  I'm photo-java with PID = "+ Long.parseLong(processName.split("@")[0])+".\r\n";
			FileWriter fw;
			try{
				fw = new FileWriter(record_log,true);
			}catch(FileNotFoundException e){
				fw = new FileWriter(record_log,false);
			}
			fw.write(my_msg);
			fw.close();
		}catch(Exception e){System.out.println("Error: "+e.toString());}
		
		File file;
		request.setCharacterEncoding("UTF-8");

		String upload_root = getServletConfig().getServletContext().getInitParameter("photopath");

		String msg = "Have Done:<br>";
		
		String filePath = upload_root;

		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		
		if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			factory.setRepository(new File(""));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);
			try {
				List fileItems = upload.parseRequest(request);
				Iterator i = fileItems.iterator();
				List<File4json> filelist = new ArrayList<File4json>();
				
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();
						String fileExt = FilenameUtils.getExtension(fileName);
						
						if (fileName.equals("")) {
							continue;
						}
						String[] tmp = fileName.split("/");
						
						int j = 0;
						while (j < tmp.length) {
							j++;
						}
						j = j > 0 ? j - 1 : j;
						
						boolean isInMemory = fi.isInMemory();
						long sizeInBytes = fi.getSize();
						
						//window
						//file = new File(filePath + "\\" + tmp[j]);
//						file = new File(filePath + "/" + tmp[j]);
//						fi.write(file);
						
						UUID uuid  =  UUID.randomUUID(); 
						file = new File(filePath + "/" + uuid + "." + fileExt);
						fi.write(file);

						msg += "&nbsp&nbsp upload " + fileName + "<br>&nbsp --> &nbsp" + filePath + "/" + tmp[j];
						
						File4json tmpfile = new File4json();
//						tmpfile.setName(tmp[j]);
						tmpfile.setName( uuid + "." + fileExt );
						
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(tmpfile);
						response.getWriter().write("{\"files\": [" + jsonStrList + "]}");

						return;
					}
				}
				
			} catch (Exception ex) {
				System.out.println(ex);
			}
		} else {

		}
	}
}
