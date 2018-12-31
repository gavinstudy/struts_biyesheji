package com.sevenEleven.javaBean;

import java.sql.SQLException;

import com.struts2.model.CadminBean;
import com.struts2.model.CstudentBean;
import com.struts2.model.CteacherBean;


public class CcheckAUser extends CDBConnection {

	// admin登陆验证
	public CadminBean checkAdminLogin(String userName, String userPwd) {
		CadminBean admin = null;
		if (!checkParameter(userName + userPwd)) {
			userName = "null";
			userPwd = "null";
		}
		try {
			sql = "SELECT COUNT(*) FROM ADMINISTRATOR WHERE A_NAME=" + " '"
					+ userName + "' " + "AND A_PASSWORD=" + " '" + userPwd // 拼SQL语句
					+ "'  ";
			if (con == null) {
				getConnection(); // 连接
			}
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					rs.close();
					close(); // 关闭记录集和声明
					admin = this.getAdmin(userName);
				} else {
					return admin;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return admin;
	}

	// student登陆验证
	public CstudentBean checkStudentLogin(String userName, String userPwd) throws SQLException {
		
		CstudentBean studnet = null;
		if (!checkParameter(userName + userPwd)) {
			userName = "null";
			userPwd = "null";
		}
		try {
			sql = "select COUNT(*) from stu_cla where S_NUMBER=" + " '"
					+ userName + "' " + "AND S_PASSWORD=" + " '" + userPwd // 拼SQL语句
					+ "'  ";
			
			if (con == null) {
				getConnection();
			}
	//System.out.println(sql);
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					rs.close();
					stmt.close(); //关闭记录集和声明
					studnet = this.getStudent(userName);
				} else {
				    return studnet ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return studnet;
	}

	// teacher登陆验证
	public CteacherBean checkTeacherLogin(String userName, String userPwd) {
		CteacherBean teacher = null;
		if (!checkParameter(userName + userPwd)) {
			 userName = "null";
			 userPwd = "null";
		}
		try {
			sql = "SELECT COUNT(*) FROM TEACHER WHERE T_NUMBER=" + " '"// 拼SQL语句
					+ userName + "' " + "AND T_PASSWORD=" + " '" + userPwd
					+ "'  ";
			if (con == null) {
				getConnection(); // 连接
			}
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					rs.close();
					stmt.close(); // 关闭记录集和声明
					teacher = this.getTeacher(userName);
				} else {
					return null;
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return teacher;
	}

	// 提取登录student信息
	public CstudentBean getStudent(String userName) {
		CstudentBean student = new CstudentBean();
		sql = "select * from student where s_number =" + " '" + userName + "' ";
		try {
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				student.setS_id(rs.getLong("S_ID"));
				student.setC_id(rs.getLong("C_ID"));
				student.setS_name(rs.getString("S_NAME"));
				student.setS_number(rs.getString("S_NUMBER"));
				student.setS_password(rs.getString("S_PASSWORD"));
				close(); // 关闭记录集并删除连接
				return student;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return student;
	}

	// 提取登录teacher信息
	public CteacherBean getTeacher(String userName) {
		CteacherBean teacher = new CteacherBean();
		sql = "select * from teacher where t_number =" + " '" + userName + "' ";
		try {
			stmt = con.createStatement();
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				teacher.setT_id(rs.getLong("T_ID"));
				teacher.setT_name(rs.getString("T_NAME"));
				teacher.setT_number(rs.getString("T_NUMBER"));
				teacher.setT_password(rs.getString("T_PASSWORD"));
				teacher.setSkill(rs.getString("T_SKILL"));
				close(); // 关闭记录集并删除连接
				return teacher;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return teacher;
	}

	// 提取登录admin信息
	public CadminBean getAdmin(String userName) {
		CadminBean admin = new CadminBean();
		sql = "select A_ID,A_NAME,A_PASSWORD,A_TYPE from [ADMINISTRATOR] where A_NAME=" + " '" + userName
				+ "' ";
		try {
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				admin.setA_id(rs.getLong("A_ID"));
				admin.setA_name(rs.getString("A_NAME"));
				admin.setA_password(rs.getString("A_PASSWORD"));
				//admin.setA_password("bbs");
				admin.setA_type(rs.getLong("A_TYPE"));
				//admin.setA_type(4);
				close(); // 关闭记录集并删除连接
				return admin;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return admin;
	}
	  //检查网络
	public boolean checkNet(){
		try {
			sql = "select * from tab";
			if (con == null) {
				getConnection();  // 连接
			}
			rs = myExecuteQuery(sql);
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					rs.close();
					close(); // 关闭记录集和声明
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	// 过滤非法字符
	public boolean checkParameter(String para) {
		int flag = 0;
		flag += para.indexOf("'") + 1;
		flag += para.indexOf(";") + 1;
		flag += para.indexOf("1=1") + 1;
		flag += para.indexOf("|") + 1;
		flag += para.indexOf("<") + 1;
		flag += para.indexOf(">") + 1;
		if (flag != 0) {
			System.out.println("提交了非法字符!!!");
			return false;
		}
		return true;
	}
}
