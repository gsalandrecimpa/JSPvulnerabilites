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
 * Servlet implementation class UserVuln
 */
@WebServlet("/user/vuln")
public class UserVuln extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserVuln() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		String strId = request.getParameter("id");
		int id = Integer.MIN_VALUE;
		
		if (strId != null) {
			try {
				id = Integer.parseInt(strId);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/admin/vulnerabilites");
				return;
			}
		}
		
		model.loadVuln(id);
		if (model.getVuln()==null) {
			response.sendRedirect(request.getContextPath() + "/user/vulnerabilites");
			return;
		}
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/userVuln.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
