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
 * Servlet implementation class UserChgpw
 */
@WebServlet("/user/chgpw")
public class UserChgpw extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserChgpw() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		//recup parameters
		String oldpwd=request.getParameter("oldpwd");
		String pwd=request.getParameter("pwd");
		String repwd=request.getParameter("repwd");
		String submit=request.getParameter("submit");
		
		// si formulaire submit
		if (submit!=null) {
			if (!model.verifyPassword(oldpwd)) {
				request.setAttribute("errText", "Le mot de passe actuel saisi incorrect");
			}
			else if (pwd == null || pwd.length() <8) {
				request.setAttribute("errText", "Le nouveau mot de passe doit être de longueur au moins 8");
			}
			else if (!pwd.equals(repwd)) {
				request.setAttribute("errText", "Le nouveau mot de passe confirmé doit être égal au nouveau mot de passe");
			}
			else { // succes
				model.updatePassword(pwd);
				if (model.isAdmin())
					response.sendRedirect(request.getContextPath() + "/admin/vulnerabilites");
				else
					response.sendRedirect(request.getContextPath() + "/user/vulnerabilites");
				return;
			}
		}
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/chgpw.jsp").forward(request, response);
		}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
