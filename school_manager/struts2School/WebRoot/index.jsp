<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page language="java" import="com.sevenEleven.javaBean.Count" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script type="text/javascript" language="javascript" src="res/js/index.js" charset="utf-8"></script>
<HTML>

<HEAD>

<base href="<%=basePath%>">

<TITLE>欢迎光临理学院教学管理信息系统-用户登录</TITLE>
<META http-equiv=Content-Language content=zh-cn>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<LINK href="。/res/css/index.css" rel=stylesheet>
<META content="MSHTML 6.00.2800.1106" name=GENERATOR>

<script type="text/javascript">

	function myButton(){
		document.form.action=document.form.userType.options[document.form.userType.selectedIndex].value;
		document.form.submit;
	}
</script>


</HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0>
<TABLE cellSpacing=0 cellPadding=0 width=778 align=center border=0>
	<TBODY>
		<TR>
			<TD vAlign=top background=res/images/index_bg.gif height=400>
			<!------------------------------------ 监听器 ----------------------------------->

			<!------------------------------------ login 表单 ----------------------------------->
			<FORM name=form action=CcheckUserServlet method=post>
			<TABLE cellSpacing=0 cellPadding=0 width="90%" align=center border=0>
				<TBODY>
					<TR>
						<TD colSpan=2 height=156>
						<DIV align=center>
						<P>&nbsp;</P>
						<P><IMG height=52 src="res/images/title_SUES.gif" width=400></P>
						</DIV>
						</TD>
					</TR>
					<TR>
						<TD align=left width=478>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 在线人数:<%=Count.getOnline()%>，额定数：100</TD>
						<TD width=222><FONT size=2>你是第<FONT color=#ff0000><B>28</B></FONT>位登录本系统的用户</FONT>
						</TD>
					</TR>
						
					<TR>
						<TD align=right colSpan=3 height=40><FONT color=#ff0000 size=2></FONT>&nbsp;</TD>
					</TR>
					<TR>
						<TD align=right colSpan=2><FONT color=blue></FONT><A
							href="http://www.i2030.org"><FONT color=red></FONT></A></TD>
					</TR>
					<TR>
						<TD align=right colSpan=2></TD>
					</TR>
					<TR>
						<TD align=right width=478 height=8><FONT
							face="Verdana, Arial, Helvetica, sans-serif" size=-1>用户名&nbsp;</FONT>
						</TD>
						<TD>&nbsp; 
						<input id="userName" class="INPUT_text" name=number
							value=""></TD>
					</TR>
					<TR>
						<TD colSpan=2>&nbsp;</TD>
					</TR>
					<TR>
						<TD align=right width=478 height=8><FONT
							face="Verdana, Arial, Helvetica, sans-serif" size=-1>密码&nbsp;</FONT>
						</TD>
						<TD>&nbsp; 
						<INPUT id="userPassword" class="INPUT_text" type=password
							name=password value=""></TD>
					</TR>
					<TR>
						<TD colSpan=2>&nbsp;</TD>
					</TR>
					<TR>
						<TD align=right width=478 height=8><FONT
							face="Verdana, Arial, Helvetica, sans-serif" size=-1>登录类型&nbsp;</FONT>
						</TD >
						<TD >&nbsp; <select class="INPUT_text" name="userType">
							<option value="user/Manager_log">管理员</option>
							<option value="user/Teacher_log">老师</option>
							<option value="user/Student_log">学生</option>
						</select></TD>
					</TR>
					<TR>
					<TR>
						<TD align=right width=478 height=8><FONT
							face="Verdana, Arial, Helvetica, sans-serif" size=-1></FONT></TD>
						<td align="center">&nbsp;</td>
					<TR>
						<TD>&nbsp;</TD>
						<TD>&nbsp;<button onClick="myButton()">登录</button>
						</TD>
					</TR>
					<TR>
						<TD colSpan=8 height=100>&nbsp;</TD>
					</TR>
					<TR>
						<TD colSpan=6 height=90>&nbsp;</TD>
					</TR>
					<TR>
						<TD>&nbsp;</TD>
						<TD>&nbsp;<FONT color=red size=2></FONT></TD>
					</TR>
				</TBODY>
			</TABLE>
			</FORM>
		</TBODY>
	</TABLE>
		<center>
			<p CLASS="first"><font color="#800080" size="2">程序调试通过by:双鱼林&nbsp;QQ:287307421&nbsp;手机:13558690869&nbsp;更多系统请到双鱼林的淘宝店:<a href='http://shop34864101.taobao.com' target='_blank'>http://shop34864101.taobao.com</a>查询</font></p>
			<p CLASS="second">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font color="#800080" size="2">测试环境为:windows xp+jdk1.5+Tomcat6.0+MyEclipse5.5+SQLServer2000</font></p>
		</center>
</BODY>
</HTML>


