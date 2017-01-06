package tw.com.aber;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.*;
import javax.servlet.*;

public class fileoutput  extends HttpServlet {
	public String file_name = "";
	public String ori_file_name = "";
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		//input the path retrieve from webservice 
		String filename = request.getParameter("filename");
		if(filename==null)return ;
//		System.out.println(new String(Base64.decodeBase64((filename).getBytes())));
//		filename="C:/result1.csv";
		try {	
			String path =getServletConfig().getServletContext().getInitParameter("groupbuyoutputpath")+"/"
					+ (new String(Base64.decodeBase64((filename).getBytes())));//request.getRealPath("/") + "test.xls";
			if("log.txt".equals(new String(Base64.decodeBase64((filename).getBytes())))){
				path="/data/vbupload/log.txt";
			}
			FileInputStream fileInput = new FileInputStream(path);
			int i = fileInput.available();
			byte[] content = new byte[i];
			fileInput.read(content);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(new String(Base64.decodeBase64((filename).getBytes()))));
			OutputStream output = response.getOutputStream();
			output.write(content);
			output.flush();
			fileInput.close();
			output.close();
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println(e.toString());
		}
	}
//	protected void doPost(HttpServletRequest httprequest,HttpServletResponse httpresponse) throws ServletException, IOException {
//		
////		request.setCharacterEncoding("UTF-8");
////	    response.setCharacterEncoding("UTF-8");
////		String fileName = "/data/vbupload/rptPickReport.pdf" ;
//		String fileName = "/WebContent/upload/log.txt" ;
//		if (httprequest.getProtocol().compareTo("HTTP/1.0") == 0) {
//	        httpresponse.setHeader("Pragma", "No-cache");
//	      }else if (httprequest.getProtocol().compareTo("HTTP/1.1") == 0) {
//	        httpresponse.setHeader("Cache-Control", "No-cache");
//	      }
//	      MimetypesFileTypeMap MIME_TYPES = new MimetypesFileTypeMap();
//	      //判別檔案類型
//	      httpresponse.setContentType(MIME_TYPES.getContentType(fileName));
//	      if (httprequest.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
//	        httpresponse.setHeader("Content-Disposition","filename=" +
//	            new String(fileName.getBytes("Big5"),"ISO8859_1"));
//	      }
//	      else {
//	        httpresponse.addHeader( "Content-Disposition", "attachment;filename=" +
//	            new String(fileName.getBytes("Big5"),"ISO8859_1"));
//	      }
//	      httpresponse.setContentType("application/octet-stream");      
////	      httpresponse.getOutputStream().write("你的檔案的byte");
//	      httpresponse.getOutputStream().write(34474);
//	      httpresponse.setStatus(HttpServletResponse.SC_OK);
//	      httpresponse.flushBuffer(); 
//	}
}
