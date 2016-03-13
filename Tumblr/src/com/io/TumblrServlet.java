package com.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONArray;
import org.json.JSONObject;


/*
 * 
 * Followers (userid, follower)
 * 2,3
 * 3,2
 * select userid where follower  = myid;
 * 1,2,3
 * 
 */
public class TumblrServlet extends HttpServlet{
	public static String getCookie(HttpServletRequest req, String cname){
		if(req!=null && req.getCookies()!=null ){
		
		for(Cookie c : req.getCookies()){
			if(c.getName().equals(cname)){
				return c.getValue();
			}
				
		}}
		return null;
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Cookie ck[]=req.getCookies();
			String uids = "";
			int i=0;
			String ruri = req.getRequestURI();
			if (ruri.contains("/feed")) {
				JSONArray arr = new JSONArray();

				int y = Integer.parseInt(req.getRequestURI().split("/")[4]);
				Connection con = util();
				PreparedStatement ps1=con.prepareStatement("select followid from follow where uid=?");
				ps1.setInt(1,y);
				ResultSet rs1=ps1.executeQuery();
				while(rs1.next())
					uids +=(rs1.getString(1)+",");
				uids = uids+getCookie(req, LoginServlet.cookieName);//uids.substring(0,uids.length()-1);
				PreparedStatement ps = con.prepareStatement("select photos.pid,user.name,photos.time from photos left join user on photos.uid=user.uid where photos.uid in ("+uids+") order by photos.time desc");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject photo = new JSONObject();
					photo.put("url", "/Tumblr/tumblr/"+rs.getInt(1));
					photo.put("name",rs.getString(2));
					photo.put("time",rs.getTimestamp(3));
					arr.put(photo);
				}
				resp.getWriter().print(arr.toString());
			} else {
				Connection con = util();
				PreparedStatement ps = con.prepareStatement("select photo from photos where pid=?");
				int x = Integer.parseInt(req.getRequestURI().split("/")[3]);
				ps.setInt(1, x);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					byte buf[] = null;
					buf = rs.getBytes(1);
					resp.getOutputStream().write(buf);
					
				}
			}
		}

		catch (Exception e) {
e.printStackTrace();
		}
	}
		
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			byte buf[]=null;
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			if(ServletFileUpload.isMultipartContent(req)){
				String uid = "";
				FileUpload fu = new FileUpload();
				fu.setFileItemFactory(new DiskFileItemFactory());
				fu.setFileSizeMax(Long.MAX_VALUE);
				List<FileItem> files = fu.parseRequest(new ServletRequestContext(req));
				for(FileItem f : files){
					if(!f.isFormField()){
						buf = f.get();
						bo.write(buf, 0, buf.length);			
					}
				}
				uid = getCookie(req,LoginServlet.cookieName);
			Connection con=util();
			
		int x=Integer.parseInt(uid);
		
		PreparedStatement ps=con.prepareStatement("insert into photos (uid,photo)values(?,?)");
		ps.setInt(1,x);
		ps.setBytes(2,buf);
		ps.executeUpdate();
			}
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	public static Connection util(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/tumblr?user=root");
			return con;
		}catch(Exception e){
				System.out.println(e);
				return null;
		}
	}
}
