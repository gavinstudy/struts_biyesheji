package com.struts.action;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sevenEleven.javaBean.CcheckAUser;
import com.sevenEleven.javaBean.exam.CTakeExam;
import com.sevenEleven.javaBean.student.CStudentMethod;
import com.struts2.model.CstudentBean;

public class StudentAction extends ActionSupport implements SessionAware,ServletRequestAware{
	
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private HttpServletRequest request;
	
	private String number;
	private String password;
	
	CstudentBean student = new CstudentBean();
	CcheckAUser check = new CcheckAUser();
	
	public String log() throws SQLException {
			student = check.checkStudentLogin(number,password);
			if (student != null) {
				session.put("student", student);
				return SUCCESS; 
			} else {
				return "error";
			}
	}
	private String newpwd;
	
	public String show(){	
		CStudentMethod usmethod=new CStudentMethod();
		Object id = ((CstudentBean) session.get("student")).getS_number();
		//student = usmethod.getinformation(userid);

		List list = usmethod.getNestedListFromResultSet(id);
		request.setAttribute("info", list);
		return SUCCESS;
	}
	
	public String update(){
		
		CstudentBean student = new CstudentBean();
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		CStudentMethod usmethod=new CStudentMethod();
		student=usmethod.getinformation(userid);
		
	          
		 String c_name= usmethod.getinformation(userid).getC_name();
		 Object c_id= usmethod.getinformation(userid).getC_id();
		 String pwd=  usmethod.getinformation(userid).getS_password();
		 String c_type=  usmethod.getinformation(userid).getC_type();
		 String s_num= usmethod.getinformation(userid).getS_number();
		 request.setAttribute("c_name",c_name);
		 request.setAttribute("c_id",c_id);
		 request.setAttribute("pwd",pwd);
		 request.setAttribute("c_type",c_type);
		 request.setAttribute("s_num",s_num);
		 
		 return SUCCESS;
	} 
	public String successUpdate(){
		
		CstudentBean student = new CstudentBean();
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		CStudentMethod usmethod=new CStudentMethod();
		student=usmethod.getinformation(userid);
		
	          
		 String c_name= usmethod.getinformation(userid).getC_name();
		 Object c_id= usmethod.getinformation(userid).getC_id();
		 String pwd=  usmethod.getinformation(userid).getS_password();
		 String c_type=  usmethod.getinformation(userid).getC_type();
		 String s_num= usmethod.getinformation(userid).getS_number();
		 request.setAttribute("c_name",c_name);
		 request.setAttribute("c_id",c_id);
		 request.setAttribute("pwd",pwd);
		 request.setAttribute("c_type",c_type);
		 request.setAttribute("s_num",s_num);
		
		
		String success;
		String page;
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		String checkpwd = request.getParameter("checkpwd");

		String oldpassword=usmethod.getinformation(userid).getS_password();
		if (oldpwd.equalsIgnoreCase(oldpassword)&& newpwd.equalsIgnoreCase(checkpwd)) 
		{
			if (usmethod.Update(userid, newpwd)) 
			{   success="修改成功!";
			   page="success.jsp";
				request.setAttribute("successMSG",success);
			    request.setAttribute("location",page);
				return SUCCESS;
			}
			
		}

		return "";
	}
	
	public String courseTable(){
		CStudentMethod usmethod=new CStudentMethod();
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		Object bb=usmethod.getinformation(userid).getC_id();
		List list=usmethod.getLesson1Info(bb);
		request.setAttribute("lesson1",list);
		return SUCCESS;
	}
	
	public String personalXuanXiu(){
		CStudentMethod usmethod=new CStudentMethod();
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		List list=usmethod.GetPersonalL2Info(userid);
		request.setAttribute("info",list);
		return SUCCESS;
	}
	
	public String bixiuChengJi(){
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		CStudentMethod usmethod=new CStudentMethod();
		List list=usmethod.getResult1(userid);
		request.setAttribute("result_1",list);
	
		return SUCCESS;
	}
	
	public String xuanxiuChengJi(){
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		CStudentMethod usmethod=new CStudentMethod();
		List list=usmethod.getResult2(userid);
		request.setAttribute("result_2",list);
		return SUCCESS;
	}
	
	public String xuanbaoCourse(){
		CStudentMethod usmethod=new CStudentMethod();
		 List list=usmethod.getLesson2Info();
		 List listid=usmethod.getL2Id();
		request.setAttribute("lesson2",list);
		request.setAttribute("l2id",listid);
		return SUCCESS;
	}
	
	public String tijiaoXuanXiuCourse(){
		Object crid = null;
		Object tid = null;
		Object timeid = null;
		String error;
		int judge;
		int aa;
		String success;
		String page;
		Object userid = ((CstudentBean) session.get("student")).getS_number();
		String[] array = request.getParameterValues("checkbox");
		CStudentMethod usmethod = new CStudentMethod();
		Object sid = usmethod.getinformation(userid).getS_id();
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				Object l2Id = (Object) array[i];
				List list = usmethod.getPersonall2Id(l2Id);
				Iterator it = list.iterator();
				List listColumn = new LinkedList();
				while (it.hasNext()) {
					listColumn = (List) it.next();
					crid = listColumn.get(0);
					tid = listColumn.get(1);
					timeid = listColumn.get(3);
				}
				if (array.length > 1) {
					error = "不能同时选报超过1门课";
					request.setAttribute("errorMSG", error);
					return "error";
				}

				usmethod.PersonalL2Info(sid, crid, tid, l2Id, timeid);
		}
			success="提交成功!";
			page="info_student.jsp";
			request.setAttribute("successMSG",success);
			request.setAttribute("location",page);
			return SUCCESS;
			
		}
		if (array == null) {
			error = "请选定课程!";
			request.setAttribute("errorMSG", error);
		return "error";
		}
		return null;
		
	}
	
	public String bixiuKaoShi(){
		CTakeExam takeExam = new CTakeExam();
		CstudentBean student = new CstudentBean();
		try {
			request.setCharacterEncoding("gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//取得session中student的信息
		//得到student的必修课程
		//得到student的所在班级名
		student = (CstudentBean) session.get("student");

		List courseList = takeExam.getStudentComperationCourse(String.valueOf(student.getC_id()),String.valueOf(student.getS_id()));
		String className = takeExam.getStudentClass(String.valueOf(student.getC_id()));
		request.setAttribute("courseList",courseList);
		
		request.setAttribute("className",className);
		return SUCCESS;
	}
	
	public String xuanxiuKaoShi(){
		CTakeExam takeExam = new CTakeExam();
		CstudentBean student = new CstudentBean();
		try {
			request.setCharacterEncoding("gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//取得session中student的信息
		//得到student的必修课程
		//得到student的所在班级名qq
		student = (CstudentBean) session.get("student");

		List courseList = takeExam.getStudentElectiveCourse(String.valueOf(student.getS_id()));
		String className = takeExam.getStudentClass(String.valueOf(student.getC_id()));
		request.setAttribute("courseList",courseList);
		
		request.setAttribute("className",className);
		return SUCCESS;
	}

	
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewpwd() {
		return newpwd;
	}
	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
	}
	
	
	

}
