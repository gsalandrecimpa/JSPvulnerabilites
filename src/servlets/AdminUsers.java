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
 * Servlet implementation class AdminUsers
 */
@WebServlet("/admin/users")
public class AdminUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUsers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		String resetpwd = request.getParameter("resetpwd"); // contient le username concerné
		String chgrole = request.getParameter("chgrole"); // contient le username concerné
		String remove = request.getParameter("remove"); // contient le username concerné
		if (resetpwd != null)
			model.resetPassword(resetpwd);
		else if (chgrole != null)
			model.changeRole(chgrole);
		else if (remove != null)
			model.deleteUser(remove);

		
		model.loadUsers();

		// view
		getServletContext().getRequestDispatcher("/WEB-INF/views/adminUsers.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
