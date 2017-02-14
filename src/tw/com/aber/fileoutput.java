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
		String fileforgroupbuy = request.getParameter("fileforgroupbuy");
		String fileforinvoice = request.getParameter("fileforinvoice");
		String file_name="";//new String(Base64.decodeBase64((fileforgroupbuy).getBytes()));
		String file_path="";
		if(fileforgroupbuy==null && fileforinvoice==null) return ;
		if(fileforgroupbuy!=null){
			file_name = new String(Base64.decodeBase64(fileforgroupbuy.getBytes()));
			file_path = getServletConfig().getServletContext().getInitParameter("groupbuyoutputpath")+"/"
					+ file_name;//request.getRealPath("/") + "test.xls";
			
			if("log.txt".equals(file_name)){
				file_path="/data/vbupload/log.txt";
			}
			if("pyupload.log".equals(file_name)){
				file_path="/data/VirtualBusiness_Data/pyupload.log";
			}
		}
		if(fileforinvoice!=null){
			file_name = new String(Base64.decodeBase64(fileforinvoice.getBytes()));
			file_path = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/invoice/"+
					file_name.replace("/data/vbupload/invoice/", "");
			file_name=file_name.substring(file_name.lastIndexOf("/")+1);
		}
		
		
//		System.out.println(new String(Base64.decodeBase64((filename).getBytes())));
//		filename="C:/result1.csv";
//		if(fileforinvoice!=null){
//			String path =getServletConfig().getServletContext().getInitParameter("groupbuyoutputpath")+"/"
//					+ (new String(Base64.decodeBase64((fileforinvoice).getBytes())));
//		}
		//if(filename==null) return ;
		//String file_name=new String(Base64.decodeBase64((file_name).getBytes()));
		//String file_path="";
		//用file_name和file_path至於內容 前面決定
		try {
			
			// =getServletConfig().getServletContext().getInitParameter("groupbuyoutputpath")+"/"
					//+ (new String(Base64.decodeBase64((filename).getBytes())));//request.getRealPath("/") + "test.xls";
			FileInputStream fileInput = new FileInputStream(file_path);
			int i = fileInput.available();
			byte[] content = new byte[i];
			fileInput.read(content);
			response.setContentType("application/octet-stream");
			
			response.setHeader("Content-Disposition", "attachment;filename=".concat(file_name));
			OutputStream output = response.getOutputStream();
			output.write(content);
			output.flush();
			fileInput.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			//e.getStackTrace();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("<html><head><title>one white html</title><meta charset='UTF-8'></head><body style='text-align:center;font-size:48px;color:red;'><br>找不到檔案</body></html>");
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
