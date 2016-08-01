package tw.com.aber.login.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.com.aber.login.controller.login.LoginService;
import tw.com.aber.login.controller.login.LoginVO;

import com.google.gson.Gson;

public class HandleDrawValidateCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 120; // 圖片寬度
	private static final int HEIGHT = 30; // 圖片高度

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);
		// 創建圖片
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// 得到圖片
		Graphics graphics = bufferedImage.getGraphics();
		// 設置背景色
		setBackGround(graphics);
		// 設定圖片邊框
		setBordor(graphics);
		// 在圖片上畫干擾線條，用了4四種顏色，共二十條線條
		drawRandomLine(graphics, Color.GREEN);
		drawRandomLine(graphics, new Color(246, 255, 145));
		drawRandomLine(graphics, new Color(225, 174, 252));
		drawRandomLine(graphics, new Color(120, 202, 254));
		// 在圖片上寫隨機字串，並記錄生成的序列
		String randomText = drawRandomText((Graphics2D) graphics);
		// 存入session中
		session.setAttribute("checkcode", randomText);
		// 設置響應頭通知瀏覽器以圖片的形式打開
		response.setContentType("image/jpeg");
		// 設置響應頭控制瀏覽器不要緩存
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		// 將圖片寫給瀏覽器
		OutputStream output = response.getOutputStream();
		ImageIO.write(bufferedImage, "jpg", output);
	}

	/**
	 * 設置圖片背景色
	 * */
	private void setBackGround(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * 設置圖片邊框
	 * */
	private void setBordor(Graphics graphics) {
		graphics.setColor(Color.BLUE);
		graphics.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
	}

	/**
	 * 在圖片上畫干擾線
	 * */
	private void drawRandomLine(Graphics graphics, Color color) {
		graphics.setColor(color);
		// 設置線條個數並畫線
		for (int i = 0; i < 5; i++) {
			int x1 = new Random().nextInt(WIDTH);
			int x2 = new Random().nextInt(WIDTH);
			int y1 = new Random().nextInt(HEIGHT);
			int y2 = new Random().nextInt(HEIGHT);
			graphics.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * 在圖片上寫隨機字符，數字和字母的組合
	 * 
	 * @param length
	 *            字符串的长度
	 * 
	 * @return 返回生成的字符串序列
	 * */
	private String drawRandomText(Graphics2D graphics) {
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("宋体", Font.BOLD, 20));

		// 数字和字母的组合
		String baseNumLetter = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
		StringBuffer sBuffer = new StringBuffer();

		int x = 5; // 旋转原点的 x 坐标
		String ch = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			// 设置字体旋转角度
			int degree = random.nextInt() % 30; // 角度小于30度
			int dot = random.nextInt(baseNumLetter.length());
			ch = baseNumLetter.charAt(dot) + "";
			sBuffer.append(ch);

			// 正向旋转
			graphics.rotate(degree * Math.PI / 180, x, 20);
			graphics.drawString(ch, x, 20);

			// 反向旋转
			graphics.rotate(-degree * Math.PI / 180, x, 20);
			x += 30;
		}

		return sBuffer.toString();
	}
}
