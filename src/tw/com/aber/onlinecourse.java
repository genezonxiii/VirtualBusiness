
package tw.com.aber;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

@SuppressWarnings("serial")

public class onlinecourse extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String path = getServletConfig().getServletContext().getInitParameter("videopath");
		String pathurl = getServletConfig().getServletContext().getInitParameter("videourl");
		File fp=new File(path);
		Video video= new Video();
		
		String[] strs=fp.list(),strs2=fp.list();
		for(int i=0;strs!=null&&i<strs.length;i++){
			if(!(new File(fp+"/"+strs[i]).isDirectory())){
				strs[i]=pathurl+"/"+strs[i];
			}else{
				strs[i]="dir";
			}
		}
		for(int i=0;strs2!=null&&i<strs2.length;i++){
			if(!(new File(fp+"/"+strs2[i]).isDirectory())){
				strs2[i]=strs2[i].replaceAll(".mov","");
			}else{
				strs2[i]="dir";
			}
		}
		video.path=strs;
		video.name=strs2;
		Gson gson = new Gson();
		String jsonStrList = gson.toJson(video);
		//System.out.println(jsonStrList);
		response.getWriter().write(jsonStrList);
	}
	public class Video {
		public String[] name;
		public String[] path;
	}
}
