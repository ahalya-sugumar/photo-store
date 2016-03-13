package com.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		String pass=req.getParameter("pass").toString();
		String email=req.getParameter("email");
		String gender=req.getParameter("sex");
		if("F".equals(gender))gender="Female";
		else gender="Male";
		try{
			Connection con=TumblrServlet.util();
			PreparedStatement ps=con.prepareStatement("insert into user (name,pass,email,gender) values(?,MD5(?),?,?)");
			ps.setString(1, name);
			ps.setString(2, pass);
			ps.setString(3, email);
			ps.setString(4, gender);
			ps.executeUpdate();
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
