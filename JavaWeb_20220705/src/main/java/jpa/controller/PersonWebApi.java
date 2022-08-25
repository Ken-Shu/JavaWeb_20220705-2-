package jpa.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import jpa.entity.Person;
import jpa.service.JPAService;

/*
 * GET 		/webapi/person/2    單筆查詢
 * GET 		/webapi/person/     多筆查詢
 * Post 	/webapi/person/    	新增
 * Put 		/webapi/person/2    修改
 * Delete 	/webapi/person/2 	刪除
 * 
 */

@WebServlet("/webapi/person/*")
public class PersonWebApi extends HttpServlet{
	
	private Gson gson = new Gson();
	
	private JPAService jpaService = new JPAService();
	
	private Integer getId(HttpServletRequest req) {
		String pathInfo = req.getPathInfo();
		pathInfo = pathInfo.replace("/", "");
		return pathInfo.matches("[0-9]+")? Integer.parseInt(pathInfo):null; // 數字0~9  +代表一到多個
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//resp.getWriter().print(getId(req)); 
		Integer id = getId(req);
		if(id == null) { //沒有id 代表要全部查詢
			List<Person> persons = jpaService.queryAllPerson();
			resp.getWriter().print(new Status("getall","true",persons));
			return;
		}
		Person person = jpaService.getPerson(id);
		
		if(person == null) {
			resp.getWriter().print(new Status("get","false !"," id = "+ id + ",此筆資料不存在"));
			return;
		}
		//resp.getWriter().print(gson.toJson(person));
		resp.getWriter().print(new Status("get","true",person));
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String jsonString = IOUtils.toString(req.getInputStream(),"UTF-8");
		//將 json 字串 轉 Person 物件
		Person person = gson.fromJson(jsonString, Person.class);
		jpaService.addPerson(person);
		resp.getWriter().print(new Status("append","true",person));
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = getId(req);
		if(id == null) {
			resp.getWriter().print(new Status("update","false ! ", "無 id 資料"));
			return;
		}
		Person person = jpaService.getPerson(id);
		if(person == null) {
			resp.getWriter().print(new Status("update", "false ! ", "此筆資料不存在"));
			return;
		}
		String jsonString = IOUtils.toString(req.getInputStream(), "UTF-8");
		person = gson.fromJson(jsonString, Person.class);
		person.setId(id); //將 id 注入到 Person 物件中
		jpaService.updatePerson(person);
		resp.getWriter().print(new Status("update", "true",person));
	
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = getId(req);
		if(id == null) {
			resp.getWriter().print(new Status("delete" , "false !","無 id 資料"));
			return;
		}
		Person person = jpaService.getPerson(id);
		if(person == null) {
			resp.getWriter().print(new Status("delete", "false !", " 此筆資料不存在"));
			return;
		}
		jpaService.deletePerson(id);
		resp.getWriter().print(new Status("delete", "true", ""));
	}
	
	// 回應物件
	class Status{
		String name;
		String message;
		Object result;
		public Status(String name , String message , Object result) {
			this.name = name;
			this.message = message;
			this.result = result;
		}
		public String toString() {
			return gson.toJson(this);
		}
		
	}
}
