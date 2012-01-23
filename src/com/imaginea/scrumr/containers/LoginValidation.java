package com.imaginea.scrumr.containers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.imaginea.scrumr.entities.UserEntity;
import com.imaginea.scrumr.utilities.DatabaseHandler;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Servlet implementation class LoginValidation
 */
@WebServlet("/LoginValidation")
public class LoginValidation extends HttpServlet {
	private static final long serialVersionUID = 1L;
    HttpSession session = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginValidation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String method = request.getParameter("m");
		String error = null;
		String destination = null;
		String projects;
		if(method != null && method.equalsIgnoreCase("logout")){
			HttpSession sessionVar = request.getSession(false);
			if(sessionVar != null){
				sessionVar.invalidate();
			}
			destination = "/index.jsp";
			RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
			rd.forward(request, response);
		}else{
			DatabaseHandler data = new DatabaseHandler();
			System.out.println("user: "+username+": password "+password);
			if(username!= null && password != null){
				UserEntity userDetails = data.getUserInfo(username);
				if(userDetails != null){
					System.out.println("User details: "+userDetails);
					if(userDetails.getPassword().equals(password)){
						session = request.getSession(true);
						session.setAttribute("session", session.getId());
						session.setAttribute("username", username);
						System.out.println("SessionID: "+session.getId());
						/*projects = getProjects(username);
					System.out.println("IN Servlet:"+projects);
					request.setAttribute("projects", projects);*/
						destination = "/index.jsp";
						request.setAttribute("error", null);
						RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
						rd.forward(request, response);
					}else{
						error = "Invalid password";
						destination = "/index.jsp";
						request.setAttribute("error", error);
						RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
						rd.forward(request, response);
					}
				}else{
					error = "User doesn't exist";
					destination = "/index.jsp";
					request.setAttribute("error", error);
					RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
					rd.forward(request, response);
				}
			}else{
				error = "Please sign-in";
				destination = "/index.jsp";
				request.setAttribute("error", error);
				RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
				rd.forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	/*private String getProjects(String username){
		Client client = Client.create();
		WebResource wb = client.resource("http://localhost:8080/scrumr/restapi/project/search/"+username);
		String s = wb.get(String.class);
		return s;
	}*/

}
