package tw.com.aber.product.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class image extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public image() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String processName =java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		String my_msg = "I'm image.java.\n\tThis is "+(new Date()).toString()+".\n\tMy PID is "+ Long.parseLong(processName.split("@")[0])+" .\n";
		String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
		
		FileWriter fw = new FileWriter(record_log,true);
		fw.write(my_msg);
		fw.close();
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("image/jpeg");

		String upload_root = getServletConfig().getServletContext().getInitParameter("photopath");
		
		String picname = request.getParameter("picname");
		if(!picname.equals("")){
			File f = new File(upload_root + "/" + picname);
			BufferedImage bi = ImageIO.read(f);
			OutputStream out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.close();
		} else {
			System.out.println("沒有圖檔名稱");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
