package model;

public class Logiciel {
	private String nom;
	private boolean linkedToUser;

	public Logiciel(String nom, boolean linkedToUser) {
		super();
		this.nom = nom;
		this.linkedToUser = linkedToUser;
	}
	

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public boolean isLinkedToUser() {
		return linkedToUser;
	}
	public void setLinkedToUser(boolean linkedToUser) {
		this.linkedToUser = linkedToUser;
	}




}
