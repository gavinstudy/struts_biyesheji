package com.struts.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sevenEleven.javaBean.CcheckAUser;
import com.sevenEleven.javaBean.Page;
import com.sevenEleven.javaBean.exam.Cexam;
import com.sevenEleven.javaBean.teacher.DBO;
import com.sevenEleven.javaBean.teacher.Doing;
import com.sevenEleven.javaBean.teacher.Typer;
import com.struts2.model.CchooseTestBean;
import com.struts2.model.CteacherBean;
import com.struts2.model.CtextTestBean;

public class TeacherAction extends ActionSupport implements SessionAware,ServletRequestAware{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private HttpServletRequest request;

	CcheckAUser check = new CcheckAUser();
	
	private String number;
	private String password;
	
	public String log() throws SQLException {
		CteacherBean teacher = check.checkTeacherLogin(number, password);
		if (teacher != null) {
			session.put("teacher", teacher);
			request.getSession().setAttribute("teacher", teacher);	
			return SUCCESS; 
		} else {
			return "error";
		}
	}
	
	private String teacherNAME;
	private String teacherPWD;
	
	public String updateSuccess(){
	
		String userID=String.valueOf(((CteacherBean) session.get("teacher")).getT_id());	

		if (userID==null||userID==""){
			request.setAttribute("errorMSG","没有登陆或登陆已超时");
			return "error";
		}
		if (teacherNAME==null||teacherPWD==null||teacherNAME==""||teacherPWD==""){
			request.setAttribute("errorMSG","用户名密码不能为空");
			return "error";
		}
		Doing DB=DBO.DBODoing();
		if (DB.UpdateTeacherInfo(userID,teacherNAME,teacherPWD)){
			DB.Close();
			request.setAttribute("successMSG","更新资料成功");
			request.setAttribute("location","Teacher_log_success.jsp");
			return SUCCESS;
		}else{
			DB.Close();
			request.setAttribute("errorMSG","更新资料失败,确认数据是否合法");
			return "error";
		}
	}
	
	private String s_id;
	
	public String stuResult(){
		String teacherID=String.valueOf(((CteacherBean) session.get("teacher")).getT_id());
		if (teacherID==null||teacherID==""){
			request.setAttribute("errorMSG","没有登陆或登陆已超时");
			return "error";
		}
		if (s_id==null||s_id==""||!Typer.IsInter(s_id)){
			request.setAttribute("errorMSG","数据不合法");
			return "error";
		}
		Doing DB=DBO.DBODoing();
		if (DB.IsTeacherStu(teacherID,s_id)){
			request.setAttribute("StudentResult",DB.GetStuResult(s_id,teacherID));
			DB.Close();
			return SUCCESS;
			
		}else{
			request.setAttribute("errorMSG","此学生不是你的学生");
		DB.Close();
			return "error";
		}
	}
	
	public String addNorScore(){
		String teacherID=String.valueOf(((CteacherBean) session.get("teacher")).getT_id());
		if (teacherID==null||teacherID==""){
			request.setAttribute("errorMSG","没有登陆或登陆已超时");
			return "error";
		}
		String UpdateScore=request.getParameter("stuScore");
		int iUpdateScore = Integer.parseInt(UpdateScore);
		
		String score = request.getParameter("Scored");
		int iScore = Integer.parseInt(score);
		
		String scorePersent = request.getParameter("scorePersent");
		if(scorePersent == null || scorePersent == ""){
			request.setAttribute("errorMSG","没有给出下时分的百分比!!");
			return "error";
		}
		int iScorePersent = Integer.parseInt(scorePersent);
		
		 //由平时分和试面分得到总分
		iScore = iScore * (100 - iScorePersent)/100 + iUpdateScore * iScorePersent/100;
		score = String.valueOf(iScore);
		//score = Integer.parseInt(score)*(100-Integer.parseInt(scorePersent))/100 + Integer.parseInt(UpdateScore) * Integer.parseInt(scorePersent)/100;  
		String s_id=request.getParameter("s_id");
		int x = 100-Integer.valueOf("12");
		if (UpdateScore==null||s_id==null){
			request.setAttribute("errorMSG","数据非法");
			return "error";
		}
		Doing DB=DBO.DBODoing();
		if (!DB.IsTeacherStu(teacherID,s_id)){
			request.setAttribute("errorMSG","此学生不是你的学生,无权修改");
			return "error";
		}
		if (DB.AddStudentScore(teacherID,s_id,score,UpdateScore)){
			request.setAttribute("successMSG","更新学生成绩成功");
			request.setAttribute("location","StuResult?s_id="+s_id);
			return "indexTeacher";
		}else{
			request.setAttribute("errorMSG","更新失败,联系系统管理员");
			return "error";
		}
	}
	
	public String addtestServlet(){
		 CteacherBean teacher = new CteacherBean();
		 Cexam exam = new Cexam();
		 teacher = (CteacherBean)session.get("teacher");	
		 List courseList = exam.findLesson1(teacher.getT_number());
		 request.setAttribute("courseList", courseList);
		 return SUCCESS;
	}
	
