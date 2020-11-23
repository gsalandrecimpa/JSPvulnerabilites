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
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		
		//recup parameters
		String login=request.getParameter("login");
		String pwd=request.getParameter("pwd");
		String repwd=request.getParameter("repwd");
		String submit=request.getParameter("submit");
		
		// si formulaire submit
		if (submit!=null) {
			model.setUsername(login);
			if (model.existsUser(login)) {
				request.setAttribute("errText", "Ce nom d'utilisateur existe déjà");
			}
			else if (pwd == null || pwd.length() <8) {
				request.setAttribute("errText", "Le mot de passe doit être de longueur au moins 8");
			}
			else if (!pwd.equals(repwd)) {
				request.setAttribute("errText", "Le mot de passe confirmé doit être égal au mot de passe");
			}
			else { // succes
				model.saveUser(login, pwd, "user");
				model.setConnected(true);
				response.sendRedirect(request.getContextPath() + "/user/vulnerabilites");
				return;
			}
		}
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
