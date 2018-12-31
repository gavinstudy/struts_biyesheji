package com.struts.action;

import java.sql.SQLException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sevenEleven.javaBean.CcheckAUser;
import com.struts2.model.CadminBean;
import com.struts2.model.UserLogin;

public class ManagerAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	
	private String number;
	private String password;
	
	CcheckAUser check = new CcheckAUser();
	
	public String log() throws SQLException {
		CadminBean admin = check.checkAdminLogin(number,password);
		if (admin != null) {
			session.put("admin", admin);
			return SUCCESS; 
		} else {
			return "error";
		}
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
}