	public String uploadTestServlet(){
		
		Cexam exam = new Cexam();
		CchooseTestBean choose = new CchooseTestBean();
		CtextTestBean text = new CtextTestBean();
		String isRight = new String();
		// CchooseTestBean
		int testType = Integer.parseInt(request.getParameter("testType"), 10);
		if (testType == 1) { //必修课单选题的操作
			//choose.setCh_id("seq_choose1_p.nextval");
			choose
					.setCh_question((String) request
							.getParameter("choose1_head"));
			choose.setKeya((String) request.getParameter("choose1_Keya"));
			choose.setKeyb((String) request.getParameter("choose1_Keyb"));
			choose.setKeyc((String) request.getParameter("choose1_Keyc"));
			choose.setKeyd((String) request.getParameter("choose1_Keyd"));
			// 得到课程的ID
			String couse = request.getParameter("course");
			int L1_id = exam.getLesson1Id(couse);
			choose.setL_id(L1_id);
			choose.setType(testType);
			//
			choose.setAnswer(request.getParameter("Key1"));
			if (exam.insertIntoChoose1Table(choose))
				isRight = "yes";
			else
				isRight = "no";
		} else if (testType == 2) {//必修课多选题的操作
			//choose.setCh_id("seq_choose1_p.nextval");
			choose
					.setCh_question((String) request
							.getParameter("choose2_head"));
			choose.setKeya((String) request.getParameter("choose2_Keya"));
			choose.setKeyb((String) request.getParameter("choose2_Keyb"));
			choose.setKeyc((String) request.getParameter("choose2_Keyc"));
			choose.setKeyd((String) request.getParameter("choose2_Keyd"));
			// 得到课程的ID
			String couse = request.getParameter("course");
			int L1_id = exam.getLesson1Id(couse);
			choose.setL_id(L1_id);
			choose.setType(testType);
			//
			choose.setType(testType);
			// check的用法
			String answer = "";
			String[] checks = request.getParameterValues("Key2");
			for (int i = 0; i < checks.length; i++) {
				answer += checks[i];
			}
			choose.setAnswer(answer);
			if (exam.insertIntoChoose1Table(choose))
				isRight = "yes";
			else
				isRight = "no";
		} else if (testType == 3) {//必修课填空题的操作
			//text.setText_id("seq_text1_p.nextval");
//			 得到课程的ID
			String couse = request.getParameter("course");
			int L1_id = exam.getLesson1Id(couse);
			text.setL_id(L1_id);
			//
			text.setText_question(request.getParameter("text_head"));
			text.setText_answer(request.getParameter("Key"));
			if (exam.insertIntoText1Table(text))
				isRight = "yes";
			else
				isRight = "no";
		} else {
			isRight = "no";
		}
		 //request.setAttribute("isRight", isRight);
		return SUCCESS;
	}
	
	public String updateOrDeleteTestServlet(){
		Cexam exam = new Cexam();
		Page choose1Page = new Page();
		List choose1List = null;
		int maxChoose1RowCount = exam.getLengthOfChoose1Table();// choose1表共有多少行
		choose1Page.setCurPage(1); // 为course1Page这个Bean设置当前页为1
		choose1Page.setRowsPerPage(15); // 为course1Page这个Bean设置每页为15条数据.
		choose1Page.setFormName("choose1Form"); // 为course1Page这个Bean设置FormName
		choose1Page.setMaxRowCount(maxChoose1RowCount); // 为course1Page这个Bean设置记录中共有多少行.
		
		if (maxChoose1RowCount % 15 == 0) {// 为course1Page这个Bean设置最大页数.
			choose1Page.setMaxPage(maxChoose1RowCount / 15 );//当总页数能整除每页数据条数时
		} else {
			choose1Page.setMaxPage(maxChoose1RowCount / 15 + 1);//否则
		}
		choose1Page.setTarget("CflushExam_updateOrDeleteTestServlet");// 为choose1Page这个Bean要跳转的servlet,就是本页.
		choose1List = exam.getChoose1Table(1, 15); // 取出初使化的当前页面要显示的记录.
		request.getSession().setAttribute("choose1Page", choose1Page);
		request.setAttribute("choose1List", choose1List);
		request.setAttribute("choose1ToString",choose1Page.toString());
		request.setAttribute("choose1PageStr", choose1Page.getPageStr());
		//选修选择题
		int maxChoose2RowCount = exam.getLengthOfChoose2Table();
		List choose2List = exam.getChoose2Table(1,maxChoose2RowCount);
		request.setAttribute("choose2List", choose2List);
		//必修填空题
		int maxText1RowCount = exam.getLengthOfText1Table();
		List text1List = exam.getText1Table(1,maxText1RowCount);
		request.setAttribute("text1List", text1List);
		//选修填空题
		int maxText2RowCount = exam.getLengthOfText2Table();
		List text2List = exam.getText2Table(1,maxText2RowCount);
		request.setAttribute("text2List", text2List);
		return SUCCESS;
	}
	
