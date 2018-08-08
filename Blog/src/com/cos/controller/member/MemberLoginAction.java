package com.cos.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.MemberDAO;
import com.cos.dto.MemberVO;
import com.cos.util.SHA256;
import com.cos.util.Script;

public class MemberLoginAction implements Action{
	
	private static String naming = "MemberLoginAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = "main.jsp";
		
		MemberVO member = new MemberVO();
		MemberDAO dao = new MemberDAO();
		
		String id = null;
		String password = null;
		String salt = null;
		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
			salt = dao.select_salt(id);
		}
		
		System.out.println("salt 갑 : " + salt);
		if(request.getParameter("password") != null) {
			password = request.getParameter("password");
			//패스워드를 SHA256으로 해쉬하기
			password = SHA256.getEncrypt(password, salt);
		}
		
		
		member.setId(id);
	
		int result = dao.check(id,password);
		
		if(result == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "로그인 성공", url);
		}else if(result == -1) {
			Script.moving(response, "로그인 실패");
		}else if(result == 0){
			Script.moving(response, "데이터 베이스오류");
		}
	}
}
	

	

