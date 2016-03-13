package com.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public  class LoginServlet extends HttpServlet{
	public static String cookieName="auth-cookie";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uid1=req.getParameter("uname");
		String pass1=req.getParameter("pass").toString().trim();//trim to remove extra spaces at end
		String uid="";
		try{
		Connection con=TumblrServlet.util();
		Connection con1=TumblrServlet.util();
		PreparedStatement ps1=con1.prepareStatement("select * from user where name=? and pass=MD5(?)");
		ps1.setString(1, uid1);
		ps1.setString(2,pass1);
		ResultSet rs= ps1.executeQuery();
		if(rs.next()){
			uid=rs.getString(1);
			Cookie coo=new Cookie(cookieName,uid);
			resp.addCookie(coo);
			resp.sendRedirect("/Tumblr/tumblr.jsp");
		}else{
			resp.sendRedirect("/Tumblr/login.html");
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}
}