	public String addtest2Servlet(){
		 CteacherBean teacher = new CteacherBean();
		 Cexam exam = new Cexam();
		 teacher = (CteacherBean)session.get("teacher");
		 List courseList = exam.findLesson2(teacher.getT_number());
		 request.setAttribute("courseList", courseList);
		 return SUCCESS;
	}
	
	public String CflushExamUpdateOrDeleteTestServlet(){
		Cexam exam = new Cexam();
		String[] curPageArray = request.getParameterValues("pageSize");
//		 改变当前页,显示当前页
		//必修课的选择题
	
		List choose1List = null;
		 Page choose1Page = (Page) request.getSession().getAttribute("choose1Page");
		 
		int curPage = Integer.parseInt(curPageArray[0]);
		choose1Page.setCurPage(curPage);
		if (curPage < choose1Page.getMaxPage()) {
			choose1List = exam.getChoose1Table((curPage - 1)//取出当前页面要显示的记录.
					* choose1Page.getRowsPerPage() + 1, curPage
					* choose1Page.getRowsPerPage());
		} else {
			choose1List = exam.getChoose1Table((curPage - 1)
					* choose1Page.getRowsPerPage() + 1, choose1Page//取出最后一页页面要显示的记录.
					.getMaxRowCount());
		}
		request.setAttribute("choose1PageStr",choose1Page.getPageStr());
		request.setAttribute("choose1PageToString",choose1Page.toString());
		request.setAttribute("choose1List",choose1List);
		
		//选修选择题
		int maxChoose2RowCount = exam.getLengthOfChoose2Table();
		List choose2List = exam.getChoose2Table(1,maxChoose2RowCount);
		request.setAttribute("choose2List", choose2List);
		//必修填空题
		int maxText1RowCount = exam.getLengthOfText1Table();
		List text1List = exam.getText1Table(1,maxText1RowCount);
		request.setAttribute("text1List", text1List);
		//选修填空题
		int maxText2RowCount = exam.getLengthOfText2Table();
		List text2List = exam.getText2Table(1,maxText2RowCount);
		request.setAttribute("text2List", text2List);
		
		return SUCCESS;
	}
	
	public String excamUploadOrDeleteTestServlet(){
		Cexam exam = new Cexam();
		
		// 这个if语句是对页面exam_updateOrDeleteTest.jsp的查看按钮的响应
		if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("mustChoose")) {
			CchooseTestBean choose = exam.getChoose1Table(request
					.getParameter("view"));
			if (choose.getType() == 1) {
				request.setAttribute("type", "必修课单选题");
				request.setAttribute("choose1", choose);
				return "exam_newPageUpdateChoose1";
			} else {
				request.setAttribute("type", "必修课多选题");
				request.setAttribute("choose2", choose);
				return "exam_newPageUpdateChoose2";
			}
		} else if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("easyChoose")) {
			CchooseTestBean choose = exam.getChoose2Table(request
					.getParameter("view"));
			if (choose.getType() == 1) {
				request.setAttribute("type", "选修课单选题");
				request.setAttribute("choose1", choose);
				return "exam_newPageUpdateChoose1";
			} else {
				request.setAttribute("type", "选修课多选题");
				request.setAttribute("choose2", choose);
				return "exam_newPageUpdateChoose2";
			}
		} else if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("mustText")) {
			CtextTestBean text = exam.getText1Table(request
					.getParameter("view"));
			request.setAttribute("type", "必修课填空题");
			request.setAttribute("text", text);
			return "exam_newPageUpdateText";
		} else if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("easyText")) {
			CtextTestBean text = exam.getText2Table(request
					.getParameter("view"));
			request.setAttribute("type", "选修课填空题");
			request.setAttribute("text", text);
			return "exam_newPageUpdateText";

		}
		// 这个if语句是对页面exam_updateOrDeleteTest.jsp的删除按钮的响应

		if (request.getParameter("dType") != null
				&& request.getParameter("dType").equals("mustChoose")) {
			String x = request.getParameter("delete");
			exam.deleteItemOfChoose1Table(x);
			return SUCCESS;
		} else if (request.getParameter("dType") != null
				&& request.getParameter("dType").equals("easyChoose")) {
			exam.deleteItemOfChoose2Table(request.getParameter("delete"));
			return SUCCESS;
		} else if (request.getParameter("dType") != null
				&& request.getParameter("dType").equals("mustText")) {
			exam.deleteItemOfText1Table(request.getParameter("delete"));
			return SUCCESS;
		} else if (request.getParameter("dType") != null
				&& request.getParameter("dType").equals("easyText")) {
			exam.deleteItemOfText2Table(request.getParameter("delete"));
			return SUCCESS;
		}
		return SUCCESS;
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

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
	}

	public String getTeacherNAME() {
		return teacherNAME;
	}

	public void setTeacherNAME(String teacherNAME) {
		this.teacherNAME = teacherNAME;
	}

	public String getTeacherPWD() {
		return teacherPWD;
	}

	public void setTeacherPWD(String teacherPWD) {
		this.teacherPWD = teacherPWD;
	}

	public String getS_id() {
		return s_id;
	}

	public void setS_id(String s_id) {
		this.s_id = s_id;
	}
	
	
}
