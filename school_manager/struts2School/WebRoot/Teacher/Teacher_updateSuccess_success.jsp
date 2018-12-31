<%@ page language="java" contentType="text/html; charset=GB2312"
    pageEncoding="GB2312"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<LINK href="../res/css/main.css" rel=stylesheet>
<title>成功消息</title>
<%String location=(String)request.getAttribute("location");
%>

</head>
<body>
<div align=center>
<% if(request.getSession().getAttribute("successMSG")==null){
%>
	<font color="red">${teacher.t_name}</font>密码更改成功，
<% 	
}else{ %>
<%=request.getSession().getAttribute("successMSG")%>
<%} %>
<%if (location!=null){ %>
<a href="../Teacher/info_teacher.jsp">确定</a>
<%}%>
</div>
</body>
</html>