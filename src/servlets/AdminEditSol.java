package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ModelVuln;
import model.Solution;


/**
 * Servlet implementation class AdminEditSol
 */
@WebServlet("/admin/editSol")
public class AdminEditSol extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminEditSol() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		String strId = request.getParameter("id");
		int id = Integer.MIN_VALUE;
		String unlinkLog = request.getParameter("unlinkLog"); // button submit, contient le nom du logiciel
		String linkLog = request.getParameter("linkLog"); // input submit
		String log = request.getParameter("log"); // lié à submit linkLog
		String new_ = request.getParameter("new");
		String save = request.getParameter("save");
		String ref = request.getParameter("ref");
		if (ref==null) ref="";
		String titre = request.getParameter("titre");
		if (titre==null) titre="";
		String descr = request.getParameter("descr");
		if (descr==null) descr="";
		// persistance de la saisie
		if (model.getSol()!=null) {
			model.getSol().setReference(ref);
			model.getSol().setTitre(titre);
			model.getSol().setDescription(descr);
		}

		// les POST en premier, pour les traiter en priorité vs params GET persistant
		if (unlinkLog != null) { 
				model.unlinkSolLog(unlinkLog);
		}
		else if (linkLog != null) { 
				model.linkSolLog(log); 
		}
		if (save != null) { 
			if (ref.isEmpty()
					|| titre.isEmpty()
					|| descr.isEmpty()
					) {
				request.setAttribute("errText", "Les champs doivent être saisis");
			}
			else if (model.existsOtherSolRef())
				request.setAttribute("errText", "La référence existe déjà");
			else {//succes
				model.saveSol();
				response.sendRedirect(request.getContextPath() + "/admin/solutions");
				return;
			}
		}
		else if (strId != null) {  // ?id=xx
			try {
				id = Integer.parseInt(strId);
			} catch (NumberFormatException e) { // echec parsing id
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/admin/solutions");
				return;
			}
			model.loadSol(id);
			if (model.getSol()==null) { // echec load from db
				response.sendRedirect(request.getContextPath() + "/admin/solutions");
				return;
			}
			model.loadSolSelectLogiciels();
		}
		else if (new_ != null || model.getSol() == null) { // ?new=true
			model.setSol(new Solution());
			model.loadSolSelectLogiciels();
		}

		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/adminEditSol.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
