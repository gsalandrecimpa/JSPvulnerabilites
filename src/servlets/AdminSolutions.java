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
 * Servlet implementation class AdminSolutions
 */
@WebServlet("/admin/solutions")
public class AdminSolutions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminSolutions() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		String delSol = request.getParameter("delSol");
		int id = Integer.MIN_VALUE;

		if (delSol != null)
		{
			try {
				id = Integer.parseInt(delSol);
				model.deleteSol(id);
			} catch (NumberFormatException e) { // echec parsing id
				e.printStackTrace();
			}
		}
		
		
		model.loadAllSolutions();
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/adminSolutions.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
