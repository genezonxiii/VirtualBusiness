package tw.com.aber.report;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")

public class shipreport extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("group_id") == null) {
			response.getWriter().write("no_session!");
			return;
		}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id = (group_id == null || group_id.length() < 3) ? "UNKNOWN" : group_id;

		String time1 = (request.getParameter("time1") == null) ? ""
				: request.getParameter("time1").replaceAll("/", "-");
		time1 = (time1.length() < 3) ? "2016-06-01" : time1;
		String time2 = (request.getParameter("time2") == null) ? ""
				: request.getParameter("time2").replaceAll("/", "-");
		time2 = (time2.length() < 3) ? "2300-12-31" : time2;

		String conString = getServletConfig().getServletContext().getInitParameter("pythonwebservice")
				+ "/ship/groupid=" + new String(Base64.encodeBase64String(group_id.getBytes())) + "&strdate="
				+ new String(Base64.encodeBase64String(time1.getBytes())) + "&enddate="
				+ new String(Base64.encodeBase64String(time2.getBytes()));
		if ("today".equals(action)) {
			conString = getServletConfig().getServletContext().getInitParameter("pythonwebservice") + "/ship/groupid="
					+ new String(Base64.encodeBase64String(group_id.getBytes())) + "&strdate="
					+ new String(Base64
							.encodeBase64String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes()))
					+ "&enddate=" + new String(Base64
							.encodeBase64String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes()));
		}
		//System.out.println("conString " + conString);
		HttpClient client = new HttpClient();
		HttpMethod method;
		try {
			method = new GetMethod(conString);
			client.executeMethod(method);
		} catch (Exception e) {
			response.getWriter().write("WebService Error for:" + e);
			return;
		}
		StringWriter writer = new StringWriter();
		IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
		String ret = writer.toString().replaceAll("null", "\"\"");
		response.getWriter().write(ret);
		method.releaseConnection();
		return;
	}
}
