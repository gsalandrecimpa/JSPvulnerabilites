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
 * Servlet implementation class AdminVulnerabilites
 */
@WebServlet("/admin/vulnerabilites")
public class AdminVulnerabilites extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminVulnerabilites() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");

		String delVuln = request.getParameter("delVuln");
		int id = Integer.MIN_VALUE;

		if (delVuln != null)
		{
			try {
				id = Integer.parseInt(delVuln);
				model.deleteVuln(id);
			} catch (NumberFormatException e) { // echec parsing id
				e.printStackTrace();
			}
		}
		
		model.loadAllVulnerabilites();
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/adminVulnerabilites.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
