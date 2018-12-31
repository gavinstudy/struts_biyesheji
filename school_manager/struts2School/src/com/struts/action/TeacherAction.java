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
			request.setAttribute("errorMSG","û�е�½���½�ѳ�ʱ");
			return "error";
		}
		if (teacherNAME==null||teacherPWD==null||teacherNAME==""||teacherPWD==""){
			request.setAttribute("errorMSG","�û������벻��Ϊ��");
			return "error";
		}
		Doing DB=DBO.DBODoing();
		if (DB.UpdateTeacherInfo(userID,teacherNAME,teacherPWD)){
			DB.Close();
			request.setAttribute("successMSG","�������ϳɹ�");
			request.setAttribute("location","Teacher_log_success.jsp");
			return SUCCESS;
		}else{
			DB.Close();
			request.setAttribute("errorMSG","��������ʧ��,ȷ�������Ƿ�Ϸ�");
			return "error";
		}
	}
	
	private String s_id;
	
	public String stuResult(){
		String teacherID=String.valueOf(((CteacherBean) session.get("teacher")).getT_id());
		if (teacherID==null||teacherID==""){
			request.setAttribute("errorMSG","û�е�½���½�ѳ�ʱ");
			return "error";
		}
		if (s_id==null||s_id==""||!Typer.IsInter(s_id)){
			request.setAttribute("errorMSG","���ݲ��Ϸ�");
			return "error";
		}
		Doing DB=DBO.DBODoing();
		if (DB.IsTeacherStu(teacherID,s_id)){
			request.setAttribute("StudentResult",DB.GetStuResult(s_id,teacherID));
			DB.Close();
			return SUCCESS;
			
		}else{
			request.setAttribute("errorMSG","��ѧ���������ѧ��");
		DB.Close();
			return "error";
		}
	}
	
	public String addNorScore(){
		String teacherID=String.valueOf(((CteacherBean) session.get("teacher")).getT_id());
		if (teacherID==null||teacherID==""){
			request.setAttribute("errorMSG","û�е�½���½�ѳ�ʱ");
			return "error";
		}
		String UpdateScore=request.getParameter("stuScore");
		int iUpdateScore = Integer.parseInt(UpdateScore);
		
		String score = request.getParameter("Scored");
		int iScore = Integer.parseInt(score);
		
		String scorePersent = request.getParameter("scorePersent");
		if(scorePersent == null || scorePersent == ""){
			request.setAttribute("errorMSG","û�и�����ʱ�ֵİٷֱ�!!");
			return "error";
		}
		int iScorePersent = Integer.parseInt(scorePersent);
		
		 //��ƽʱ�ֺ�����ֵõ��ܷ�
		iScore = iScore * (100 - iScorePersent)/100 + iUpdateScore * iScorePersent/100;
		score = String.valueOf(iScore);
		//score = Integer.parseInt(score)*(100-Integer.parseInt(scorePersent))/100 + Integer.parseInt(UpdateScore) * Integer.parseInt(scorePersent)/100;  
		String s_id=request.getParameter("s_id");
		int x = 100-Integer.valueOf("12");
		if (UpdateScore==null||s_id==null){
			request.setAttribute("errorMSG","���ݷǷ�");
			return "error";
		}
		Doing DB=DBO.DBODoing();
		if (!DB.IsTeacherStu(teacherID,s_id)){
			request.setAttribute("errorMSG","��ѧ���������ѧ��,��Ȩ�޸�");
			return "error";
		}
		if (DB.AddStudentScore(teacherID,s_id,score,UpdateScore)){
			request.setAttribute("successMSG","����ѧ���ɼ��ɹ�");
			request.setAttribute("location","StuResult?s_id="+s_id);
			return "indexTeacher";
		}else{
			request.setAttribute("errorMSG","����ʧ��,��ϵϵͳ����Ա");
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
		if (testType == 1) { //���޿ε�ѡ��Ĳ���
			//choose.setCh_id("seq_choose1_p.nextval");
			choose
					.setCh_question((String) request
							.getParameter("choose1_head"));
			choose.setKeya((String) request.getParameter("choose1_Keya"));
			choose.setKeyb((String) request.getParameter("choose1_Keyb"));
			choose.setKeyc((String) request.getParameter("choose1_Keyc"));
			choose.setKeyd((String) request.getParameter("choose1_Keyd"));
			// �õ��γ̵�ID
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
		} else if (testType == 2) {//���޿ζ�ѡ��Ĳ���
			//choose.setCh_id("seq_choose1_p.nextval");
			choose
					.setCh_question((String) request
							.getParameter("choose2_head"));
			choose.setKeya((String) request.getParameter("choose2_Keya"));
			choose.setKeyb((String) request.getParameter("choose2_Keyb"));
			choose.setKeyc((String) request.getParameter("choose2_Keyc"));
			choose.setKeyd((String) request.getParameter("choose2_Keyd"));
			// �õ��γ̵�ID
			String couse = request.getParameter("course");
			int L1_id = exam.getLesson1Id(couse);
			choose.setL_id(L1_id);
			choose.setType(testType);
			//
			choose.setType(testType);
			// check���÷�
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
		} else if (testType == 3) {//���޿������Ĳ���
			//text.setText_id("seq_text1_p.nextval");
//			 �õ��γ̵�ID
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
		int maxChoose1RowCount = exam.getLengthOfChoose1Table();// choose1���ж�����
		choose1Page.setCurPage(1); // Ϊcourse1Page���Bean���õ�ǰҳΪ1
		choose1Page.setRowsPerPage(15); // Ϊcourse1Page���Bean����ÿҳΪ15������.
		choose1Page.setFormName("choose1Form"); // Ϊcourse1Page���Bean����FormName
		choose1Page.setMaxRowCount(maxChoose1RowCount); // Ϊcourse1Page���Bean���ü�¼�й��ж�����.
		
		if (maxChoose1RowCount % 15 == 0) {// Ϊcourse1Page���Bean�������ҳ��.
			choose1Page.setMaxPage(maxChoose1RowCount / 15 );//����ҳ��������ÿҳ��������ʱ
		} else {
			choose1Page.setMaxPage(maxChoose1RowCount / 15 + 1);//����
		}
		choose1Page.setTarget("CflushExam_updateOrDeleteTestServlet");// Ϊchoose1Page���BeanҪ��ת��servlet,���Ǳ�ҳ.
		choose1List = exam.getChoose1Table(1, 15); // ȡ����ʹ���ĵ�ǰҳ��Ҫ��ʾ�ļ�¼.
		request.getSession().setAttribute("choose1Page", choose1Page);
		request.setAttribute("choose1List", choose1List);
		request.setAttribute("choose1ToString",choose1Page.toString());
		request.setAttribute("choose1PageStr", choose1Page.getPageStr());
		//ѡ��ѡ����
		int maxChoose2RowCount = exam.getLengthOfChoose2Table();
		List choose2List = exam.getChoose2Table(1,maxChoose2RowCount);
		request.setAttribute("choose2List", choose2List);
		//���������
		int maxText1RowCount = exam.getLengthOfText1Table();
		List text1List = exam.getText1Table(1,maxText1RowCount);
		request.setAttribute("text1List", text1List);
		//ѡ�������
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
//		 �ı䵱ǰҳ,��ʾ��ǰҳ
		//���޿ε�ѡ����
	
		List choose1List = null;
		 Page choose1Page = (Page) request.getSession().getAttribute("choose1Page");
		 
		int curPage = Integer.parseInt(curPageArray[0]);
		choose1Page.setCurPage(curPage);
		if (curPage < choose1Page.getMaxPage()) {
			choose1List = exam.getChoose1Table((curPage - 1)//ȡ����ǰҳ��Ҫ��ʾ�ļ�¼.
					* choose1Page.getRowsPerPage() + 1, curPage
					* choose1Page.getRowsPerPage());
		} else {
			choose1List = exam.getChoose1Table((curPage - 1)
					* choose1Page.getRowsPerPage() + 1, choose1Page//ȡ�����һҳҳ��Ҫ��ʾ�ļ�¼.
					.getMaxRowCount());
		}
		request.setAttribute("choose1PageStr",choose1Page.getPageStr());
		request.setAttribute("choose1PageToString",choose1Page.toString());
		request.setAttribute("choose1List",choose1List);
		
		//ѡ��ѡ����
		int maxChoose2RowCount = exam.getLengthOfChoose2Table();
		List choose2List = exam.getChoose2Table(1,maxChoose2RowCount);
		request.setAttribute("choose2List", choose2List);
		//���������
		int maxText1RowCount = exam.getLengthOfText1Table();
		List text1List = exam.getText1Table(1,maxText1RowCount);
		request.setAttribute("text1List", text1List);
		//ѡ�������
		int maxText2RowCount = exam.getLengthOfText2Table();
		List text2List = exam.getText2Table(1,maxText2RowCount);
		request.setAttribute("text2List", text2List);
		
		return SUCCESS;
	}
	
	public String excamUploadOrDeleteTestServlet(){
		Cexam exam = new Cexam();
		
		// ���if����Ƕ�ҳ��exam_updateOrDeleteTest.jsp�Ĳ鿴��ť����Ӧ
		if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("mustChoose")) {
			CchooseTestBean choose = exam.getChoose1Table(request
					.getParameter("view"));
			if (choose.getType() == 1) {
				request.setAttribute("type", "���޿ε�ѡ��");
				request.setAttribute("choose1", choose);
				return "exam_newPageUpdateChoose1";
			} else {
				request.setAttribute("type", "���޿ζ�ѡ��");
				request.setAttribute("choose2", choose);
				return "exam_newPageUpdateChoose2";
			}
		} else if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("easyChoose")) {
			CchooseTestBean choose = exam.getChoose2Table(request
					.getParameter("view"));
			if (choose.getType() == 1) {
				request.setAttribute("type", "ѡ�޿ε�ѡ��");
				request.setAttribute("choose1", choose);
				return "exam_newPageUpdateChoose1";
			} else {
				request.setAttribute("type", "ѡ�޿ζ�ѡ��");
				request.setAttribute("choose2", choose);
				return "exam_newPageUpdateChoose2";
			}
		} else if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("mustText")) {
			CtextTestBean text = exam.getText1Table(request
					.getParameter("view"));
			request.setAttribute("type", "���޿������");
			request.setAttribute("text", text);
			return "exam_newPageUpdateText";
		} else if (request.getParameter("cType") != null
				&& request.getParameter("cType").equals("easyText")) {
			CtextTestBean text = exam.getText2Table(request
					.getParameter("view"));
			request.setAttribute("type", "ѡ�޿������");
			request.setAttribute("text", text);
			return "exam_newPageUpdateText";

		}
		// ���if����Ƕ�ҳ��exam_updateOrDeleteTest.jsp��ɾ����ť����Ӧ

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
