package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ModelVuln;
import model.Vulnerabilite;

/**
 * Servlet implementation class AdminEditVuln
 */
@WebServlet("/admin/editVuln")
public class AdminEditVuln extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminEditVuln() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ModelVuln model = (ModelVuln) session.getAttribute("model");
		
		String new_ = request.getParameter("new");
		String save = request.getParameter("save");
		String strId = request.getParameter("id");
		int id = Integer.MIN_VALUE;
		String unlinkLog = request.getParameter("unlinkLog");
		String linkLog = request.getParameter("linkLog");
		String log = request.getParameter("log");
		String unlinkSol = request.getParameter("unlinkSol");
		String linkSol = request.getParameter("linkSol");
		String refSol = request.getParameter("refSol");
		String ref = request.getParameter("ref");
		if (ref==null) ref="";
		String titre = request.getParameter("titre");
		if (titre==null) titre="";
		String grStr = request.getParameter("gravite");
		if (grStr==null) grStr="";
		String descr = request.getParameter("descr");
		if (descr==null) descr="";
		int gr = 0;
		try {
			gr = Integer.parseInt(grStr);
		} catch (NumberFormatException e) {}
		// persistance de la saisie
		if (model.getVuln() != null) {
			model.getVuln().setReference(ref);
			model.getVuln().setTitre(titre);
			model.getVuln().setGravite(gr);
			model.getVuln().setDescription(descr);
		}
		
		// les POST en premier, pour les traiter en priorité vs params GET persistant
		if (unlinkLog != null) {  
			model.unlinkVulnLog(unlinkLog); 
		}
		else if (linkLog != null) { 
			model.linkVulnLog(log); 
		}
		else if (unlinkSol != null) {  
			try {
				id = Integer.parseInt(unlinkSol);
				model.unlinkVulnSol(id); 
			} catch (NumberFormatException e) { // echec parsing id
				e.printStackTrace();
			}
		}
		else if (linkSol != null) {
			model.linkVulnSol(refSol); 
		}
		else if (save != null) {
			if (ref.isEmpty()
					|| titre.isEmpty()
					|| grStr.isEmpty()
					|| descr.isEmpty()
					) {
				request.setAttribute("errText", "Les champs doivent être saisis");
			}
			else if (model.existsOtherVulnRef())
				request.setAttribute("errText", "La référence existe déjà");
			else {//succes
				model.saveVuln();
				response.sendRedirect(request.getContextPath() + "/admin/vuln");
				return;
			}
		}
		else if (strId != null) {  // ?id=xx
			try {
				id = Integer.parseInt(strId);
			} catch (NumberFormatException e) { // echec parsing id
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/admin/vulnerabilites");
				return;
			}
			model.loadVuln(id);
			if (model.getVuln()==null) { // echec load from db
				response.sendRedirect(request.getContextPath() + "/admin/vulnerabilites");
				return;
			}
			model.loadVulnSelectLogiciels();
			model.loadVulnSelectSolReferences();
		}
		else if (new_ != null || model.getVuln() == null) { // ?new=true
			model.setVuln(new Vulnerabilite());
			model.loadVulnSelectLogiciels();
			model.loadVulnSelectSolReferences();
		}


		//view
		getServletContext().getRequestDispatcher("/WEB-INF/views/adminEditVuln.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
