package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ModelVuln;

/**
 * Servlet implementation class Login
 */

@WebServlet("/") // marche aussi avec /login ou toute page non déclarée par les servlets
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		if (model == null) {
			model = new ModelVuln();
			session.setAttribute("model", model);
		}
		
		// si déjà connecté
		if (model.isAdmin()) {
			response.sendRedirect(request.getContextPath() + "/admin/vulnerabilites");
			return;
		}
		if (model.isConnected()) {
			response.sendRedirect(request.getContextPath() + "/user/vulnerabilites");
			return;
		}
		
		//recup parameters
		String login=request.getParameter("login");
		String pwd=request.getParameter("pwd");
		String submit=request.getParameter("submit");
		
		// si formulaire submit
		if (submit!=null) {
			model.authenticateUser(login, pwd);
			if (model.isAdmin()){
				response.sendRedirect(request.getContextPath() + "/admin/vulnerabilites");
				return;
			}
			if (model.isConnected()){
				response.sendRedirect(request.getContextPath() + "/user/vulnerabilites");
				return;
			}
			// echec authentification
			request.setAttribute("errText", "La connexion a échoué");
		}
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
