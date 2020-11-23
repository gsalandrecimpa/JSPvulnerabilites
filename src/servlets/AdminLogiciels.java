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
 * Servlet implementation class AdminLogiciel
 */
@WebServlet("/admin/logiciels")
public class AdminLogiciels extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminLogiciels() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		// recup param
		String delLog=request.getParameter("delLog");
		String add=request.getParameter("add");
		String logiciel=request.getParameter("logiciel");
		
		// si  add
		if (add != null)
			model.saveLogiciel(logiciel);
		// si del
		else if (delLog != null)
			model.deleteLogiciel(delLog);
		
		// maj pour la vue
		model.loadLogiciels();
		
		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/adminLogiciels.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
