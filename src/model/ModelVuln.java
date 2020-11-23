package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import secu.BCrypt;

public class ModelVuln {
	private static String RESET_PASSWORD = "depart0";
	private boolean connected;
	private boolean admin;
	private Connection conn;
	private String username;
	private ArrayList<Vulnerabilite> vulnerabilites = new ArrayList<>();
	private ArrayList<Solution> solutions = new ArrayList<>();
	private ArrayList<Logiciel> logiciels = new ArrayList<>();
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<String> selectLogiciels = new ArrayList<>();
	private ArrayList<String> selectSolReferences = new ArrayList<>();

	private Vulnerabilite vuln;
	private Solution sol;
	
	public void loadAllSolutions() { // with logiciels
		setSolutions(new ArrayList<Solution>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		Solution sol = null;
		try {
			prst= getConn().prepareStatement(
					"SELECT id_sol,reference,titre,description FROM solution"
					+ " ORDER BY id_sol DESC");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					sol = new Solution(
							rs.getInt(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							true);
					getSolutions().add(sol);
					loadSolLogiciels(sol);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}  finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}

	}
	
	public boolean existsOtherVulnRef() {
		if (vuln == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT reference FROM vulnerabilite WHERE reference = ? AND id_vuln <> ?");
			prst.setInt(2, vuln.getId());
			prst.setString(1, vuln.getReference());
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}  finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
	}
	
	public boolean existsOtherSolRef() {
		if (sol == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			if (sol.isFromDB()) {
				prst = getConn().prepareStatement(
						"SELECT reference FROM solution WHERE reference = ? AND id_sol <> ?");
				prst.setInt(2, sol.getId());
			}
			else // as sol not in db, search in all references
				prst = getConn().prepareStatement(
						"SELECT reference FROM solution WHERE reference = ?");
			prst.setString(1, sol.getReference());
			rs = prst.executeQuery();
			if (rs != null && rs.next()) 
				return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
	}

	
	public void saveVuln() { 
		ResultSet rs = null;
		
		if (vuln == null) 
			return;
		PreparedStatement prst = null;
		try {
			if (!vuln.isFromDB() || !existsVulnerabilite(vuln.getId())) { // new vuln
				prst = getConn().prepareStatement(
						"INSERT INTO vulnerabilite(reference,titre,gravite,description) VALUES(?,?,?,?)");
			}
			else {
				prst = getConn().prepareStatement(
						"UPDATE vulnerabilite SET reference=?,titre=?,gravite=?,description=? WHERE id_vuln=?");
				prst.setInt(5, vuln.getId());
			}
			prst.setString(1, vuln.getReference());
			prst.setString(2, vuln.getTitre());
			prst.setInt(3, vuln.getGravite());
			prst.setString(4, vuln.getDescription());
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		// **************
		// suite seulement si vuln était new (pas en base avant)
		if (vuln.isFromDB())
			return;
		
		// recup de l'id par la réference, si success set fromDB
		try {
			prst = getConn().prepareStatement(
					"SELECT id_vuln FROM vulnerabilite WHERE reference = ?");
			prst.setString(1, vuln.getReference());
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				vuln.setId(rs.getInt(1));
				vuln.setFromDB(true);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		// ajout en base de tous les logiciels liés
		if (vuln.getLogiciels() != null) {
			for (String logNom:vuln.getLogiciels()) {
				saveVulnLog(logNom);
			}
		}
		// ajout en base de toutes les solutions liées
		if (vuln.getSolutions() != null) {
			for (Solution sol:vuln.getSolutions()) {
				saveVulnSol(sol.getReference());
			}
		}
		// **************
	} 
	
	public void saveSol() { 
		ResultSet rs = null;
		
		if (sol == null) 
			return;
		PreparedStatement prst = null;
		try {
			if (sol.isFromDB()) { 
				prst = getConn().prepareStatement(
						"UPDATE solution SET reference=?,titre=?,description=? WHERE id_sol=?");
				prst.setInt(4, sol.getId());
			}
			else {// new sol
				prst = getConn().prepareStatement(
						"INSERT INTO solution(reference,titre,description) VALUES(?,?,?)");
			}
			prst.setString(1, sol.getReference());
			prst.setString(2, sol.getTitre());
			prst.setString(3, sol.getDescription());
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		}  finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		// **************
		// suite uniquement si sol n'était pas dans DB
		if (sol.isFromDB()) 
			return;
		
		// recup de l'id par la réference, si success set fromDB
		try {
			prst = getConn().prepareStatement(
					"SELECT id_sol FROM solution WHERE reference = ?");
			prst.setString(1, sol.getReference());
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				sol.setId(rs.getInt(1));
				sol.setFromDB(true);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		// ajout en base de tous les logiciels liés
		if (sol.getLogiciels() != null) {
			for (String logNom:sol.getLogiciels()) {
				saveSolLog(logNom);
			}
		}  
		// **************
	}
	
	public void loadVulnSelectLogiciels() { 
		setSelectLogiciels(new ArrayList<String>());
		if (vuln==null) return;
		PreparedStatement prst = null;
		ResultSet rs = null;
		String logNom = null;
		
		try {
			if (vuln.isFromDB()) {
				prst = getConn().prepareStatement(
						"SELECT nom_logiciel FROM logiciel WHERE nom_logiciel NOT IN "
								+ " (SELECT nom_logiciel FROM vulnerabilite_logiciel WHERE id_vuln = ?)");
				prst.setInt(1, vuln.getId());
			}
			else
				prst = getConn().prepareStatement(
						"SELECT nom_logiciel FROM logiciel");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					logNom = rs.getString(1);
					getSelectLogiciels().add(logNom);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}

	}
	
	public void loadSolSelectLogiciels() { // la sol peut être nouvelle ou bien from db
		setSelectLogiciels(new ArrayList<String>());
		if (sol==null) return;
		PreparedStatement prst = null;
		ResultSet rs = null;
		String logNom = null;
		
		// recup  logiciels non liés à la sol, ou tout si new (not in db)
		try {
			if (sol.isFromDB()){
				prst = getConn().prepareStatement(
						"SELECT nom_logiciel FROM logiciel WHERE nom_logiciel NOT IN "
						+ " (SELECT nom_logiciel FROM logiciel_solution WHERE id_sol = ?)");
				prst.setInt(1, sol.getId());
			}
			else
				prst = getConn().prepareStatement(
						"SELECT nom_logiciel FROM logiciel");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					logNom = rs.getString(1);
					selectLogiciels.add(logNom);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}

	}


	public void loadVulnSelectSolReferences() { 
		setSelectSolReferences(new ArrayList<String>());
		if (vuln==null) return;
		PreparedStatement prst = null;
		ResultSet rs = null;
		String ref = null;
		
		// recup  references non liés à la vuln 
		try {
			if (vuln.isFromDB()) {
				prst = getConn().prepareStatement(
						"SELECT reference FROM solution WHERE id_sol NOT IN "
								+ " (SELECT id_sol FROM vulnerabilite_solution WHERE id_vuln = ?)");
				prst.setInt(1, vuln.getId());
			}
			else
				prst = getConn().prepareStatement(
						"SELECT reference FROM solution");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ref = rs.getString(1);
					getSelectSolReferences().add(ref);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}

	}

	
	public void unlinkVulnLog(String logNom) { // prerequis: vuln en base
		if (vuln==null
				|| logNom == null)
			return;
		
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"DELETE FROM vulnerabilite_logiciel WHERE id_vuln = ? AND nom_logiciel = ?");
			prst.setInt(1, vuln.getId());
			prst.setString(2, logNom);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		// update model
		vuln.removeLogiciel(logNom);
		selectLogiciels.add(logNom);
	
	}

	
	public void saveVulnLog(String logNom) { 
		if (vuln==null
				|| logNom == null 
				|| !vuln.isFromDB()
				|| !existsLogiciel(logNom) 
				|| !existsVulnerabilite(vuln.getId())
				|| existsVulnLog(logNom))
			return;
		
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"INSERT INTO vulnerabilite_logiciel(id_vuln,nom_logiciel) VALUES(?,?)");
			prst.setInt(1, vuln.getId());
			prst.setString(2, logNom);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void linkVulnLog(String logNom) { 
		saveVulnLog(logNom);
		vuln.addLogiciel(logNom);
		selectLogiciels.remove(logNom);
	}
	
	
	public void unlinkSolLog(String logNom) {
		if (sol==null
				|| logNom == null)
			return;
		
		// update db
		if (sol.isFromDB()) {
			PreparedStatement prst = null;
			try { 
				prst = getConn().prepareStatement(
						"DELETE FROM logiciel_solution WHERE id_sol = ? AND nom_logiciel = ?");
				prst.setInt(1, sol.getId());
				prst.setString(2, logNom);
				prst.executeUpdate();
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
				return;
			} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		}
		
		// update sol.logiciels and selectLogiciels
		sol.removeLogiciel(logNom);
		selectLogiciels.add(logNom);
	}

	
	public void saveSolLog(String logNom) {
		if (sol==null
				|| logNom == null 
				|| !existsLogiciel(logNom) 
				|| !sol.isFromDB()
				|| existsSolLog(logNom))
			return;
		
		// update db
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"INSERT INTO logiciel_solution(id_sol,nom_logiciel) VALUES(?,?)");
			prst.setInt(1, sol.getId());
			prst.setString(2, logNom);
			prst.executeUpdate();
			// update logiciels
			sol.getLogiciels().add(logNom);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void linkSolLog(String logNom) {
			saveSolLog(logNom);
			sol.addLogiciel(logNom);
			selectLogiciels.remove(logNom);
	}


	
	public void unlinkVulnSol(int solId) { 
		if (vuln==null)
			return;
		
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"DELETE FROM vulnerabilite_solution WHERE id_vuln = ? AND id_sol = ?");
			prst.setInt(1, vuln.getId());
			prst.setInt(2, solId);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		// update model
		vuln.removeSolution(solId);
		loadVulnSelectSolReferences();
	
	}

	
	public Solution saveVulnSol(String solRef) { 
		Solution sol = returnSolFromRef(solRef);
		if (vuln==null
				|| sol==null
				|| !vuln.isFromDB()
				|| existsVulnSol(sol.getId()))
			return sol;
		
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"INSERT INTO vulnerabilite_solution(id_vuln,id_sol) VALUES(?,?)");
			prst.setInt(1, vuln.getId());
			prst.setInt(2, sol.getId());
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return null;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		return sol;
	}
	
	public void linkVulnSol(String solRef) { 
		Solution sol = saveVulnSol(solRef);
		vuln.addSolution(sol);
		selectSolReferences.remove(solRef);
	}

	public boolean existsSolution(int idSol) {
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_sol FROM solution WHERE id_sol = ?");
			prst.setInt(1, idSol);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
		
	}
	
	public boolean existsVulnerabilite(int idVuln) {
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_vuln FROM vulnerabilite WHERE id_vuln = ?");
			prst.setInt(1, idVuln);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
		
	}
	
	public void deleteVuln(int idVuln) {
		PreparedStatement prst = null;
		try {
			prst = getConn().prepareStatement(
					"DELETE FROM vulnerabilite WHERE id_vuln = ?");
			prst.setInt(1, idVuln);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public void deleteSol(int idSol) {
		PreparedStatement prst = null;
		try {
			prst = getConn().prepareStatement(
					"DELETE FROM solution WHERE id_sol = ?");
			prst.setInt(1, idSol);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public boolean existsVulnSol(int solId) {
		if (vuln == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_vuln FROM vulnerabilite_solution WHERE id_vuln = ? AND id_sol = ?");
			prst.setInt(1, vuln.getId());
			prst.setInt(2, solId);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
	}

	
	public boolean existsLogiciel(String logNom) {
		if (logNom == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT nom_logiciel FROM logiciel WHERE nom_logiciel = ?");
			prst.setString(1, logNom);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
		
	}
	
	public boolean existsVulnLog(String logNom) {
		if (logNom == null || vuln == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_vuln FROM vulnerabilite_logiciel WHERE id_vuln = ? AND nom_logiciel = ?");
			prst.setInt(1, vuln.getId());
			prst.setString(2, logNom);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
	}
	
	public boolean existsSolLog(String logNom) {
		if (logNom == null || sol == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_sol FROM logiciel_solution WHERE id_sol = ? AND nom_logiciel = ?");
			prst.setInt(1, sol.getId());
			prst.setString(2, logNom);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
	}
	
	public void loadVuln(int id) {
		setVuln(null);
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT reference,titre,gravite,description FROM vulnerabilite WHERE id_vuln = ?");
			prst.setInt(1, id);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				setVuln(new Vulnerabilite(
						id,
						rs.getString(1),
						rs.getString(2),
						rs.getInt(3),
						rs.getString(4),
						true));
				loadVulnLogiciels();
				loadVulnSolutions();
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void loadSol(int id) {
		setSol(null);
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT reference,titre,description FROM solution WHERE id_sol = ?");
			prst.setInt(1, id);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				setSol(new Solution(
						id,
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						true));
				loadSolLogiciels();
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public Solution returnSolFromRef(String ref) {
		if (ref==null) return null;
		Solution sol = null;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_sol,titre,description FROM solution WHERE reference = ?");
			prst.setString(1, ref);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				sol = new Solution(
						rs.getInt(1),
						ref,
						rs.getString(2),
						rs.getString(3),
						true);
				loadSolLogiciels(sol);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		return sol;
	}

	
	public void loadSolLogiciels() {
		loadSolLogiciels(getSol());
	}
	
	public void loadSolLogiciels(Solution sol) {
		if (sol == null) return;
		sol.setLogiciels(new ArrayList<String>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		String logNom = null;
		
		// recup logiciels
		try {
			prst = getConn().prepareStatement(
					"SELECT nom_logiciel FROM logiciel_solution WHERE id_sol = ?");
			prst.setInt(1, sol.getId());
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					logNom = rs.getString(1);
					sol.getLogiciels().add(logNom);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
	}
	
	public void loadVulnSolutions() {
		if (vuln==null) return;
		vuln.setSolutions(new ArrayList<Solution>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		Solution sol = null;
		
		// recup solutions
		try {
			prst = getConn().prepareStatement(
					"SELECT id_sol,reference,titre,description FROM vulnerabilite_solution "
					+ " JOIN solution USING(id_sol) "
					+ " WHERE id_vuln = ?");
			prst.setInt(1, vuln.getId());
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					sol = new Solution(
							rs.getInt(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							true);
					loadSolLogiciels(sol);
					vuln.getSolutions().add(sol);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public void loadVulnLogiciels() {
		loadVulnLogiciels(vuln);
	}
	
	public void loadVulnLogiciels(Vulnerabilite vuln) {
		if (vuln==null) return;
		vuln.setLogiciels(new ArrayList<String>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		String logNom = null;
		
		// recup logiciels
		try {
			prst = getConn().prepareStatement(
					"SELECT nom_logiciel FROM vulnerabilite_logiciel WHERE id_vuln = ?");
			prst.setInt(1, vuln.getId());
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					logNom = rs.getString(1);
					vuln.getLogiciels().add(logNom);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void deleteLogiciel(String log_name) {
		if (log_name==null) return;
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"DELETE FROM logiciel WHERE nom_logiciel = ?");
			prst.setString(1, log_name);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void saveLogiciel(String log_name) {
		if (log_name==null || log_name.isEmpty()) 
			return;
		PreparedStatement prst = null;
		try { 
			prst = getConn().prepareStatement(
					"INSERT INTO logiciel(nom_logiciel) VALUES(?)");
			prst.setString(1, log_name);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		return;
	}
	
	public void updateUserLogiciels(String[] userLogiciels) {
		PreparedStatement prst = null;
		
		try { // retire tout
			prst = getConn().prepareStatement(
					"DELETE FROM user_logiciel WHERE username = ?");
			prst.setString(1, getUsername());
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}

		try {// ajoute les nouveaux
			prst = getConn().prepareStatement(
					"INSERT INTO user_logiciel(username,nom_logiciel) VALUES(?,?)");
			prst.setString(1, getUsername());
			if (userLogiciels!=null)
				for(String log:userLogiciels) {
					prst.setString(2, log);
					prst.executeUpdate();
				}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
	}
	
	public boolean existsUserLogiciel(String logNom) {
		if (logNom == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT username FROM user_logiciel WHERE username = ? AND nom_logiciel = ?");
			prst.setString(1, getUsername());
			prst.setString(2, logNom);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		
		return false;
	}
	
	public void loadLogiciels() {
		setLogiciels(new ArrayList<Logiciel>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		String logNom = null;
		
		// recup tous les logiciels
		try {
			prst = getConn().prepareStatement(
					"SELECT * FROM logiciel");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					logNom = rs.getString(1);
					getLogiciels().add(new Logiciel(
						logNom,
						existsUserLogiciel(logNom)));
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void loadVulnerabilitesLinkedToUser() { 
		setVulnerabilites(new ArrayList<Vulnerabilite>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT DISTINCT id_vuln,reference,titre,gravite,description FROM vulnerabilite"
					+ " JOIN vulnerabilite_logiciel USING(id_vuln)"
					+ " JOIN user_logiciel USING(nom_logiciel)"
					+ " WHERE username = ?"
					+ " ORDER BY id_vuln DESC");
			prst.setString(1, getUsername());
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Vulnerabilite vuln = new Vulnerabilite(
							rs.getInt(1),
							rs.getString(2),
							rs.getString(3),
							rs.getInt(4),
							rs.getString(5),
							true);
					loadVulnLogiciels(vuln);
					getVulnerabilites().add(vuln);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public void loadAllVulnerabilites() { // Vulnerabilites sans les liens
		setVulnerabilites(new ArrayList<Vulnerabilite>());
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT id_vuln,reference,titre,gravite,description FROM vulnerabilite"
					+ " ORDER BY id_vuln DESC");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Vulnerabilite vuln = new Vulnerabilite(
							rs.getInt(1),
							rs.getString(2),
							rs.getString(3),
							rs.getInt(4),
							rs.getString(5),
							true);
					loadVulnLogiciels(vuln);
					getVulnerabilites().add(vuln);
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public void authenticateUser(String user, String pwd){ // si auth OK, sets connected and admin upon role
		setConnected(false);
		setAdmin(false);
		setUsername(user);
		if (user==null || pwd == null) return;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT password,role FROM user WHERE username = ?");
			prst.setString(1, user);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				String hashPw = rs.getString(1);
				if (hashPw!=null && BCrypt.checkpw(pwd, hashPw)) {
					String role = rs.getString(2);
					if (role != null) {
						if (role.equals("admin")) {
							setConnected(true);
							setAdmin(true);
						} 
						else if (role.equals("user")) {
							setConnected(true);
						}
					}
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public boolean verifyPassword(String pwd){ // true si pw OK
		if (pwd == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT password FROM user WHERE username = ?");
			prst.setString(1, getUsername());
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
				String hashPw = rs.getString(1);
				if (hashPw!=null && BCrypt.checkpw(pwd, hashPw)) {
					return true;
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		return false;
	}

	public void saveUser(String user, String pwd, String role) { 
		if (user == null || pwd == null || role == null) return;
		String hashPw = BCrypt.hashpw(pwd, BCrypt.gensalt());
		PreparedStatement prst = null;
		try {
			prst = getConn().prepareStatement(
					"INSERT INTO user(username,password,role) VALUES(?,?,?)");
			prst.setString(1, user);
			prst.setString(2, hashPw);
			prst.setString(3, role);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return;
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

	public void resetPassword(String user) { 
		updatePassword(user, RESET_PASSWORD);
	}

	
	public void updatePassword(String pwd) { 
		updatePassword(username, pwd);
	}

	public void updatePassword(String user, String pwd) { 
		if (user == null || pwd == null) return;
		String hashPw = BCrypt.hashpw(pwd, BCrypt.gensalt());
		PreparedStatement prst = null;
		try {
			prst = getConn().prepareStatement(
					"UPDATE user SET password=? WHERE username=?");
			prst.setString(1, hashPw);
			prst.setString(2, user);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	
	public void deleteUser(String user) { 
		if (user == null ) return;
		PreparedStatement prst = null;
		try {
			prst = getConn().prepareStatement(
					"DELETE FROM user WHERE username=?");
			prst.setString(1, user);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}


	public void loadUsers() {
		users = new ArrayList<User>();
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT username, role FROM user");
			rs = prst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					users.add(new User(
							rs.getString(1),
							rs.getString(2)));
				}
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}


	public boolean existsUser(String user) {
		if (user == null) return false;
		PreparedStatement prst = null;
		ResultSet rs = null;
		try {
			prst = getConn().prepareStatement(
					"SELECT username FROM user WHERE username = ?");
			prst.setString(1, user);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) return true;
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
		return false;	
	}

// getters and setters
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public Connection getConn() {
		if (conn==null) {
			conn = Connecteur.getConnection();
		}
		if (conn==null) {throw new RuntimeException("!!!La Connection n'a pu être obtenue.!!!");}
		return conn;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// construct
	public ModelVuln() {
		if (!existsUser("root"))
			saveUser("root", RESET_PASSWORD, "admin");
	}

	public ArrayList<Vulnerabilite> getVulnerabilites() {
		return vulnerabilites;
	}

	public void setVulnerabilites(ArrayList<Vulnerabilite> vulnerabilites) {
		this.vulnerabilites = vulnerabilites;
	}

	public ArrayList<Logiciel> getLogiciels() {
		return logiciels;
	}

	public void setLogiciels(ArrayList<Logiciel> logiciels) {
		this.logiciels = logiciels;
	}
	public Vulnerabilite getVuln() {
		return vuln;
	}
	public void setVuln(Vulnerabilite vuln) {
		this.vuln = vuln;
	}

	public ArrayList<String> getSelectLogiciels() {
		return selectLogiciels;
	}

	public void setSelectLogiciels(ArrayList<String> selectLogiciels) {
		this.selectLogiciels = selectLogiciels;
	}

	public Solution getSol() {
		return sol;
	}

	public void setSol(Solution sol) {
		this.sol = sol;
	}

	public ArrayList<String> getSelectSolReferences() {
		return selectSolReferences;
	}

	public void setSelectSolReferences(ArrayList<String> selectSolReferences) {
		this.selectSolReferences = selectSolReferences;
	}

	public ArrayList<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(ArrayList<Solution> solutions) {
		this.solutions = solutions;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public void changeRole(String user) { 
		if (user == null) return;
		PreparedStatement prst = null;
		ResultSet rs = null;
		String role="";
		
		try {
			prst = getConn().prepareStatement(
					"SELECT role FROM user WHERE username = ?");
			prst.setString(1, user);
			rs = prst.executeQuery();
			if (rs != null && rs.next()) {
					role = rs.getString(1);
			}
			switch (role) {
			case "user": role="admin";break;
			case "admin": role = "user";break;
			default: return;
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	
		try {
			prst = getConn().prepareStatement(
					"UPDATE user SET role=? WHERE username=?");
			prst.setString(1, role);
			prst.setString(2, user);
			prst.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {try{if (prst!=null) prst.close();} catch (SQLException e) {e.printStackTrace();}}
	}

}
