
<%if(com.io.TumblrServlet.getCookie(request, com.io.LoginServlet.cookieName)==null){
	response.sendRedirect("/Tumblr/login.html");
	return; 
}
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
html,body{
	margin:0px;
	width:100%;
	height:100%;
}
.container{
	width:100%;
	height:100%;
}
.top{
	
	background-color:grey;
	height:20%;
	width:100%;
}

.list{
	width:100%;
	height:80%;
}

.list ul{	
	width:50%;
	margin-left: 25%;
	text-decoration: none;
	list-style: none;
}

img{
	width:70%;
	height:65%;
	margin-left:15%;
}
</style>
</head>
<body>
<div class='container'>
	<div class='top'>
		<img src="C:\Users\Ahalya\Desktop\images.jpeg" style="width:40px;height:40px;" align="left">
		<form method=post onsubmit="return submitForm(this);" enctype="multipart/form-data" align="center">
		<input name=pic type=file><br>
		<input type=submit value="Submit">
		</form>
	</div>
	<div class='list'>
		<ul class='photo-list'>
		</ul>
	</div>
</div>
</body>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.2.1.min.js"></script>

<script type="text/javascript">
$.get('/Tumblr/tumblr/feed/<%=com.io.TumblrServlet.getCookie(request,com.io.LoginServlet.cookieName)%>',function(data){
	jsonarr = eval(data);
	for(i in jsonarr){
		$('.photo-list').append("<li>"+jsonarr[i].name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+jsonarr[i].time+"</li>");
		$('.photo-list').append("<li><img src='"+jsonarr[i].url+"'/></li>");
	}
});
function  submitForm(f){
	var data = new FormData();
	/*jQuery.each(f.file.files, function(i, file) {
	    data.append('file-'+i, file);
	});*/
	data.append('file-0',f.pic.files[0]);
	jQuery.ajax({
	    url: 'tumblr/1',
	    data: data,
	    cache: false,
	    contentType: false,
	    processData: false,
	    type: 'POST',
	    success: function(data){
	        alert(data);
	    }
	});

}
</script>
</html>