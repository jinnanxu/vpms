package com.vpms.controller.user;

import com.vpms.dao.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ApiBaseController {
	
	public User getCurrUser(HttpServletRequest request) {
		User user = (User) request.getAttribute("loginUser");//从Request中根据Token获取登录用户
		return user;
	}
	
	public void updateCurrUser(HttpSession session, User user) {
		session.setAttribute("CURR_USER", user);
	}

}
