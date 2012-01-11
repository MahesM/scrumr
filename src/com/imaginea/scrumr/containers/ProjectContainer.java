package com.imaginea.scrumr.containers;

import java.io.IOException;

import javax.activation.DataHandler;
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
@WebServlet("/ProjectContainer")
public class ProjectContainer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    HttpSession session = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectContainer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ProjContainer");
		String projectId = request.getParameter("projectId");
		String username = (String) request.getSession().getAttribute("username");
		System.out.println(username);
		System.out.println(projectId);
		String error = null;
		String destination = null;
		DatabaseHandler data = new DatabaseHandler();
		if(username!= null){
			/*String project = getProject(projectId);
			String userStories = getUserStories(projectId);
			request.setAttribute("project", project);
			request.setAttribute("stories", userStories);
			System.out.println("Proj: "+project);
			System.out.println("Stories: "+userStories);*/
			destination = "/project.jsp";
			request.setAttribute("error", null);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
			rd.forward(request, response);
		}else{
			error = "Please enter username/password";
			destination = "/login.jsp";
			request.setAttribute("error", error);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	/*private String getProject(String projectId){
		Client client = Client.create();
		WebResource wb = client.resource("http://localhost:8080/scrumr/restapi/project/"+projectId);
		String s = wb.get(String.class);
		return s;
	}

	private String getUserStories(String projectId){
		Client client = Client.create();
		WebResource wb = client.resource("http://localhost:8080/scrumr/restapi/stories/search/"+projectId);
		String s = wb.get(String.class);
		return s;
	}*/
}
