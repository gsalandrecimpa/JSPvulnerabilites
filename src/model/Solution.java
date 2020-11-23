package model;

import java.util.ArrayList;

public class Solution {
	private int id;
	private String reference;
	private String titre;
	private String description;
	private boolean fromDB;  // used to manage linked logiciels while not yet saved in DB
	private ArrayList<String> logiciels;
	


	@Override
	public int hashCode() { // for sonarQube :)
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) { // only on id
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solution other = (Solution) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void removeLogiciel(String logNom) {
		if (logiciels!=null)
			logiciels.remove(logNom);
	}

	public void addLogiciel(String logNom) {
		if (logNom == null) return;
		if (logiciels==null)
			setLogiciels(new ArrayList<String>());
		if (!logiciels.contains(logNom))
			logiciels.add(logNom);
	}


	public Solution() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Solution(int id, String reference, String titre, String description, boolean fromDB) {
		super();
		this.id = id;
		this.reference = reference;
		this.titre = titre;
		this.description = description;
		this.fromDB = fromDB;
	}
	
	
	public ArrayList<String> getLogiciels() {
		return logiciels;
	}



	public void setLogiciels(ArrayList<String> logiciels) {
		this.logiciels = logiciels;
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
