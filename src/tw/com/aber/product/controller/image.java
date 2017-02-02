package tw.com.aber.product.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class image extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public image() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");

		String upload_root = getServletConfig().getServletContext().getInitParameter("photopath");
		String picname = request.getParameter("picname");
		if (picname != null && !picname.equals("")) {
			File f = new File(upload_root + "/" + picname);
			try {
				BufferedImage bi = ImageIO.read(f);
				OutputStream out = response.getOutputStream();
				ImageIO.write(bi, "jpg", out);
				out.close();
			} catch (Exception e) {
				RequestDispatcher successView = request.getRequestDispatcher("/images/loss.gif");

				successView.forward(request, response);
				System.out.println("name: " + f + " with: " + e.toString());
			}
		} else {
			System.out.println("沒有圖檔名稱");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
