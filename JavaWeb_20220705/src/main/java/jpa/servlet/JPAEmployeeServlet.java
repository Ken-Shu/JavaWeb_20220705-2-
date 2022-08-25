package jpa.servlet;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpa.entity.Employee;
import jpa.service.JPAService;
import jpa.util.DESEncryptService;

/*
 * GET /jpa/employee/
 * GET /jpa/employee/1?password=1234 (輸入正確會得到此人的salary資料)
 * 
 */

@WebServlet("/jpa/employee/*")
public class JPAEmployeeServlet extends HttpServlet{
	
	private JPAService jpaService = new JPAService();
	
	private Integer getId(HttpServletRequest req) {
		String pathInfo = req.getPathInfo();
		pathInfo = pathInfo.replace("/", "");
		return pathInfo.matches("[0-9]+")? Integer.parseInt(pathInfo):null; // 數字0~9  +代表一到多個
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = getId(req);
		if(id != null) {
			String password = req.getParameter("password");
			try {
				//使用者所輸入的密碼
				// 將 password 透過MD5加密
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				byte[] result = md5.digest(password.getBytes()); // input 資料加密
				String md5_password = String.format("%032X", new BigInteger(result));
				
				//取得使用者存在資料庫的 加密密碼
				Employee employee = jpaService.getEmployee(id);
				
				//加密密碼比對
				if(md5_password.equals(employee.getPassword())) {
					String key_path = "C:/Users/MB-207/git/JavaWeb_20220705-2-/JavaWeb_20220705/key/user.key";
					DESEncryptService des = new DESEncryptService(key_path);
					String salary =new String( des.decryptor(employee.getSalary())); //解密後的資料
					resp.getWriter().print(salary);
				}else {
					resp.getWriter().print("fail");
				}
			} catch (Exception e) {
				
			}
			
		}
	}
}
