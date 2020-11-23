package model;

import java.util.ArrayList;

public class Vulnerabilite {
	private int id;
	private String reference;
	private String titre;
	private int gravite;
	private String description;
	private boolean fromDB; // used to insert or update when saving
	private ArrayList<String> logiciels;
	private ArrayList<Solution> solutions;

	
	public void removeLogiciel(String logNom) {
		if (logiciels!=null)
			logiciels.remove(logNom);
	}

	public void addLogiciel(String logNom) {
		if (logiciels==null)
			setLogiciels(new ArrayList<String>());
		if (!logiciels.contains(logNom))
			logiciels.add(logNom);
	}

	public void removeSolution(int solId) {
		if (solutions != null)
			solutions.remove(new Solution(solId,null,null,null,false));
	}

	public void addSolution(Solution sol) {
		if (sol == null) return;
		if (solutions==null)
			setSolutions(new ArrayList<Solution>());
		solutions.add(sol);
	}


	public Vulnerabilite() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Vulnerabilite(int id, String reference, String titre, int gravite, String description, boolean fromDB) {
		super();
		this.id = id;
		this.reference = reference;
		this.titre = titre;
		this.gravite = gravite;
		this.description = description;
		this.fromDB = fromDB;
	}

	public ArrayList<String> getLogiciels() {
		return logiciels;
	}

	public void setLogiciels(ArrayList<String> logiciels) {
		this.logiciels = logiciels;
	}

	public ArrayList<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(ArrayList<Solution> solutions) {
		this.solutions = solutions;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public int getGravite() {
		return gravite;
	}
	public void setGravite(int gravite) {
		this.gravite = gravite;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFromDB() {
		return fromDB;
	}

	public void setFromDB(boolean fromDB) {
		this.fromDB = fromDB;
	}
	
}
